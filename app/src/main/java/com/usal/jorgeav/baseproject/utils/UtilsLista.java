package com.usal.jorgeav.baseproject.utils;

import com.usal.jorgeav.baseproject.model.Binario;
import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;

/**
 * Created by Jorge Avila on 27/03/2017.
 */

public class UtilsLista {

    public static ArrayList<Implicante> termsToList(int[] ints, int numOfBits) {
        ArrayList<Implicante> result = new ArrayList<>();
        if (ints != null) {
            ArrayList<Integer> terminos;
            for (int anInt : ints) {
                terminos = new ArrayList<>(1);
                terminos.add(anInt);
                //Implicante con un termino, en la iteracion 0, con numOfBits bits
                result.add(new Implicante(terminos, null, 0, numOfBits));
            }
        }
        return result;
    }

    public static ArrayList<Implicante> ordenarIteracion(ArrayList<Implicante> list, int numOfBits) {
        ArrayList<Implicante> ordenado = new ArrayList<>();

        //Busca los implicantes que tengan desde 0 hasta numOfBits número de 1
        // (igual que nº subseccion) y los va añadiendo a la lista ordenada
        for (int i = 0; i <= numOfBits; i++)
            for (Implicante implicante : list)
                if (implicante.subseccion == i) {
                    ordenado.add(new Implicante(implicante.getTerminos(), null, implicante.iteracion+1, numOfBits));
                    implicante.setMarca(true);
                }
        return ordenado;
    }

    public static ArrayList<Implicante> emparejarIteracion(ArrayList<Implicante> list, int numOfBits) {
        ArrayList<Implicante> result = new ArrayList<>();
        //Recorrer lista implicantes
        for (Implicante implicante : list) {
            //Obtener implicantes con un 1 más que este
            ArrayList<Implicante> subList = getListaDeSubseccion(list, implicante.subseccion+1);
            for (Implicante comparado : subList) {
                //Si solo se diferencian en un digito
                if (UtilsBinarios.digitosDiferentes(implicante.getBinarios(), comparado.getBinarios()) == 1) {
                    //Terminos del nuevo implicante
                    ArrayList<Integer> terminos = new ArrayList<>();
                    terminos.addAll(implicante.getTerminos());
                    terminos.addAll(comparado.getTerminos());

                    //Binarios del nuevo implicante
                    ArrayList<Binario> binarios =
                            UtilsBinarios.fusionaBinarios(implicante.getBinarios(), comparado.getBinarios());

                    //Nuevo implicante
                    Implicante nuevoImplicante = new Implicante(terminos, binarios, implicante.iteracion+1, numOfBits);
                    //Si no contiene al nuevo Implicante, añadir
                    // (ver metodo Implicante.equals)
                    if (!result.contains(nuevoImplicante)) {
                        result.add(nuevoImplicante);
                        marcarSimplificados(list, terminos);
                    }
                }
            }
        }

        return result;
    }

    public static ArrayList<Implicante> buscarPrimerosImplicantes(ArrayList<Implicante> implicantes) {
        ArrayList<Implicante> result = new ArrayList<>();
        for (Implicante implicante : implicantes)
            if (!implicante.isMarca())
                result.add(implicante);
        //Devuelve los Implicantes no marcados
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

    private static void marcarSimplificados(ArrayList<Implicante> implicantes, ArrayList<Integer> terminos) {
        for (Implicante i : implicantes)
            if (terminos.containsAll(i.getTerminos()))
                i.setMarca(true);
    }
}
