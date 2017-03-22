package com.usal.jorgeav.baseproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_num_variables)
    EditText numVariablesEsitText;
    @BindView(R.id.et_minterms)
    EditText mintermEditText;
    @BindView(R.id.rv_lista_simplificaciones)
    RecyclerView recyclerViewListaSimplificaciones;

    ArrayList<ArrayList<String>> listaIteraciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        ListaIteracionesAdapter listaIteracionesAdapter = new ListaIteracionesAdapter(this);
        listaIteraciones = new ArrayList<>();
        testingListaIteraciones(listaIteraciones);
        listaIteracionesAdapter.setDataset(listaIteraciones);
        recyclerViewListaSimplificaciones.setAdapter(listaIteracionesAdapter);
        recyclerViewListaSimplificaciones.setLayoutManager(linearLayoutManager);

    }

    //TODO testing purpose. Delete
    private void testingListaIteraciones(ArrayList<ArrayList<String>> listaIteraciones) {
        for (int i = 0; i < 20; i++) {
            listaIteraciones.add(Utils.getAlfabeto());
        }
    }
}
