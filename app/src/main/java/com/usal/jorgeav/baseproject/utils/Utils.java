package com.usal.jorgeav.baseproject.utils;

import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {

    public static String printArrayListImplicante(ArrayList<Implicante> arrayList) {
        StringBuilder result = new StringBuilder("");
        for (Implicante impl : arrayList) {
            result.append(impl.terminosToString()).append(", ");
        }
        try { result.replace(result.lastIndexOf(", "), result.lastIndexOf(", ")+1, "");
        } catch (IndexOutOfBoundsException e) { e.printStackTrace(); }
        return result.toString();
    }

    public static String printArrayListInteger(ArrayList<Integer> arrayList) {
        StringBuilder result = new StringBuilder("");
        for (Integer impl : arrayList) {
            result.append(impl).append(", ");
        }
        try { result.replace(result.lastIndexOf(", "), result.lastIndexOf(", ")+1, "");
        } catch (IndexOutOfBoundsException e) { e.printStackTrace(); }
        return result.toString();
    }

    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
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

    public static int[] obtenerRestoDeTerminos(int numVariables, int[] minTerms, int[] no_ni) {
        ArrayList<Integer> result = new ArrayList<>();
        int maximo = (int) Math.pow(2, numVariables);
        for (int termino = 0; termino < maximo; termino++) {
            boolean presente = false;
            for (int j = 0; j < minTerms.length; j++)
                if (minTerms[j] == termino) {
                    presente = true;
                    break;
                }
            for (int j = 0; j < no_ni.length; j++)
                if (no_ni[j] == termino) {
                    presente = true;
                    break;
                }
            if (!presente) result.add(termino);
        }
        return convertIntegers(result);
    }
}
