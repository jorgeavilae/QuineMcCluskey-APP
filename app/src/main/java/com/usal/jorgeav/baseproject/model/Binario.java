package com.usal.jorgeav.baseproject.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Jorge Avila on 22/03/2017.
 */

public class Binario {
    public static final int b0 = 0;
    public static final int b1 = 1;
    public static final int bZ = 2;
    private @EstadoBinario int digito;

    public Binario(@EstadoBinario int digito) {
        this.digito = digito;
    }

    public int getDigito() {
        return digito;
    }

    public void setDigito(int digito) {
        this.digito = digito;
    }

    @Override
    public String toString() {
        switch (this.digito) {
            case b0:
                return "0";
            case b1:
                return "1";
            case bZ:
                return "-";
            default:
                return "";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Binario binario = (Binario) o;

        return digito == binario.digito;
    }

    @Override
    public int hashCode() {
        return digito;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({b0, b1, bZ})
    public @interface EstadoBinario {}
}
