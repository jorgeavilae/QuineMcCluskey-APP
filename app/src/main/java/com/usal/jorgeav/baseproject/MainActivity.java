package com.usal.jorgeav.baseproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.usal.jorgeav.baseproject.model.Implicante;
import com.usal.jorgeav.baseproject.utils.Utils;
import com.usal.jorgeav.baseproject.utils.UtilsBinarios;
import com.usal.jorgeav.baseproject.utils.UtilsLista;
import com.usal.jorgeav.baseproject.utils.UtilsTabla;

import java.util.ArrayList;

// TODO: 01/04/2017 Lint;
public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {
    public static final String BUNDLE_ET_FUNCION_KEY = "BUNDLE_ET_FUNCION_KEY";
    public static final String BUNDLE_ET_NONI_KEY = "BUNDLE_ET_NONI_KEY";
    private static final String BUNDLE_FRAGMENT_KEY = "BUNDLE_FRAGMENT_KEY";

    String etFuncion;
    String etNoni;

    //Numero de variables de la funcion
    int numVariables = 0;
    //Lista de minterms
    int minTerms[];
    //Lista de Maxterms
    int maxTerms[];
    //Lista de Terminos NO/NI
    int no_ni[];

    //Lista de listas de implicantes
    ArrayList<ArrayList<Implicante>> listaIteracionesMinterm;
    ArrayList<ArrayList<Implicante>> listaIteracionesMaxterm;
    //Primeros Implicantes
    ArrayList<Implicante> primerosImplicantesMinterm;
    ArrayList<Implicante> primerosImplicantesMaxterm;
    //Marcas en la Tabla de Primeros Implicantes
    boolean[][] tablaMarcasMinterm;
    boolean[][] tablaMarcasMaxterm;
    //Implicantes esenciales y necesarios que forman la funcion simplificada
    ArrayList<Implicante> primerosImplicantesTotalesMinterm;
    ArrayList<Implicante> primerosImplicantesTotalesMaxterm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_FRAGMENT_KEY)) {
            Fragment fragment = getSupportFragmentManager().getFragment(savedInstanceState, BUNDLE_FRAGMENT_KEY);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commitNow();
        } else {
            MainFragment mainFragment = new MainFragment();
            if (savedInstanceState != null
                    && savedInstanceState.containsKey(BUNDLE_ET_FUNCION_KEY)
                    && savedInstanceState.containsKey(BUNDLE_ET_NONI_KEY)) {
                Bundle b = new Bundle(savedInstanceState);
                mainFragment.setArguments(b);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mainFragment).commitNow();

        }
    }

    private void resetVariables() {
        numVariables = 0;
        minTerms = null;
        maxTerms = null;
        no_ni = null;
        listaIteracionesMinterm = null;
        tablaMarcasMinterm = null;
        primerosImplicantesMinterm = null;
        primerosImplicantesTotalesMinterm = null;
        listaIteracionesMaxterm = null;
        tablaMarcasMaxterm = null;
        primerosImplicantesMaxterm = null;
        primerosImplicantesTotalesMaxterm = null;
    }


    @Override
    public void onQuineMcluskey(String funcionStr, String noniString) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof MainFragment) {
            hideKeyboard();
            //Parsear funcion y NO/NI
            if (!parsearFuncion(funcionStr, noniString)) {
                ((MainFragment) fragment).showError("Error en la sintaxis de la funcion");
                return;
            }

            //Ejecutar algoritmo para minterms y Maxterms
            algoritmo(minTerms, no_ni, true);
            algoritmo(maxTerms, no_ni, false);

            Log.d("RESULT", "Primeros Implicantes m: "+Utils.printArrayListImplicante(primerosImplicantesTotalesMinterm));
            Log.d("RESULT", "Primeros Implicantes M: "+Utils.printArrayListImplicante(primerosImplicantesTotalesMaxterm));
            Log.d("RESULT", "Funcion m: "+Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMinterm, true));
            Log.d("RESULT", "Funcion M: "+Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMaxterm, false));
            Log.d("RESULT", "Puertas m: "+String.valueOf(Utils.contarPuertas(Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMinterm, true), true)));
            Log.d("RESULT", "Puertas M: "+String.valueOf(Utils.contarPuertas(Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMaxterm, false), false)));

            //Mostrar resultados en pantalla
            mostrarResultados();
            ((MainFragment) fragment).showResultados();
        }
    }

    private boolean parsearFuncion(String funcionStr, String noniString) {
        //Resetea las variables
        resetVariables();

        //Obtener el numero de varibles (numero de bits)
        numVariables = Utils.sacarVariables(funcionStr);
        if (numVariables < 1) return false;

        //Obtener lista de terminos NO/NI
        if (!parsearNoNi(noniString)) return false;

        //Obtener la expresion de la funcion
        String[] ladosIgualdad = funcionStr.split(" ?= ?");
        String funcion = ladosIgualdad[1];

        Log.e("FUNC", funcion);

        if (!(funcion.isEmpty())) {
            ArrayList<Integer> min = new ArrayList<>();
            ArrayList<Integer> max = new ArrayList<>();

            //Si la funcion es una lista de terminos
            if (funcion.matches(Utils.PATTERN_LISTA_TERMINOS)) {
                //Separa los terminos
                funcion = funcion.replace("(", "");
                funcion = funcion.replace(")", "");
                String[] terminosStr = funcion.split("(, ?)");

                //Para cada termino
                for (int i = 0; i < terminosStr.length; i++) {
                    //Esta dentro del rango de bits?
                    if (Math.pow(2, numVariables) <= Integer.valueOf(terminosStr[i])) {
                        Toast.makeText(this, "Error: Valores de terminos fuera del rango del la función", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    //Pertenece a la lista de NO/NI? Si no, es minterm
                    if (!Utils.hasInt(no_ni, Integer.valueOf(terminosStr[i])))
                        min.add(Integer.valueOf(terminosStr[i]));
                }

                //El resto de terminos son maxterm
                max.addAll(Utils.obtenerRestoDeTerminos(numVariables, min, no_ni));
            } else { //Si la funcion es una expresion
                boolean[] valores;
                //Para cada termino, evaluar la funcion si no es NO/NI
                for (int i = 0; i < Math.pow(2, numVariables); i++)
                    if (!Utils.hasInt(no_ni, i)) {
                        //Obtener valores de A,B,C..
                        valores = UtilsBinarios.intToBinaryBoolean(i, numVariables);
                        boolean evaluadaFuncion = false;
                        //Evaluar funcion
                        try {
                            evaluadaFuncion = Utils.evaluarFuncion(funcion, valores);
                        } catch (Exception e) { //Si hay un error en la sintaxis se lanza una exception
                            e.printStackTrace();
                            return false;
                        }
                        Log.d("FUNC", Utils.printboolean(valores) + " -- " + evaluadaFuncion);
                        //Si la funcion es true, es minterm. Si no, es maxterm
                        if (evaluadaFuncion) min.add(i);
                        else max.add(i);
                    }
            }

            //Convertir resultado
            minTerms = Utils.convertIntegers(min);
            maxTerms = Utils.convertIntegers(max);
            Log.d("FUNC", "m "+Utils.printintegers(minTerms));
            Log.d("FUNC", "M "+Utils.printintegers(maxTerms));
            return true;
        }
        return false;
    }

    private boolean parsearNoNi(String noniString) {
        if (!noniString.isEmpty()) {
            //Obtener lista de terminos
            noniString = noniString.replace("(","");
            noniString = noniString.replace(")","");
            String[] terminosStr = noniString.split("(, ?)");
            ArrayList<Integer> noni = new ArrayList<>();

            //Para cada termino
            for (int i = 0; i < terminosStr.length; i++) {
                //Esta dentro del rango de bits?
                if (Math.pow(2, numVariables) <= Integer.valueOf(terminosStr[i])) {
                    Toast.makeText(this, "Error: Valores de NO/NI fuera del rango del la función", Toast.LENGTH_LONG).show();
                    return false;
                }
                //Añadir a la lista
                noni.add(Integer.valueOf(terminosStr[i]));
            }
            //Convertir resultado
            no_ni = Utils.convertIntegers(noni);
        } else {
            //Si no se introdujo texto: lista vacia
            no_ni = new int[]{};
            Log.i("ParsearNoNi", "NoNi empty");
        }

        Log.d("FUNC", "n "+Utils.printintegers(no_ni));
        return true;
    }

    public void algoritmo(int[] terms, int[] noni, boolean isMinterm) {
        //Variables con los resultados de cada fase del algoritmo
        ArrayList<ArrayList<Implicante>> listaIteraciones = new ArrayList<>();
        boolean[][] tablaMarcas = null;
        ArrayList<Implicante> primerosImplicantes = new ArrayList<>();
        ArrayList<Implicante> primerosImplicantesTotales = null;

        //Iteracion 0: iniciar
        //Se crean los implicantes a partir de los terminos y se ponen en la lista 0
        ArrayList<Implicante> list = UtilsLista.termsToList(terms, numVariables);
        if (noni != null)
            list.addAll(UtilsLista.termsToList(noni, numVariables));
        listaIteraciones.add(list);

        //Iteracion 1: ordenar
        //Se ordenan los implicantes de la lista anterior
        listaIteraciones.add(UtilsLista.ordenarIteracion(listaIteraciones.get(0), numVariables));

        //Emparejar iteracion 1 y sucesivas
        while (true) {
            //Se obtiene la iteracion siguiente emparejando los implicantes de la ultima iteracion
            ArrayList<Implicante> nuevo = UtilsLista.
                    emparejarIteracion(getUltimaIteracion(listaIteraciones), numVariables);
            //Se añaden los implicantes sin marcar a la lista de primeros implicantes
            primerosImplicantes.addAll(UtilsLista.
                    buscarPrimerosImplicantes(getUltimaIteracion(listaIteraciones)));
            //Si no se ha producido ninguna pareja nueva se sale del bucle
            if (nuevo.isEmpty()) break;
            //Si se han producido parejas nuevas se añade la iteracion a la lista de las anteriores
            listaIteraciones.add(nuevo);
        }

        //Obtener marcas de la tabla
        //Filas: primeros implicantes. Columnas: terminos
        tablaMarcas = UtilsTabla.obtenerMarcasTabla(primerosImplicantes, terms);

        //Obtener los esenciales de los primeros implicantes
        //Implicantes que incluyan terminos que no incluye ningun otro implicante
        ArrayList<Implicante> primerosImplicantesEsenciales =
                UtilsTabla.getPrimerosImplicantesEsenciales(primerosImplicantes, tablaMarcas);

        //Completar la lista con los implicantes necesarios para cubrir todos los terminos
        primerosImplicantesTotales = UtilsTabla.completarImplicantesParaTerminos(primerosImplicantes,
                primerosImplicantesEsenciales,
                terms,
                isMinterm);

        //Asociacion de los resultados de cada fase a las variables globales
        if(isMinterm) {
            listaIteracionesMinterm = listaIteraciones;
            tablaMarcasMinterm = tablaMarcas;
            primerosImplicantesMinterm = primerosImplicantes;
            primerosImplicantesTotalesMinterm = primerosImplicantesTotales;
        } else {
            listaIteracionesMaxterm = listaIteraciones;
            tablaMarcasMaxterm = tablaMarcas;
            primerosImplicantesMaxterm = primerosImplicantes;
            primerosImplicantesTotalesMaxterm = primerosImplicantesTotales;

        }
    }

    private void mostrarResultados() {
        String implicantes;
        String funcionSimple;
        int puertas;

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        //Lista de implicantes
        implicantes = Utils.printArrayListImplicante(primerosImplicantesTotalesMinterm);
        //Funcion simplificada
        funcionSimple = Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMinterm, true);
        //Numero de puertas
        puertas = Utils.contarPuertas(funcionSimple, true);
        //Escribir resultados en pantalla
        mainFragment.setMintermResults(implicantes, funcionSimple, puertas);

        implicantes = Utils.printArrayListImplicante(primerosImplicantesTotalesMaxterm);
        funcionSimple = Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMaxterm, false);
        puertas = Utils.contarPuertas(funcionSimple, false);
        mainFragment.setMaxtermResults(implicantes, funcionSimple, puertas);
    }

    @Override
    public void onDetallesMinterm() {
        DetailFragment newFragment = new DetailFragment();
        newFragment.setMinterm(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }

    @Override
    public void onDetallesMaxterm() {
        DetailFragment newFragment = new DetailFragment();
        newFragment.setMinterm(false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }

    @Override
    public void setEditTextFuncion(String string) {
        etFuncion = string;
    }

    @Override
    public void setEditTextNoni(String string) {
        etNoni = string;
    }

    private ArrayList<Implicante> getUltimaIteracion(ArrayList<ArrayList<Implicante>> listaIteraciones) {
        return listaIteraciones.get(listaIteraciones.size() - 1);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof MainFragment)
            getSupportFragmentManager().putFragment(outState, BUNDLE_FRAGMENT_KEY, fragment);
        else {
            outState.putString(BUNDLE_ET_FUNCION_KEY, etFuncion);
            outState.putString(BUNDLE_ET_NONI_KEY, etNoni);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MainFragment mainFragment = new MainFragment();
                Bundle b = new Bundle();
                b.putString(BUNDLE_ET_FUNCION_KEY, etFuncion);
                b.putString(BUNDLE_ET_NONI_KEY, etNoni);
                mainFragment.setArguments(b);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainFragment).commitNow();
                if (primerosImplicantesTotalesMinterm != null && primerosImplicantesTotalesMaxterm != null)
                    mostrarResultados();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (fragment instanceof DetailFragment) {
            MainFragment mainFragment = new MainFragment();
            Bundle b = new Bundle();
            b.putString(BUNDLE_ET_FUNCION_KEY, etFuncion);
            b.putString(BUNDLE_ET_NONI_KEY, etNoni);
            mainFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mainFragment).commitNow();
            if (primerosImplicantesTotalesMinterm != null && primerosImplicantesTotalesMaxterm != null)
                mostrarResultados();
        } else
            super.onBackPressed();

    }

}
