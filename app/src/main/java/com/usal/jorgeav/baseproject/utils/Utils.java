package com.usal.jorgeav.baseproject.utils;

import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {
    public static final String PATTERN_DEFINICION_FUNCION = "^([fF] ?\\()(([a-zA-Z], ?)*[a-zA-Z])(\\))";
    public static final String PATTERN_FUNCION = "[a-zA-Z]+([a-zA-Z]*(( ?\\+ ?)[a-zA-Z])*)*$";
    public static final String PATTERN_LISTA_TERMINOS = "^([0-9]+(, ?)?)+$";

    public static String printArrayListImplicante(ArrayList<Implicante> arrayList) {
        if (!arrayList.isEmpty()) {
            StringBuilder result = new StringBuilder("");
            for (Implicante impl : arrayList) {
                result.append(impl.terminosToString()).append(", ");
            }
            try {
                result.replace(result.lastIndexOf(", "), result.lastIndexOf(", ") + 1, "");
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return result.toString();
        } else return "Empty";
    }

    public static String printArrayListInteger(ArrayList<Integer> arrayList) {
        if (!arrayList.isEmpty()) {
            StringBuilder result = new StringBuilder("");
            for (Integer impl : arrayList) {
                result.append(impl).append(", ");
            }
            try {
                result.replace(result.lastIndexOf(", "), result.lastIndexOf(", ") + 1, "");
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return result.toString();
        }
        return "Empty";
    }

    public static int[] convertIntegers(List<Integer> integers) {
        if(!integers.isEmpty()) {
            int[] ret = new int[integers.size()];
            Iterator<Integer> iterator = integers.iterator();
            for (int i = 0; i < ret.length; i++) {
                ret[i] = iterator.next();
            }
            return ret;
        } else return null;
    }

    public static boolean hasInt(int[] integers, int n) {
        for (int i = 0; i < integers.length; i++)
            if (integers[i] == n)
                return true;
        return false;
    }

    public static int getIndexAlfabeto(char character) {
        ArrayList<Character> alfabeto = new ArrayList<>();
        alfabeto.add('A'); alfabeto.add('B'); alfabeto.add('C'); alfabeto.add('D'); alfabeto.add('E');
        alfabeto.add('F'); alfabeto.add('G'); alfabeto.add('H'); alfabeto.add('I'); alfabeto.add('J');
        alfabeto.add('K'); alfabeto.add('L'); alfabeto.add('M'); alfabeto.add('N'); alfabeto.add('O');
        alfabeto.add('P'); alfabeto.add('Q'); alfabeto.add('R'); alfabeto.add('S'); alfabeto.add('T');
        alfabeto.add('U'); alfabeto.add('V'); alfabeto.add('W'); alfabeto.add('X'); alfabeto.add('Y');
        alfabeto.add('Z');
        if (alfabeto.indexOf(character) == -1) {
            alfabeto = new ArrayList<>();
            alfabeto.add('a');alfabeto.add('b');alfabeto.add('c');alfabeto.add('d');alfabeto.add('e');
            alfabeto.add('f');alfabeto.add('g');alfabeto.add('h');alfabeto.add('i');alfabeto.add('j');
            alfabeto.add('k');alfabeto.add('l');alfabeto.add('m');alfabeto.add('n');alfabeto.add('o');
            alfabeto.add('p');alfabeto.add('q');alfabeto.add('r');alfabeto.add('s');alfabeto.add('t');
            alfabeto.add('u');alfabeto.add('v');alfabeto.add('w');alfabeto.add('x');alfabeto.add('y');
            alfabeto.add('z');
        }
        return alfabeto.indexOf(character);
    }

    public static int sacarVariables(String funcion) {
        if (funcion.contains("=")) {
            String[] ladosIgualdad = funcion.split(" ?= ?");
            if (ladosIgualdad[0].matches(PATTERN_DEFINICION_FUNCION)) {
                char aP = '(';
                char cP = ')';
                int indexAP = -1;
                int indexCP = -1;
                for (int i = 0; i < ladosIgualdad[0].length(); i++) {
                    if (ladosIgualdad[0].charAt(i) == aP) indexAP = i;
                    if (ladosIgualdad[0].charAt(i) == cP) indexCP = i;
                }
                if (indexAP != -1 && indexCP != -1) {
                    String variablesStr = ladosIgualdad[0].substring(indexAP + 1, indexCP);
                    return variablesStr.split(", ?").length;
                }
            }
        }
        return 0;
    }

    public static boolean evaluarFuncion(String funcion, boolean[] valores) throws ArithmeticException{
        char aP = '(';
        int indexAP = -1;
        char cP = ')';
        int indexCP = -1;
        boolean tieneP = true;

        while(tieneP) {
            //Buscar primer "("
            for (int i = 0; i < funcion.length(); i++)
                if (aP == funcion.charAt(i)) { indexAP = i; break; }
            if (indexAP == -1)
                if (funcion.matches(PATTERN_FUNCION)) {
                    return hacerCuenta(funcion, valores);
                }

            //Buscar pareja ")"
            int contadorP = 0;
            for (int i = indexAP; i < funcion.length(); i++) {
                if (funcion.charAt(i) == aP) contadorP++;
                if (funcion.charAt(i) == cP) contadorP--;
                if (contadorP == 0) { indexCP = i; break; }
            }
            if (indexCP == -1) {
                return false/*error de parentesis*/;
            }

            //Evaluar subcadena
            boolean abc = evaluarFuncion(funcion.substring(indexAP + 1, indexCP), valores);
            StringBuilder buf = new StringBuilder(funcion)
                    .replace(indexAP, indexCP + 1, (abc?"1":"0"));
            funcion = buf.toString();

            //Hay mas parentesis en este nivel?
            tieneP = false;
            for (int i = 0; i < funcion.length(); i++)
                if (funcion.charAt(i) == aP) { tieneP = true; break; }
        }
        return hacerCuenta(funcion, valores);
    }

    private static boolean hacerCuenta(String funcionIn, boolean[] valores) {
        ArrayList<Boolean> valoresCuenta = new ArrayList<>();
        if (funcionIn.contains("+")) {
            String[] funcionTerminos = funcionIn.split("( ?\\+ ?)");
            for (int i = 0; i < funcionTerminos.length; i++) {
                valoresCuenta.add(hacerCuenta(funcionTerminos[i], valores));
            }
            Boolean result = Boolean.FALSE; //a OR 0 = a
            for (Boolean b : valoresCuenta) {
                result = result || b;
            }
            return result;
        }

        //hacer and
        for (int i = 0; i < funcionIn.length(); i++) {
            int index = getIndexAlfabeto(funcionIn.charAt(i));
            if (index != -1)
                valoresCuenta.add(valores[index]);
            else if (funcionIn.charAt(i) == '1')
                valoresCuenta.add(true);
            else if (funcionIn.charAt(i) == '0')
                valoresCuenta.add(false);
        }
        Boolean result = Boolean.TRUE; //a AND 1 = a
        for (Boolean b : valoresCuenta) {
            result = result && b;
        }
        return result;
    }
}
