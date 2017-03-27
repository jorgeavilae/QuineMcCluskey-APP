package com.usal.jorgeav.baseproject.model;

import com.usal.jorgeav.baseproject.MainActivity;
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

    public Implicante(ArrayList<Integer> terminos, ArrayList<Binario> binarios, int iteracion) {
        this.terminos = terminos;
        if (terminos.size() == 1)
            this.binarios = UtilsBinarios.intToBinary(terminos.get(0), MainActivity.numVariables);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Implicante that = (Implicante) o;

        return binarios.equals(that.binarios);

    }

    @Override
    public int hashCode() {
        return binarios.hashCode();
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
}
