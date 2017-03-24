package com.usal.jorgeav.baseproject;

import com.usal.jorgeav.baseproject.model.Binario;
import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;

/**
 * Created by Jorge Avila on 22/03/2017.
 */

public class Utils {

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
                if (Utils.difierenUnDigito(implicante.getBinarios(), comparado.getBinarios())) {
                    ArrayList<Integer> terminos = new ArrayList<>();
                    terminos.addAll(implicante.getTerminos());
                    terminos.addAll(comparado.getTerminos());

                    ArrayList<Binario> binarios = Utils.compareBinarios(implicante.getBinarios(), comparado.getBinarios());

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

    private static ArrayList<Implicante> getListaDeSubseccion(ArrayList<Implicante> list, int subseccion) {
        ArrayList<Implicante> result = new ArrayList<>();
        for (Implicante i : list) {
            if (i.subseccion == subseccion)
                result.add(i);
            else if (i.subseccion > subseccion) break;
        }
        return result;
    }

    private static boolean difierenUnDigito(ArrayList<Binario> primero, ArrayList<Binario> segundo) {
        int diferentes = 0;
        for (int i = 0; i < primero.size(); i++) {
            if (!(primero.get(i).getDigito() == segundo.get(i).getDigito()))
                diferentes++;
        }
        return (diferentes == 1);
    }

    private static ArrayList<Binario> compareBinarios(ArrayList<Binario> a, ArrayList<Binario> b) {
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

    public static int contarUnos(ArrayList<Binario> binarios) {
        int contador = 0;
        for (Binario b : binarios)
            if (b.getDigito() == Binario.b1)
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
