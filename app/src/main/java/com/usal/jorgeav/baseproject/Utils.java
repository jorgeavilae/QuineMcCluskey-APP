package com.usal.jorgeav.baseproject;

import com.usal.jorgeav.baseproject.model.Binario;

import java.util.ArrayList;

/**
 * Created by Jorge Avila on 22/03/2017.
 */

public class Utils {

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

    public static ArrayList<Binario> compareBinarios(ArrayList<Binario> a, ArrayList<Binario> b) {
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
}
