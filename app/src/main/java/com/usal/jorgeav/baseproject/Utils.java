package com.usal.jorgeav.baseproject;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.usal.jorgeav.baseproject.model.Binario;
import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Jorge Avila on 22/03/2017.
 */

public class Utils {

    private static String cellIdFormat = "%02d%02d";

    public static ArrayList<Implicante> termsToList(int[] ints) {
        ArrayList<Implicante> result = new ArrayList<>(ints.length);
        ArrayList<Integer> terminos;
        for (int anInt : ints) {
            terminos = new ArrayList<>(1);
            terminos.add(anInt);
            result.add(new Implicante(terminos, null, 0));
        }
        return result;
    }

    public static ArrayList<Implicante> ordenarIteracion(ArrayList<Implicante> list, int numOfBits) {
        ArrayList<Implicante> ordenado = new ArrayList<>();
        for (int i = 0; i <= numOfBits; i++)
            for (Implicante implicante : list)
                if (implicante.subseccion == i) {
                    ordenado.add(new Implicante(implicante.getTerminos(), null, implicante.iteracion + 1));
                    implicante.setMarca(true);
                }
        return ordenado;
    }

    public static ArrayList<Implicante> emparejarIteracion(ArrayList<Implicante> list) {
        ArrayList<Implicante> result = new ArrayList<>();

        for (Implicante implicante : list) {
            ArrayList<Implicante> subList = Utils.getListaDeSubseccion(list, implicante.subseccion+1);
            for (Implicante comparado : subList) {
                if (Utils.digitosDiferentes(implicante.getBinarios(), comparado.getBinarios()) == 1) {
                    ArrayList<Integer> terminos = new ArrayList<>();
                    terminos.addAll(implicante.getTerminos());
                    terminos.addAll(comparado.getTerminos());

                    ArrayList<Binario> binarios = Utils.fusionaBinarios(implicante.getBinarios(), comparado.getBinarios());

                    Implicante nuevoImplicante = new Implicante(terminos, binarios, implicante.iteracion + 1);
                    if (!result.contains(nuevoImplicante)) {
                        result.add(nuevoImplicante);
                        Utils.marcarSimplificados(list, terminos);
                    }
                }
            }
        }

        return result;
    }


    public static boolean[][] dibujarTabla(Context context, TableLayout tableLayout, ArrayList<Implicante> primerosImplicantes, int[] minTerms) {
        boolean[][] tablaMarcas = new boolean[primerosImplicantes.size()][minTerms.length];
        String marcaCelda;

        //Cabecera
        TableRow row = new TableRow(context);
        TextView tv = getNewTextView(context, "", true, -1, -1);
        row.addView(tv);
        for (int i = 0; i < minTerms.length; i++) {
            tv = getNewTextView(context, Integer.toString(minTerms[i]), true, -1, -1);
            row.addView(tv);
        }
        tableLayout.addView(row);

        //Filas
        for (Implicante implicante : primerosImplicantes) {
            row = new TableRow(context);
            tv = getNewTextView(context, implicante.terminosToString(), true, -1, -1);
            row.addView(tv);
            for (int i = 0; i < minTerms.length; i++) {
                if (implicante.getTerminos().contains(minTerms[i])) {
                    tablaMarcas[primerosImplicantes.indexOf(implicante)][i] = true;
                    marcaCelda = "X";
                } else {
                    tablaMarcas[primerosImplicantes.indexOf(implicante)][i] = false;
                    marcaCelda = "";
                }
                tv = getNewTextView(context, marcaCelda, false, primerosImplicantes.indexOf(implicante), i);
                row.addView(tv);
            }
            tableLayout.addView(row);
        }

        tableLayout.setColumnStretchable(0, false);
        return tablaMarcas;
    }

    public static ArrayList<Implicante> getPrimerosImplicantesEstenciales(Context context, TableLayout tableLayout, ArrayList<Implicante> primerosImplicantes, int[] terms, boolean[][] tablaMarcas) {
        ArrayList<Implicante> result = new ArrayList<>();
        for (int i = 0; i < terms.length; i++) {
            int index = indexOfUnicaMarca(tablaMarcas, i);
            if (index != -1) {
                if (!result.contains(primerosImplicantes.get(index)))
                    result.add(primerosImplicantes.get(index));
                TextView tv = (TextView)tableLayout.findViewById(Integer.valueOf(String.format(Locale.ENGLISH, cellIdFormat, index, i)));
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            }
        }
        return result;
    }

