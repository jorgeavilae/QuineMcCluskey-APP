package com.usal.jorgeav.baseproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.usal.jorgeav.baseproject.model.Implicante;

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

    ListaIteracionesAdapter listaIteracionesAdapter;
    ArrayList<ArrayList<Implicante>> listaIteraciones;

    ArrayList<Implicante> primerosImplicantes;

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

    }

    public void onClick(View view) {
        //Sacar minterms y numVariables
        numVariables = 5;
        int terms[] = {3,8,9,10,11,12,14,19,24,27,13,26};

        //Iniciar iteracion 0
        ArrayList<Implicante> list = Utils.termsToList(terms);
        listaIteraciones.add(list);

        //Ordenar iteracion 1
        listaIteraciones.add(Utils.ordenarIteracion(listaIteraciones.get(0), numVariables));

        //Emparejar iteracion 1 y sucesivas
        while (true) {
            ArrayList<Implicante> nuevo = Utils.emparejarIteracion(getUltimaIteracion(listaIteraciones));
            primerosImplicantes.addAll(Utils.buscarPrimerosImplicantes(getUltimaIteracion(listaIteraciones)));
            if (nuevo.isEmpty()) break;
            listaIteraciones.add(nuevo);
        }

        //Actualizar lista
        listaIteracionesAdapter.setDataset(listaIteraciones);
    }

    private ArrayList<Implicante> getUltimaIteracion(ArrayList<ArrayList<Implicante>> listaIteraciones) {
        return listaIteraciones.get(listaIteraciones.size() - 1);
    }

}
