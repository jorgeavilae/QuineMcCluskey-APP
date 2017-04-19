package com.usal.jorgeav.baseproject.utils;

import android.util.Log;

import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;

public class UtilsTabla {

    public static boolean[][] obtenerMarcasTabla(ArrayList<Implicante> primerosImplicantes, int[] minTerms) {
        if (minTerms != null) {
            boolean[][] tablaMarcas = new boolean[primerosImplicantes.size()][minTerms.length];

            //Recorre la lista de implicantes
            for (Implicante implicante : primerosImplicantes) {
                for (int i = 0; i < minTerms.length; i++) {
                    //True si este implicante contiene este termino, false en caso contrario
                    tablaMarcas[primerosImplicantes.indexOf(implicante)][i] = implicante.getTerminos().contains(minTerms[i]);
                }
            }
            return tablaMarcas;
        }
        return null;
    }

    public static ArrayList<Implicante> getPrimerosImplicantesEsenciales(ArrayList<Implicante> primerosImplicantes,
                                                                          boolean[][] tablaMarcas) {
        ArrayList<Implicante> result = new ArrayList<>();
        //Recorre la tabla
        if (tablaMarcas != null) {
            if (tablaMarcas.length > 0) {
                for (int i = 0; i < tablaMarcas[0].length; i++) {
                    int index = indexOfUnicaMarca(tablaMarcas, i);
                    //Si es la unica marca de la columna i
                    if (index != -1) {
                        if (!result.contains(primerosImplicantes.get(index)))
                            result.add(primerosImplicantes.get(index));
                    }
                }
            }
        }
        return result;
    }

    public static int indexOfUnicaMarca(boolean[][] tablaMarcas, int columna) {
        int index = -1;
        for (int j = 0; j < tablaMarcas.length; j++) {
            //Si hay marca en la fila j
            if (tablaMarcas[j][columna]) {
                if (index == -1) { //Si es la primera marca
                    index = j;
                } else { //Si no es la primera marca
                    index = -1;
                    break;
                }
            }
        }
        return index;
    }

    public static ArrayList<Implicante> completarImplicantesParaTerminos(ArrayList<Implicante> primerosImplicantes,
                                                                         ArrayList<Implicante> primerosImplicantesEsenciales,
                                                                         int[] terms, boolean isMinterm) {
        ArrayList<Implicante> result = new ArrayList<>();
        result.addAll(primerosImplicantesEsenciales);

        if (terms != null) {
            ArrayList<Integer> faltantes = obtenerFaltantes(result, terms);
            ArrayList<Implicante> posibles;

            //Mientras falten terminos
            while (faltantes.size() > 0) {
                int maxTerminosContenidos = faltantes.size();
                //Buscar implicante que contenga todos los que falten,
                // si no todos los que falten menos -1, si no -2, etc.
                for (int i = maxTerminosContenidos; i >= 0; i--) {
                    posibles = new ArrayList<>();

                    for (Implicante impl : primerosImplicantes)
                        if (impl.cuantosTerminosIguales(faltantes) == i)
                            posibles.add(impl);

                    //Si hay implicantes con i terminos, escoger el mejor
                    if (posibles.size() > 0) {
                        result.add(escogerMejorImplicante(posibles, primerosImplicantesEsenciales, isMinterm));
                        break;
                    }
                }

                //Faltan mas terminos?
                faltantes = obtenerFaltantes(result, terms);
            }
        }
        return result;
    }

    private static ArrayList<Integer> obtenerFaltantes(ArrayList<Implicante> lista, int[] valores) {
        ArrayList<Integer> faltantes = new ArrayList<>();
        boolean presente;
        //Recorrer lista de terminos
        for (int i = 0; i < valores.length; i++) {
            presente = false;
            for (Implicante implicante : lista) {
                if (implicante.getTerminos().contains(Integer.valueOf(valores[i])))
                    presente = true;
            }
            //Si ningun implicante contiene el termino
            if (!presente) faltantes.add(Integer.valueOf(valores[i]));
        }
        return faltantes;
    }

    private static Implicante escogerMejorImplicante(ArrayList<Implicante> posibles,
                                                     ArrayList<Implicante> primerosImplicantesEsenciales,
                                                     boolean isMinterm) {

        Log.i("Utils", "escogerMejorImplicante: Posibles implicantes "+posibles.size()+" "+ Utils.printArrayListImplicante(posibles));
        //El más simplificado
        if (posibles.size() > 1) {
            int numTerminos = 0;
            //Recorrer posibles implicantes buscando numTerminos mas alto
            for (Implicante implicante : posibles) {
                if (implicante.getTerminos().size() > numTerminos) {
                    numTerminos = implicante.getTerminos().size();
                } else if (implicante.getTerminos().size() < numTerminos)
                    posibles.remove(implicante);
            }
            //Borramos todos los que no tengan numTerminos
            for (Implicante borrar : new ArrayList<>(posibles))
                if (borrar.getTerminos().size() < numTerminos)
                    posibles.remove(borrar);
        }
        Log.i("Utils", "escogerMejorImplicante: Posibles implicantes "+posibles.size()+" mas simplificado"+" "+ Utils.printArrayListImplicante(posibles));

        //El que tenga mas entradas en comun con otros
        if (posibles.size() > 1) {
            int numEntradas = 0;
            for (Implicante implicante : posibles) {
                //Buscar entradas negadas en comun con el resto de implicantes
                int entradasNegadasEnComun =
                        implicante.getEntradasNegadasEnComun(primerosImplicantesEsenciales, true);
                if (entradasNegadasEnComun > numEntradas) {
                    numEntradas = entradasNegadasEnComun;
                } else if (entradasNegadasEnComun < numEntradas)
                    posibles.remove(implicante);
            }
            //Borrar todos los que tengan menos entradas negadas en comun que el elegido
            for (Implicante borrar : new ArrayList<>(posibles))
                if (borrar.getEntradasNegadasEnComun(primerosImplicantesEsenciales, true) < numEntradas)
                    posibles.remove(borrar);
        }
        Log.i("Utils", "escogerMejorImplicante: Posibles implicantes "+posibles.size()+" mas entradas en comun"+" "+ Utils.printArrayListImplicante(posibles));

        //El que tenga menos variables negadas
        if (posibles.size() > 1) {
            int numVariableNegadas = 1000;
            //Recorrer la lista de implicantes
            for (Implicante implicante : posibles) {
                if (implicante.getVariablesNegadas(isMinterm) < numVariableNegadas) {
                    numVariableNegadas = implicante.getVariablesNegadas(isMinterm);
                } else if (implicante.getVariablesNegadas(isMinterm) > numVariableNegadas)
                    posibles.remove(implicante);
            }
            //Borrar todos los que tengan mas entradas negadas
            for (Implicante borrar : new ArrayList<>(posibles))
                if (borrar.getVariablesNegadas(isMinterm) > numVariableNegadas)
                    posibles.remove(borrar);
        }
        Log.i("Utils", "escogerMejorImplicante: Posibles implicantes "+posibles.size()+" menos variables negadas"+" "+ Utils.printArrayListImplicante(posibles));

        //Al final solo habra uno implicante posible,
        // si hay más, seran igual de validos.
        return posibles.get(0);
    }
}
