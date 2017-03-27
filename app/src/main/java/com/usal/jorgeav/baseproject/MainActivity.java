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
    private static final String BUNDLE_FRAGMENT_KEY = "BUNDLE_FRAGMENT_KEY";

    int numVariables = 0;
    int minTerms[];
    int maxTerms[];
    int no_ni[];

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
        if (savedInstanceState != null) {
            MainFragment mainFragment = (MainFragment)getSupportFragmentManager().getFragment(savedInstanceState, BUNDLE_FRAGMENT_KEY);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mainFragment).commitNow();
        }
    }

    @Override
    protected void onStart() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof MainFragment)) {
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mainFragment).commitNow();
        }
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

        super.onStart();
    }

    private void parsearFuncion(String funcionStr) {
        String[] terminosStr = funcionStr.split("(, ?)");
        ArrayList<Integer> min = new ArrayList<>();
        for (int i = 0; i < terminosStr.length; i++) {
            min.add(Integer.valueOf(terminosStr[i]));
        }

        minTerms = Utils.convertIntegers(min);
        maxTerms = new int[]{0,7,8,9,10,11,12,13,14,15};
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
            Log.e("ASDASD", "NoNi empty");
        }

        numVariables = 0;
        for (int i = 0; i < minTerms.length; i++) {
            int numDigitosBinarios = UtilsBinarios.cuantosDigitosBinario(minTerms[i]);
            if (numVariables < numDigitosBinarios) numVariables = numDigitosBinarios;
        }
        for (int i = 0; i < maxTerms.length; i++) {
            int numDigitosBinarios = UtilsBinarios.cuantosDigitosBinario(maxTerms[i]);
            if (numVariables < numDigitosBinarios) numVariables = numDigitosBinarios;
        }
        for (int i = 0; i < no_ni.length; i++) {
            int numDigitosBinarios = UtilsBinarios.cuantosDigitosBinario(no_ni[i]);
            if (numVariables < numDigitosBinarios) numVariables = numDigitosBinarios;
        }

        maxTerms = Utils.obtenerRestoDeTerminos(numVariables, minTerms, no_ni);
    }

    @Override
    public void onQuineMcluskey(String funcionStr, String noniString) {
        //Parsear funcion y NO/NI
        hideKeyboard();
        // TODO: 27/03/2017 parsear datos introducidos
        parsearFuncion(funcionStr);
        parsearNoNi(noniString);
        // TODO: 27/03/2017 guardar edittext string en bundle state

        algoritmo(minTerms, no_ni, true);
        algoritmo(maxTerms, no_ni, false);

        mostrarResultados();
    }

    //todo parsear resultados
    private void mostrarResultados() {
        String implicantesMinterm = Utils.printArrayListImplicante(primerosImplicantesTotalesMinterm);
        String implicantesMaxterm = Utils.printArrayListImplicante(primerosImplicantesTotalesMaxterm);
        String funcionSimple = "";
        int puertas = 0;

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        funcionSimple = "";
        puertas = 0;
        mainFragment.setMintermResults(implicantesMinterm, funcionSimple, puertas);
        funcionSimple = "";
        puertas = 0;
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

        //TODO if primerosimplicantes.size == 1 && binarios == --... => f=1 o f=0 / minterm o maxterm

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

    private String printboolean(boolean[][] array){
        String result = "";
        for (int i = 0; i < array[0].length; i++) {
            for (int j = 0; j < array.length; j++) {
                result = result + array[j][i] + " ";
            }
            result = result + "\n";
        }
        return result;
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
        //Save the fragment's instance

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof MainFragment) {
            getSupportFragmentManager().putFragment(outState, BUNDLE_FRAGMENT_KEY, fragment);
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
