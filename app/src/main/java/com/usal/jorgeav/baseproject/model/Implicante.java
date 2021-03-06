package com.usal.jorgeav.baseproject.model;

import com.usal.jorgeav.baseproject.utils.UtilsBinarios;

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

    public Implicante(ArrayList<Integer> terminos, ArrayList<Binario> binarios, int iteracion, int numVariables) {
        this.terminos = terminos;

        //Si solo tiene un termino, traducimos a binario
        if (terminos.size() == 1)
            this.binarios = UtilsBinarios.intToBinarios(terminos.get(0), numVariables);
        //Si no, hay que pasarselo
        else if (binarios != null) this.binarios = binarios;
        else throw new NullPointerException("\"binarios\" can not be NULL");

        this.iteracion = iteracion;
        this.subseccion = UtilsBinarios.contarUnosCeros(this.binarios, Binario.b1);
        this.marca = false;
    }

    public ArrayList<Integer> getTerminos() {
        return terminos;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Implicante that = (Implicante) o;

        //Para comprobar si son iguales se comprueban sus bits
        return binarios.equals(that.binarios);

    }

    @Override
    public int hashCode() {
        return binarios.hashCode();
    }

    public int cuantosTerminosIguales(ArrayList<Integer> integers) {
        int contador = 0;
        for (Integer i : integers) {
            if (this.terminos.contains(i))
                contador++;
        }
        return contador;
    }

    public int getVariablesNegadas(boolean isMinTerm) {
        if (isMinTerm) {
            return UtilsBinarios.contarUnosCeros(this.binarios, Binario.b0);
        } else {
            return this.subseccion;
        }
    }

    public int getEntradasNegadasEnComun(ArrayList<Implicante> lista, boolean isMinTerm) {
        int comparado;
        if (isMinTerm)  comparado = Binario.b0;
        else  comparado = Binario.b1;

        int result = 0;
        for (int i = 0; i < binarios.size(); i++) {
            if (this.binarios.get(i).getDigito() == comparado) {
                for (Implicante impl : lista) {
                    if (impl.getBinarios().get(i).getDigito() == this.binarios.get(i).getDigito()) {
                        result++;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public boolean isTotalReduce() {
        for (Binario b : this.binarios) {
            if (b.getDigito() != Binario.bZ)
                return false;
        }
        return true;
    }
}
