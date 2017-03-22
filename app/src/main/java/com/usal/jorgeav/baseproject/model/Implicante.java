package com.usal.jorgeav.baseproject.model;

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

    public Implicante(ArrayList<Integer> terminos) {
        this.iteracion = 0;
        this.subseccion = 0;
        this.terminos = terminos;
        this.binarios = getBinariosFromTerminos();
        this.marca = Math.random() < 0.5;
    }

    private ArrayList<Binario> getBinariosFromTerminos() {
        ArrayList<Binario> result = new ArrayList<>();
        result.add(new Binario(Binario.bZ));
        result.add(new Binario(Binario.b0));
        result.add(new Binario(Binario.b1));
        result.add(new Binario(Binario.bZ));
        result.add(new Binario(Binario.b0));
        return result;

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
        result.replace(result.lastIndexOf(","), result.lastIndexOf(",")+1, ")" );
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
            result = result + b.toString();
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
