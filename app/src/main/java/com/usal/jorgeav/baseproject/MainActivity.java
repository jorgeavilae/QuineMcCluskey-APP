package com.usal.jorgeav.baseproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import com.usal.jorgeav.baseproject.model.Implicante;
import com.usal.jorgeav.baseproject.utils.UtilsLista;
import com.usal.jorgeav.baseproject.utils.UtilsTabla;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static int numVariables = 0;

    @BindView(R.id.et_num_variables)
    EditText numVariablesEsitText;
    @BindView(R.id.et_minterms)
    EditText mintermEditText;
    @BindView(R.id.rv_lista_simplificaciones)
    RecyclerView recyclerViewListaSimplificaciones;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.table)
    TableLayout tableLayout;

    ListaIteracionesAdapter listaIteracionesAdapter;
    ArrayList<ArrayList<Implicante>> listaIteraciones;

    boolean[][] tablaMarcas;
    ArrayList<Implicante> primerosImplicantes;
    ArrayList<Implicante> primerosImplicantesTotales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listaIteracionesAdapter = new ListaIteracionesAdapter(this);
        listaIteraciones = new ArrayList<>();
        recyclerViewListaSimplificaciones.setAdapter(listaIteracionesAdapter);
        recyclerViewListaSimplificaciones.setLayoutManager(linearLayoutManager);

        primerosImplicantes = new ArrayList<>();

        tableLayout.setGravity(Gravity.CENTER);

    }

    public void onClick(View view) {
        //Sacar minterms y numVariables
        numVariables = 2;
        int minTerms[] = {0,1,2,3};
        int maxTerms[] = {};
        int no_ni[] = {};

        //Iniciar iteracion 0
        ArrayList<Implicante> list = UtilsLista.termsToList(minTerms);
        list.addAll(UtilsLista.termsToList(no_ni));
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

        //Actualizar lista
//        listaIteracionesAdapter.setDataset(listaIteraciones);

        //TODO if primerosimplicantes.size == 1 && binarios == --... => f=1 o f=0 / minterm o maxterm

        //Dibujar tabla
        tablaMarcas = UtilsTabla.obtenerMarcasTabla(primerosImplicantes, minTerms);

        //Obtener los esenciales de los primeros implicantes
        ArrayList<Implicante> primerosImplicantesEsenciales =
                UtilsTabla.getPrimerosImplicantesEstenciales(primerosImplicantes, tablaMarcas);

        //Completar la lista con los implicantes necesarios para los todos los terminos
        primerosImplicantesTotales = UtilsTabla.completarImplicantesParaTerminos(primerosImplicantes,
                primerosImplicantesEsenciales,
                minTerms,
                true);
        Log.d("ImplicantesTotales", printArrayListImplicante(primerosImplicantesTotales));

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

    private String printArrayListImplicante(ArrayList<Implicante> arrayList) {
        String result = "";
        for (Implicante impl : arrayList) {
            result = result + impl.terminosToString() + "\n";
        }
        return result;
    }

}
