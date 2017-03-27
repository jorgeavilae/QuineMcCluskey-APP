package com.usal.jorgeav.baseproject.utils;

import android.util.Log;

import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;

public class UtilsTabla {

    public static boolean[][] obtenerMarcasTabla(ArrayList<Implicante> primerosImplicantes, int[] minTerms) {
        boolean[][] tablaMarcas = new boolean[primerosImplicantes.size()][minTerms.length];

        for (Implicante implicante : primerosImplicantes) {
            for (int i = 0; i < minTerms.length; i++) {
                tablaMarcas[primerosImplicantes.indexOf(implicante)][i] = implicante.getTerminos().contains(minTerms[i]);
            }
        }
        return tablaMarcas;
    }

    public static ArrayList<Implicante> getPrimerosImplicantesEstenciales(ArrayList<Implicante> primerosImplicantes,
                                                                          boolean[][] tablaMarcas) {
        ArrayList<Implicante> result = new ArrayList<>();
        for (int i = 0; i < tablaMarcas[i].length; i++) {
            int index = indexOfUnicaMarca(tablaMarcas, i);
            if (index != -1) {
                if (!result.contains(primerosImplicantes.get(index)))
                    result.add(primerosImplicantes.get(index));
            }
        }
        return result;
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

    public static ArrayList<Implicante> completarImplicantesParaTerminos(ArrayList<Implicante> primerosImplicantes,
                                                                         ArrayList<Implicante> primerosImplicantesEsenciales,
                                                                         int[] terms, boolean isMinterm) {
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
            result.add(escogerMejorImplicante(posibles, primerosImplicantesEsenciales, isMinterm));
        }

        return result;
    }


    private static Implicante escogerMejorImplicante(ArrayList<Implicante> posibles,
                                                     ArrayList<Implicante> primerosImplicantesEsenciales,
                                                     boolean isMinterm) {

        Log.i("Utils", "escogerMejorImplicante: Posibles implicantes "+posibles.size());
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
                if (implicante.getVariablesNegadas(isMinterm) < numVariableNegadas) {
                    numVariableNegadas = implicante.getVariablesNegadas(isMinterm);
                } else if (implicante.getVariablesNegadas(isMinterm) > numVariableNegadas)
                    posibles.remove(implicante);
            }
            for (Implicante borrar : posibles)
                if (borrar.getVariablesNegadas(isMinterm) > numVariableNegadas)
                    posibles.remove(borrar);
        }
        Log.i("Utils", "escogerMejorImplicante: Posibles implicantes "+posibles.size()+" menos variables negadas");

        //El que tenga mas entradas en comun con otros
        if (posibles.size() > 1) {
            int numEntradas = 0;
            for (Implicante implicante : posibles) {
                int entradasNegadasEnComun =
                        implicante.getEntradasNegadasEnComun(primerosImplicantesEsenciales, true);
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
}
