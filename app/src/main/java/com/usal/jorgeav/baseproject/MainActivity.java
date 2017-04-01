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

import com.usal.jorgeav.baseproject.model.Implicante;
import com.usal.jorgeav.baseproject.utils.Utils;
import com.usal.jorgeav.baseproject.utils.UtilsBinarios;
import com.usal.jorgeav.baseproject.utils.UtilsLista;
import com.usal.jorgeav.baseproject.utils.UtilsTabla;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {
    public static final String BUNDLE_ET_FUNCION_KEY = "BUNDLE_ET_FUNCION_KEY";
    public static final String BUNDLE_ET_NONI_KEY = "BUNDLE_ET_NONI_KEY";
    private static final String BUNDLE_FRAGMENT_KEY = "BUNDLE_FRAGMENT_KEY";
    int numVariables = 0;
    int minTerms[];
    int maxTerms[];
    int no_ni[];

    String etFuncion;
    String etNoni;

    ArrayList<ArrayList<Implicante>> listaIteracionesMinterm;
    boolean[][] tablaMarcasMinterm;
    ArrayList<Implicante> primerosImplicantesMinterm;
    ArrayList<Implicante> primerosImplicantesTotalesMinterm;

    ArrayList<ArrayList<Implicante>> listaIteracionesMaxterm;
    boolean[][] tablaMarcasMaxterm;
    ArrayList<Implicante> primerosImplicantesMaxterm;
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

    private boolean parsearFuncion(String funcionStr, String noniString) {
        resetVariables();

        numVariables = Utils.sacarVariables(funcionStr);
        if (numVariables < 1) return false;
        parsearNoNi(noniString);

        String[] ladosIgualdad = funcionStr.split(" ?= ?");
        String funcion = ladosIgualdad[1];

        Log.e("FUNC", funcion);

        if (!(funcion.isEmpty())) {
            ArrayList<Integer> min = new ArrayList<>();
            ArrayList<Integer> max = new ArrayList<>();

            if (funcion.matches(Utils.PATTERN_LISTA_TERMINOS)) {
                String[] terminosStr = funcion.split("(, ?)");
                for (int i = 0; i < terminosStr.length; i++)
                    min.add(Integer.valueOf(terminosStr[i]));
                max.addAll(Utils.obtenerRestoDeTerminos(numVariables, min, no_ni));
            } else {
                boolean[] valores;
                for (int i = 0; i < Math.pow(2, numVariables); i++)
                    if (!Utils.hasInt(no_ni, i)) {
                        valores = UtilsBinarios.intToBinaryBoolean(i, numVariables);
                        boolean evaluadaFuncion = false;
                        try {
                            evaluadaFuncion = Utils.evaluarFuncion(funcion, valores);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                        Log.d("FUNC", Utils.printboolean(valores) + " -- " + evaluadaFuncion);
                        if (evaluadaFuncion) min.add(i);
                        else max.add(i);
                    }
            }

            minTerms = Utils.convertIntegers(min);
            maxTerms = Utils.convertIntegers(max);
            Log.d("FUNCm", Utils.printintegers(minTerms));
            Log.d("FUNCM", Utils.printintegers(maxTerms));
            return true;
        }
        return false;


//        numVariables = 2;
//        parsearNoNi("");
//        minTerms = new int[]{0,1,2,3};
//        maxTerms = new int[]{};
    }

    private void parsearNoNi(String noniString) {
        if (!noniString.isEmpty()) {
            String[] terminosStr = noniString.split("(, ?)");
            ArrayList<Integer> noni = new ArrayList<>();
            for (int i = 0; i < terminosStr.length; i++) {
                noni.add(Integer.valueOf(terminosStr[i]));
            }
            no_ni = Utils.convertIntegers(noni);
        } else {
            no_ni = new int[]{};
            Log.e("ParsearNoNi", "NoNi empty");
        }
    }

    @Override
    public void onQuineMcluskey(String funcionStr, String noniString) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof MainFragment) {
            //Parsear funcion y NO/NI
            hideKeyboard();
            if (!parsearFuncion(funcionStr, noniString)) {
                ((MainFragment) fragment).showError("Error en la sintaxis de la funcion");
                return;
            }

            algoritmo(minTerms, no_ni, true);
            algoritmo(maxTerms, no_ni, false);

            Log.d("FUNCm", Utils.printArrayListImplicante(primerosImplicantesTotalesMinterm));
            Log.d("FUNCM", Utils.printArrayListImplicante(primerosImplicantesTotalesMaxterm));
            Log.d("RESULT m", Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMinterm, true));
            Log.d("RESULT M", Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMaxterm, false));
            Log.d("RESULT m", String.valueOf(Utils.contarPuertas(Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMinterm, true))));
            Log.d("RESULT M", String.valueOf(Utils.contarPuertas(Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMaxterm, false))));
            mostrarResultados();
            ((MainFragment) fragment).showResultados();
        }
    }

    private void mostrarResultados() {
        String implicantesMinterm = Utils.printArrayListImplicante(primerosImplicantesTotalesMinterm);
        String implicantesMaxterm = Utils.printArrayListImplicante(primerosImplicantesTotalesMaxterm);
        String funcionSimple = "";
        int puertas = 0;

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        funcionSimple = Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMinterm, true);
        puertas = Utils.contarPuertas(funcionSimple);
        mainFragment.setMintermResults(implicantesMinterm, funcionSimple, puertas);
        funcionSimple = Utils.escribirFuncionFromImplicantes(primerosImplicantesTotalesMaxterm, false);
        puertas = Utils.contarPuertas(funcionSimple);
        mainFragment.setMaxtermResults(implicantesMaxterm, funcionSimple, puertas);
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

    public void algoritmo(int[] terms, int[] noni, boolean isMinterm) {
        ArrayList<ArrayList<Implicante>> listaIteraciones = new ArrayList<>();
        boolean[][] tablaMarcas = null;
        ArrayList<Implicante> primerosImplicantes = new ArrayList<>();
        ArrayList<Implicante> primerosImplicantesTotales = null;

        //Iniciar iteracion 0
        ArrayList<Implicante> list = UtilsLista.termsToList(terms, numVariables);
        list.addAll(UtilsLista.termsToList(noni, numVariables));
        listaIteraciones.add(list);

        //Ordenar iteracion 1
        listaIteraciones.add(UtilsLista.ordenarIteracion(listaIteraciones.get(0), numVariables));

        //Emparejar iteracion 1 y sucesivas
        while (true) {
            ArrayList<Implicante> nuevo = UtilsLista.emparejarIteracion(getUltimaIteracion(listaIteraciones), numVariables);
            primerosImplicantes.addAll(UtilsLista.buscarPrimerosImplicantes(getUltimaIteracion(listaIteraciones)));
            if (nuevo.isEmpty()) break;
            listaIteraciones.add(nuevo);
        }

        //Obtener marcas de la tabla
        tablaMarcas = UtilsTabla.obtenerMarcasTabla(primerosImplicantes, terms);


        //Obtener los esenciales de los primeros implicantes
        ArrayList<Implicante> primerosImplicantesEsenciales =
                UtilsTabla.getPrimerosImplicantesEstenciales(primerosImplicantes, tablaMarcas);

        //Completar la lista con los implicantes necesarios para los todos los terminos
        primerosImplicantesTotales = UtilsTabla.completarImplicantesParaTerminos(primerosImplicantes,
                primerosImplicantesEsenciales,
                terms,
                isMinterm);

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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mainFragment).commitNow();
            if (primerosImplicantesTotalesMinterm != null && primerosImplicantesTotalesMaxterm != null)
                mostrarResultados();
        } else
            super.onBackPressed();

    }

}
