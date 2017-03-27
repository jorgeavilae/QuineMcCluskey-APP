package com.usal.jorgeav.baseproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.usal.jorgeav.baseproject.model.Implicante;
import com.usal.jorgeav.baseproject.utils.Utils;
import com.usal.jorgeav.baseproject.utils.UtilsLista;
import com.usal.jorgeav.baseproject.utils.UtilsTabla;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    public static int numVariables = 0;
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
    }

    @Override
    protected void onStart() {
        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mainFragment).commitNow();
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

    @Override
    public void onQuineMcluskey(String funcion, String noniString) {
        // TODO: 27/03/2017 parsear datos introducidos
        // TODO: 27/03/2017 guardar edittext string en bundle state
        //Parsear funcion y NO/NI
        numVariables = 5;
        minTerms = new int[]{3,8,9,10,11,12,14,19,24,27};
        maxTerms = new int[]{0,1,2,4,5,6,7,15,16,17,18,20,21,22,23,25,28,29,30,31};
        no_ni = new int[]{13,26};

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
        ArrayList<Implicante> list = UtilsLista.termsToList(terms);
        list.addAll(UtilsLista.termsToList(noni));
        listaIteraciones.add(list);

        //Ordenar iteracion 1
        listaIteraciones.add(UtilsLista.ordenarIteracion(listaIteraciones.get(0), numVariables));

        //Emparejar iteracion 1 y sucesivas
        while (true) {
            ArrayList<Implicante> nuevo = UtilsLista.emparejarIteracion(getUltimaIteracion(listaIteraciones));
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