    public static ArrayList<Implicante> completarImplicantesParaTerminos(ArrayList<Implicante> primerosImplicantes, ArrayList<Implicante> primerosImplicantesEsenciales, int[] terms) {
        ArrayList<Implicante> result = new ArrayList<>();
        result.addAll(primerosImplicantesEsenciales);

        ArrayList<Integer> faltantes = new ArrayList<>();
        boolean presente;
        for (int i = 0; i < terms.length; i++) {
            presente = false;
            for (Implicante implicante : primerosImplicantesEsenciales) {
                if (implicante.getTerminos().contains(Integer.valueOf(terms[i])))
                    presente = true;
            }
            if (!presente) faltantes.add(Integer.valueOf(terms[i]));
        }

        ArrayList<Implicante> posibles;
        for (Integer i : faltantes) {
            posibles = new ArrayList<>();
            for (Implicante implicante : primerosImplicantes) {
                if (implicante.getTerminos().contains(i))
                    posibles.add(implicante);
            }
            result.add(escogerMejorImplicante(posibles, primerosImplicantesEsenciales));
        }

        return result;
    }

    private static Implicante escogerMejorImplicante(ArrayList<Implicante> posibles, ArrayList<Implicante> primerosImplicantesEsenciales) {

        Log.i("Utils", "escogerMejorImplicante: Posibles implicantes "+posibles.size());
        //todo comprobar que pasa si no es ni < ni > sino =
        //El más simplificado
        if (posibles.size() > 1) {
            int numTerminos = 0;
            for (Implicante implicante : posibles) {
                if (implicante.getTerminos().size() > numTerminos) {
                    numTerminos = implicante.getTerminos().size();
                } else if (implicante.getTerminos().size() < numTerminos)
                    posibles.remove(implicante);
            }
            for (Implicante borrar : posibles)
                if (borrar.getTerminos().size() < numTerminos)
                    posibles.remove(borrar);
        }
        Log.i("Utils", "escogerMejorImplicante: Posibles implicantes "+posibles.size()+" mas simplificado");

        //El que tenga menos variables negadas
        if (posibles.size() > 1) {
            int numVariableNegadas = 1000;
            for (Implicante implicante : posibles) {
                if (implicante.getVariablesNegadas(true) < numVariableNegadas) {
                    numVariableNegadas = implicante.getVariablesNegadas(true);
                } else if (implicante.getVariablesNegadas(true) > numVariableNegadas)
                    posibles.remove(implicante);
            }
            for (Implicante borrar : posibles)
                if (borrar.getVariablesNegadas(true) > numVariableNegadas)
                    posibles.remove(borrar);
        }
        Log.i("Utils", "escogerMejorImplicante: Posibles implicantes "+posibles.size()+" menos variables negadas");

        //El que tenga mas entradas en comun con otros
        if (posibles.size() > 1) {
            int numEntradas = 0;
            for (Implicante implicante : posibles) {
                int entradasNegadasEnComun = implicante.getEntradasNegadasEnComun(primerosImplicantesEsenciales, true);
                if (entradasNegadasEnComun > numEntradas) {
                    numEntradas = entradasNegadasEnComun;
                } else if (entradasNegadasEnComun < numEntradas)
                    posibles.remove(implicante);
            }
            for (Implicante borrar : posibles)
                if (borrar.getEntradasNegadasEnComun(primerosImplicantesEsenciales, true) < numEntradas)
                    posibles.remove(borrar);
        }

        Log.i("Utils", "escogerMejorImplicante: Posibles implicantes "+posibles.size()+" mas entradas en comun");
        return posibles.get(0);
    }

    private static ArrayList<Implicante> getListaDeSubseccion(ArrayList<Implicante> list, int subseccion) {
        ArrayList<Implicante> result = new ArrayList<>();
        for (Implicante i : list) {
            if (i.subseccion == subseccion)
                result.add(i);
            else if (i.subseccion > subseccion) break;
        }
        return result;
    }

