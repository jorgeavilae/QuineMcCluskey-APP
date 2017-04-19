package com.usal.jorgeav.baseproject.utils;

import com.usal.jorgeav.baseproject.model.Binario;
import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {
    public static final String PATTERN_DEFINICION_FUNCION = "^([fF] ?\\()(([a-zA-Z], ?)*[a-zA-Z])(\\))";
    public static final String PATTERN_FUNCION = "[a-zA-Z]+([a-zA-Z]*(( ?\\+ ?)[a-zA-Z])*)*$";
    public static final String PATTERN_LISTA_TERMINOS = "^\\(([0-9]+(, ?)?)+\\)$";
    private static final int LETRAS_EN_ALFABETO = 26;

    public static String printArrayListImplicante(ArrayList<Implicante> arrayList) {
        if (arrayList != null && !arrayList.isEmpty()) {
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
        } else return "()";
    }

    public static String printArrayListInteger(ArrayList<Integer> arrayList) {
        if (arrayList != null && !arrayList.isEmpty()) {
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
        return "";
    }

    public static String printboolean(boolean[] array) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                result = result + (array[i] ? "1" : "0") + " ";
            }
        }
        return result;
    }

    public static String printintegers(int[] array) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                result = result + String.valueOf(array[i]) + " ";
            }
        }
        return result;
    }

    public static int[] convertIntegers(List<Integer> integers) {
        if (!integers.isEmpty()) {
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
        alfabeto.add('a');
        alfabeto.add('b');
        alfabeto.add('c');
        alfabeto.add('d');
        alfabeto.add('e');
        alfabeto.add('f');
        alfabeto.add('g');
        alfabeto.add('h');
        alfabeto.add('i');
        alfabeto.add('j');
        alfabeto.add('k');
        alfabeto.add('l');
        alfabeto.add('m');
        alfabeto.add('n');
        alfabeto.add('o');
        alfabeto.add('p');
        alfabeto.add('q');
        alfabeto.add('r');
        alfabeto.add('s');
        alfabeto.add('t');
        alfabeto.add('u');
        alfabeto.add('v');
        alfabeto.add('w');
        alfabeto.add('x');
        alfabeto.add('y');
        alfabeto.add('z');
        alfabeto.add('A');
        alfabeto.add('B');
        alfabeto.add('C');
        alfabeto.add('D');
        alfabeto.add('E');
        alfabeto.add('F');
        alfabeto.add('G');
        alfabeto.add('H');
        alfabeto.add('I');
        alfabeto.add('J');
        alfabeto.add('K');
        alfabeto.add('L');
        alfabeto.add('M');
        alfabeto.add('N');
        alfabeto.add('O');
        alfabeto.add('P');
        alfabeto.add('Q');
        alfabeto.add('R');
        alfabeto.add('S');
        alfabeto.add('T');
        alfabeto.add('U');
        alfabeto.add('V');
        alfabeto.add('W');
        alfabeto.add('X');
        alfabeto.add('Y');
        alfabeto.add('Z');
        return alfabeto.indexOf(character);
    }

    public static char getAfabetoFromIndex(int index, boolean isMinuscula) {
        ArrayList<Character> alfabeto;
        if (isMinuscula) {
            alfabeto = new ArrayList<>();
            alfabeto.add('a');
            alfabeto.add('b');
            alfabeto.add('c');
            alfabeto.add('d');
            alfabeto.add('e');
            alfabeto.add('f');
            alfabeto.add('g');
            alfabeto.add('h');
            alfabeto.add('i');
            alfabeto.add('j');
            alfabeto.add('k');
            alfabeto.add('l');
            alfabeto.add('m');
            alfabeto.add('n');
            alfabeto.add('o');
            alfabeto.add('p');
            alfabeto.add('q');
            alfabeto.add('r');
            alfabeto.add('s');
            alfabeto.add('t');
            alfabeto.add('u');
            alfabeto.add('v');
            alfabeto.add('w');
            alfabeto.add('x');
            alfabeto.add('y');
            alfabeto.add('z');
        } else {
            alfabeto = new ArrayList<>();
            alfabeto.add('A');
            alfabeto.add('B');
            alfabeto.add('C');
            alfabeto.add('D');
            alfabeto.add('E');
            alfabeto.add('F');
            alfabeto.add('G');
            alfabeto.add('H');
            alfabeto.add('I');
            alfabeto.add('J');
            alfabeto.add('K');
            alfabeto.add('L');
            alfabeto.add('M');
            alfabeto.add('N');
            alfabeto.add('O');
            alfabeto.add('P');
            alfabeto.add('Q');
            alfabeto.add('R');
            alfabeto.add('S');
            alfabeto.add('T');
            alfabeto.add('U');
            alfabeto.add('V');
            alfabeto.add('W');
            alfabeto.add('X');
            alfabeto.add('Y');
            alfabeto.add('Z');
        }
        if (index > -1 && index < alfabeto.size())
            return alfabeto.get(index);
        else
            return ' ';
    }

    public static ArrayList<Integer> obtenerRestoDeTerminos(int numVariables, ArrayList<Integer> minTerms, int[] no_ni) {
        ArrayList<Integer> result = new ArrayList<>();
        int maximo = (int) Math.pow(2, numVariables);
        for (int termino = 0; termino < maximo; termino++) {
            boolean presente = false;
            for (Integer i : minTerms)
                if (i == termino) {
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
        return result;
    }

    public static int sacarVariables(String funcion) {
        if (funcion.contains("=")) {
            String[] ladosIgualdad = funcion.split(" ?= ?");
            //Si lo que hay a la izq del = es una definicion de funcion
            if (ladosIgualdad[0].matches(PATTERN_DEFINICION_FUNCION)) {
                char aP = '(';
                char cP = ')';
                int indexAP = -1;
                int indexCP = -1;
                //Busco los indices de los parentisis
                for (int i = 0; i < ladosIgualdad[0].length(); i++) {
                    if (ladosIgualdad[0].charAt(i) == aP) indexAP = i;
                    if (ladosIgualdad[0].charAt(i) == cP) indexCP = i;
                }
                if (indexAP != -1 && indexCP != -1) {
                    //Saco lo que hay entre esos indices
                    String variablesStr = ladosIgualdad[0].substring(indexAP + 1, indexCP);

                    //Devuelve una lista de las String con cada letra
                    return variablesStr.split(", ?").length;
                }
            }
        }
        return 0;
    }

    public static boolean evaluarFuncion(String funcion, boolean[] valores) throws Exception {
        //Caso especial f()=1 o f()=0
        if (funcion.equals("1")) return true;
        if (funcion.equals("0")) return false;

        char aP = '(';
        int indexAP;
        char cP = ')';
        int indexCP;
        boolean tieneP = true;

        //Se hara el bucle al menos una vez
        // y tantas veces como parentesis haya en este nivel
        while (tieneP) {
            indexAP = -1;
            indexCP = -1;

            //Buscar primer "("
            for (int i = 0; i < funcion.length(); i++)
                if (aP == funcion.charAt(i)) {
                    indexAP = i;
                    break;
                }
            //Si no hay parentesis hace la cuenta
            if (indexAP == -1)
                if (funcion.matches(PATTERN_FUNCION)) {
                    return hacerCuenta(funcion, valores);
                }

            //Buscar pareja ")", distinto de buscar el siguiente ")"
            int contadorP = 0;
            for (int i = indexAP; i < funcion.length(); i++) {
                if (funcion.charAt(i) == aP) contadorP++;
                if (funcion.charAt(i) == cP) contadorP--;
                if (contadorP == 0) {
                    indexCP = i;
                    break;
                }
            }
            if (indexCP == -1) {
                throw new Exception("Error de parentesis");
            }

            //Evaluar subcadena con llamada recursiva
            boolean abc = evaluarFuncion(funcion.substring(indexAP + 1, indexCP), valores);
            //Sustitur la cadena por 1 o 0
            StringBuilder buf = new StringBuilder(funcion)
                    .replace(indexAP, indexCP + 1, (abc ? "1" : "0"));
            funcion = buf.toString();

            //Hay mas parentesis en este nivel?
            tieneP = false;
            for (int i = 0; i < funcion.length(); i++)
                if (funcion.charAt(i) == aP) {
                    tieneP = true;
                    break;
                }
        }

        //En funcion hay 1s y 0s con operadores AND y OR
        return hacerCuenta(funcion, valores);
    }

    private static boolean hacerCuenta(String funcionIn, boolean[] valores) {
        //Se van añadiendo boolean dependiendo
        // del valor de cada variable de la expresion
        ArrayList<Boolean> valoresCuenta = new ArrayList<>();

        //Si hay operaciones OR
        if (funcionIn.contains("+")) {
            //Separar la funcion en segmentos sin + y evaluarla
            String[] funcionTerminos = funcionIn.split("( ?\\+ ?)");
            for (int i = 0; i < funcionTerminos.length; i++) {
                valoresCuenta.add(hacerCuenta(funcionTerminos[i], valores));
            }
            Boolean result = Boolean.FALSE; //a OR 0 = a
            //Hacer OR de todos los valores
            for (Boolean b : valoresCuenta) {
                result = result || b;
            }
            return result;
        } else { //Si no hacer AND
            for (int i = 0; i < funcionIn.length(); i++) {
                //Obtener el index del alfabeto (coincide con index de valores[])
                int index = getIndexAlfabeto(funcionIn.charAt(i));
                if (index != -1) {
                    if (index >= LETRAS_EN_ALFABETO) //Mayuscula: negamos el valor
                        valoresCuenta.add(!valores[index - LETRAS_EN_ALFABETO]);
                    else //Minuscula
                        valoresCuenta.add(valores[index]);
                } else if (funcionIn.charAt(i) == '1') //Si es 1 -> true
                    valoresCuenta.add(true);
                else if (funcionIn.charAt(i) == '0') //Si es 0 -> false
                    valoresCuenta.add(false);
            }
            Boolean result = Boolean.TRUE; //a AND 1 = a
            //Hacer AND de todos los valores
            for (Boolean b : valoresCuenta) {
                result = result && b;
            }
            return result;
        }
    }

    public static String escribirFuncionFromImplicantes(ArrayList<Implicante> implicantes, boolean isMinterm) {
        //Si la lista de implicantes esta vacia
        if (implicantes.size() == 0) {/* f=0 o f=1 / minterm o maxterm*/
            if (isMinterm) return "0";
            else return "1";
        }
        //Si el unico implicante esta totalmente simplificado (---)
        if (implicantes.size() == 1 && implicantes.get(0).isTotalReduce()) {/* f=1 o f=0 / minterm o maxterm*/
            if (isMinterm) return "1";
            else return "0";
        }


        StringBuilder result = new StringBuilder("");
        String or = "+";
        String and = "·";
        String abreParentesis = "(";
        String cierraParentesis = ")";
        if (isMinterm) { /*Los 0 son variables negadas*/

            //Recorrer implicantes
            for (Implicante impl : implicantes) {
                //Traducir cada binario en su letra correspondiente
                for (int i = 0; i < impl.getBinarios().size(); i++) {
                    switch (impl.getBinarios().get(i).getDigito()) {
                        case Binario.b0:
                            result.append(getAfabetoFromIndex(i, false)); /*Mayusculas*/
                            break;
                        case Binario.b1:
                            result.append(getAfabetoFromIndex(i, true)); /*MInusculas*/
                            break;
                        case Binario.bZ:
                            break;
                    }
                }
                //Unir cada implicante con +
                result.append(or);
            }
            //Quitar el ultimo +
            try {
                result.replace(result.lastIndexOf(or), result.lastIndexOf(or) + 1, "");
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else { /*Los 1 son variables negadas*/

            //Recorrer implicantes
            for (Implicante impl : implicantes) {
                //Abrir parentesis
                if (implicantes.size() > 1) result.append(abreParentesis);
                //Traducir cada binario en su letra correspondiente y "+"
                for (int i = 0; i < impl.getBinarios().size(); i++) {
                    switch (impl.getBinarios().get(i).getDigito()) {
                        case Binario.b0:
                            result.append(getAfabetoFromIndex(i, true)).append(or); /*Minusculas*/
                            break;
                        case Binario.b1:
                            result.append(getAfabetoFromIndex(i, false)).append(or); /*Mayusculas*/
                            break;
                        case Binario.bZ:
                            break;
                    }
                }
                //Quitar el ultimo +
                try {
                    result.replace(result.lastIndexOf(or), result.lastIndexOf(or) + 1, "");
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                if (implicantes.size() > 1) {
                    if (!result.toString().contains(or)) { //Si no hay +, quitar "("
                        try {
                            result.replace(result.indexOf(abreParentesis), result.indexOf(abreParentesis) + 1, "");
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    } else { //Si hay +, poner ")"
                        result.append(cierraParentesis);
                    }
                    //Unir cada implicante con "·"
                    result.append(and);
                }
            }
            //Quitar ultimo "·"
            try {
                result.replace(result.lastIndexOf(and), result.lastIndexOf(and) + 1, "");
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    public static int contarPuertas(String funcion, boolean isMinterm) {
        //Casos especiales
        if (funcion.equals("1") || funcion.equals("0") || funcion.equals(""))
            /* f=1 o f=0 / minterm o maxterm*/
            return 0;

        int result = 0;
        ArrayList<Integer> variablesNegadasPresentes = new ArrayList<>();
        String or = "\\+";
        String and = "·";
        String abreParentesis = "\\(";
        String cierraParentesis = "\\)";

        if (isMinterm) { /*Minterms*/
            String[] mintermsStr = funcion.split(or);
            if (mintermsStr.length > 1) result++; /*Puerta OR*/
            for (String aMintermsStr : mintermsStr) {
                if (aMintermsStr.length() > 1) result++; /*Puerta AND*/
                //Para cada letra
                for (int j = 0; j < aMintermsStr.length(); j++) {
                    int index = getIndexAlfabeto(aMintermsStr.charAt(j));
                    if (index != -1)
                        if (index >= LETRAS_EN_ALFABETO) /*Es Mayuscula => esta negada*/
                            //Si no esta negada en otro implicante
                            if (!variablesNegadasPresentes.contains(Integer.valueOf(index))) {
                                result++; /*Puerta NOT*/
                                variablesNegadasPresentes.add(index);
                            }
                }
            }
        } else { /*Maxterms*/
            String[] maxtermsStr = funcion.split(and);
            if (maxtermsStr.length > 1) result++; /*Puerta AND*/
            for (String aMaxtermsStr : maxtermsStr) {
                //Quitar parentesis de cada operador del "·"
                aMaxtermsStr = aMaxtermsStr.replace(abreParentesis, "");
                aMaxtermsStr = aMaxtermsStr.replace(cierraParentesis, "");
                String[] maxtermsStrNoMas = aMaxtermsStr.split(or);
                if (maxtermsStrNoMas.length > 1) result++; /*Puerta OR*/
                //Para cada operador del "+"
                for (String aMaxtermsStrNoMas : maxtermsStrNoMas) {
                    for (int j = 0; j < aMaxtermsStrNoMas.length(); j++) {
                        int index = getIndexAlfabeto(aMaxtermsStrNoMas.charAt(j));
                        if (index != -1)
                            if (index >= LETRAS_EN_ALFABETO) /*Es Mayuscula => esta negada*/
                                //Si no esta negada en otro implicante
                                if (!variablesNegadasPresentes.contains(Integer.valueOf(index))) {
                                    result++; /*Puerta NOT*/
                                    variablesNegadasPresentes.add(index);
                                }
                    }
                }
            }
        }

        return result;
    }

}
