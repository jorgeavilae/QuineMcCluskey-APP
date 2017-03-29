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
        System.out.println("funcion ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState != null) {
//            MainFragment mainFragment = (MainFragment)getSupportFragmentManager().getFragment(savedInstanceState, BUNDLE_FRAGMENT_KEY);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, mainFragment).commitNow();
//        }
    }

    @Override
    protected void onStart() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if (!(fragment instanceof MainFragment)) {
//            MainFragment mainFragment = new MainFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, mainFragment).commitNow();
//        }
//        numVariables = 0;
//        minTerms = null;
//        maxTerms = null;
//        no_ni = null;
//        listaIteracionesMinterm = null;
//        tablaMarcasMinterm = null;
//        primerosImplicantesMinterm = null;
//        primerosImplicantesTotalesMinterm = null;
//        listaIteracionesMaxterm = null;
//        tablaMarcasMaxterm = null;
//        primerosImplicantesMaxterm = null;
//        primerosImplicantesTotalesMaxterm = null;

        //todo borrar
        testCosas();
        super.onStart();
    }

    private void testCosas() {
        String s1 = "f(a,b,c)=ab+c";
        String s2 = "f(a,b,c,d)=a+b";
        String s3 = "f(a,b,c)=((ab)+(bc))";
        String s4 = "f(a,b,c,d)=(a+b+c)(d+a)";
        String s5 = "f(a,b,c,d)=ab+cd+ad";
        String s6 = "f(a,b,c,d)=ab(ab+cd + a(acb))ad";
        String s7 = "f(a,b,c,d)=a(ab+cd + a(acb))";
        String s8 = "f(a,b,c,d)=(a(b+cd) + a(acb))ad";
        String s9 = "f(a,b,c,d)=a(ab+cd + a(acb))+c";
        String s10 = "f(a,b,c,d)=a+(ab+cd + a(acb))a";
        String s11 = "f(a,b,c,d)=asd";
        String s12 = "f(a)=a";
        String s13 = "f (a, b, c, d)=asd";
        String s14 = "f(a,b,c,d) =asd";
        String s15 = "f(a,b,c,d) = asd";

        Log.d("RESULT",""+parsearFuncion(s3,""));
        //evaluarFuncion(s10);
    }

    private boolean parsearFuncion(String funcionStr, String noniString) {
        numVariables = Utils.sacarVariables(funcionStr);
        if (numVariables < 1) return false;
        parsearNoNi(noniString);

        String[] ladosIgualdad = funcionStr.split(" ?= ?");
        String funcion = ladosIgualdad[1];

        Log.e("FUNC", funcion);
        boolean[] valores;
        ArrayList<Integer> min = new ArrayList<>();
        ArrayList<Integer> max = new ArrayList<>();
        for (int i = 0; i < Math.pow(2, numVariables); i++)
            if (!Utils.hasInt(no_ni, i)) {
                valores = UtilsBinarios.intToBinaryBoolean(i, numVariables);
                boolean evaluadaFuncion = Utils.evaluarFuncion(funcion, valores);
                Log.d("FUNC",printboolean(valores)+" -- "+evaluadaFuncion);
                if (evaluadaFuncion) min.add(i);
                else max.add(i);
            }

        Log.d("FUNCm", Utils.printArrayListInteger(min));
        Log.d("FUNCM", Utils.printArrayListInteger(max));
        minTerms = Utils.convertIntegers(min);
        maxTerms = Utils.convertIntegers(max);
        return true;
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
            // TODO: 27/03/2017 guardar edittext string en bundle state

            algoritmo(minTerms, no_ni, true);
            algoritmo(maxTerms, no_ni, false);

            mostrarResultados();
            ((MainFragment) fragment).showResultados();
        }
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

    private String printboolean(boolean[] array){
        String result = "";
        for (int i = 0; i < array.length; i++) {
            result = result + (array[i]?"1":"0") + " ";
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
