package com.usal.jorgeav.baseproject.model;

import com.usal.jorgeav.baseproject.MainActivity;

import java.util.ArrayList;

/**
 * Created by Jorge Avila on 22/03/2017.
 */

public class Implicante {
    public int iteracion;
    public int subseccion;
    private ArrayList<Integer> terminos;
    private ArrayList<Binario> binarios;
    private boolean marca;

    public Implicante(ArrayList<Integer> terminos, ArrayList<Binario> binarios, int iteracion) {
        this.terminos = terminos;
        if (terminos.size() == 1)
            this.binarios = intToBinary(terminos.get(0), MainActivity.numVariables);
        else
            this.binarios = binarios;
        this.iteracion = iteracion;
        this.subseccion = contar1s(this.binarios);
        this.marca = false;
    }

    private ArrayList<Binario> intToBinary(int n, int numOfBits) {
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

    private int contar1s(ArrayList<Binario> binarios) {
        int contador = 0;
        for (Binario b : binarios)
            if (b.getDigito() == Binario.b1)
                contador++;
        return contador;
    }

    public ArrayList<Integer> getTerminos() {
        return terminos;
    }

    public void setTerminos(ArrayList<Integer> terminos) {
        this.terminos = terminos;
    }

    public String terminosToString() {
        StringBuilder result = new StringBuilder("(");
        for (Integer i : this.terminos) {
            result.append(i).append(",");
        }
        try { result.replace(result.lastIndexOf(","), result.lastIndexOf(",")+1, ")" );
        } catch (IndexOutOfBoundsException e) { e.printStackTrace(); }
        return result.toString().replace(",", ", ");
    }

    public ArrayList<Binario> getBinarios() {
        return binarios;
    }

    public void setBinarios(ArrayList<Binario> binarios) {
        this.binarios = binarios;
    }

    public String binariosToString() {
        String result = "";
        for (Binario b : this.binarios) {
            result = b.toString() + result;
        }
        return result;
    }

    public boolean isMarca() {
        return marca;
    }

    public void setMarca(boolean marca) {
        this.marca = marca;
    }


}
