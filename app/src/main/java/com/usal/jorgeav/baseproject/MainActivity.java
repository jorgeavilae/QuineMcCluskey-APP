package com.usal.jorgeav.baseproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_num_variables)
    EditText numVariablesEsitText;
    @BindView(R.id.et_minterms)
    EditText mintermEditText;
    @BindView(R.id.rv_lista_simplificaciones)
    RecyclerView recyclerViewListaSimplificaciones;
    @BindView(R.id.button)
    Button button;

    ArrayList<ArrayList<Implicante>> listaIteraciones;

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
    private void testingListaIteraciones(ArrayList<ArrayList<Implicante>> listaIteraciones) {
        ArrayList<Integer> terminos = new ArrayList<>();
        Random rand = new Random();
        terminos.add(rand.nextInt(32)); terminos.add(rand.nextInt(32));
        terminos.add(rand.nextInt(32)); terminos.add(rand.nextInt(32));
        for (int i = 0; i < 6; i++) {
            ArrayList<Implicante> list = new ArrayList<>();
            Log.e("ASD", ""+terminos.size());
            int n = rand.nextInt(10)+1;
            for (int j = 0; j < n; j++) {
                list.add(new Implicante(terminos));
            }
            listaIteraciones.add(list);
        }
    }
}
