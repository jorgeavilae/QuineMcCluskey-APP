package com.usal.jorgeav.baseproject.utils;

import com.usal.jorgeav.baseproject.model.Binario;

import java.util.ArrayList;

public class UtilsBinarios {

    public static ArrayList<Binario> fusionaBinarios(ArrayList<Binario> a, ArrayList<Binario> b) {
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

    public static int digitosDiferentes(ArrayList<Binario> primero, ArrayList<Binario> segundo) {
        int diferentes = 0;
        for (int i = 0; i < primero.size(); i++) {
            if (!(primero.get(i).getDigito() == segundo.get(i).getDigito()))
                diferentes++;
        }
        return diferentes;
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

    public static int cuantosDigitosBinario(int n) {
        int result = 1;
        while(true) {
            if (n < Math.pow(2,result)) break;
            result++;
        }
        return result;
    }
}