    public static int digitosDiferentes(ArrayList<Binario> primero, ArrayList<Binario> segundo) {
        int diferentes = 0;
        for (int i = 0; i < primero.size(); i++) {
            if (!(primero.get(i).getDigito() == segundo.get(i).getDigito()))
                diferentes++;
        }
        return diferentes;
    }

    private static ArrayList<Binario> fusionaBinarios(ArrayList<Binario> a, ArrayList<Binario> b) {
        ArrayList<Binario> result = new ArrayList<>();
        if (a.size() == b.size())
            for (int i = 0; i < a.size(); i++) {
                if (a.get(i).getDigito() == b.get(i).getDigito())
                    result.add(new Binario(a.get(i).getDigito()));
                else
                    result.add(new Binario(Binario.bZ));
            }

        return result;
    }

    private static void marcarSimplificados(ArrayList<Implicante> implicantes, ArrayList<Integer> terminos) {
        for (Implicante i : implicantes)
            if (terminos.containsAll(i.getTerminos()))
                i.setMarca(true);
    }

    private static TextView getNewTextView(Context context, String text, boolean isHeader, int x, int y) {
        TextView tv = new TextView(context);
        tv.setText(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            tv.setTextAppearance(android.R.style.TextAppearance_Material_Subhead);
        else
            tv.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Subtitle);
        if (!isHeader)
            tv.setTypeface(Typeface.MONOSPACE);
        if (x != -1 && y != -1)
            tv.setId(Integer.valueOf(String.format(Locale.ENGLISH, cellIdFormat, x, y)));
        tv.setGravity(Gravity.CENTER);
        tv.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_shape));
        return tv;
    }

    private static int indexOfUnicaMarca(boolean[][] tablaMarcas, int columna) {
        int index = -1;
        for (int j = 0; j < tablaMarcas.length; j++) {
            if (tablaMarcas[j][columna]) {
                if (index == -1) {
                    index = j;
                } else {
                    index = -1;
                    break;
                }
            }
        }
        return index;
    }

    public static ArrayList<Implicante> buscarPrimerosImplicantes(ArrayList<Implicante> implicantes) {
        ArrayList<Implicante> result = new ArrayList<>();
        for (Implicante implicante : implicantes)
            if (!implicante.isMarca())
                result.add(implicante);
        return result;
    }

    public static ArrayList<Binario> intToBinary(int n, int numOfBits) {
        ArrayList<Binario> result = new ArrayList<>();
        for(int i = 0; i < numOfBits; ++i, n/=2) {
            switch (n % 2) {
                case 0:
                    result.add(new Binario(Binario.b0));
                    break;
                case 1:
                    result.add(new Binario(Binario.b1));
                    break;
            }
        }
        return result;
    }

    public static int contarUnosCeros(ArrayList<Binario> binarios, @Binario.EstadoBinario int buscado) {
        int contador = 0;
        for (Binario b : binarios)
            if (b.getDigito() == buscado)
                contador++;
        return contador;
    }

    public static ArrayList<String> getAlfabeto() {
        ArrayList<String> result = new ArrayList<>();
        result.add("A"); result.add("B"); result.add("C"); result.add("D"); result.add("E");
        result.add("F"); result.add("G"); result.add("H"); result.add("I"); result.add("J");
        result.add("K"); result.add("L"); result.add("M"); result.add("N"); result.add("O");
        result.add("P"); result.add("Q"); result.add("R"); result.add("S"); result.add("T");
        result.add("U"); result.add("V"); result.add("W"); result.add("X"); result.add("Y");
        result.add("Z");
        result.add("a"); result.add("b"); result.add("c"); result.add("d"); result.add("e");
        result.add("f"); result.add("g"); result.add("h"); result.add("i"); result.add("j");
        result.add("k"); result.add("l"); result.add("m"); result.add("n"); result.add("o");
        result.add("p"); result.add("q"); result.add("r"); result.add("s"); result.add("t");
        result.add("u"); result.add("v"); result.add("w"); result.add("x"); result.add("y");
        result.add("z");
        return result;
    }

}
