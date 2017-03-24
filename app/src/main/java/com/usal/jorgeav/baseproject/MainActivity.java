package com.usal.jorgeav.baseproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.usal.jorgeav.baseproject.model.Binario;
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        listaIteracionesAdapter = new ListaIteracionesAdapter(this);
        listaIteraciones = new ArrayList<>();
        recyclerViewListaSimplificaciones.setAdapter(listaIteracionesAdapter);
        recyclerViewListaSimplificaciones.setLayoutManager(linearLayoutManager);

        primerosImplicantes = new ArrayList<>();

    }

    public void onClick(View view) {
        //Sacar minterms y numVariables
        numVariables = 4;

        //Iniciar iteracion 0
        ArrayList<Implicante> list = new ArrayList<>();
//        for (int i = 0; i < 16; i++) {
//            ArrayList<Integer> terminos = new ArrayList<>();
//            terminos.add(i);
//            list.add(new Implicante(terminos, null, 0));
//        }
        testingMintermsInciales(list);
        listaIteraciones.add(list);

        //Ordenar iteracion 1
        ArrayList<Implicante> ordenado = new ArrayList<>();
        for (int i = 0; i <= numVariables; i++)
            for (Implicante implicante : list)
                if (implicante.subseccion == i) {
                    ordenado.add(new Implicante(implicante.getTerminos(), null, implicante.iteracion + 1));
                    implicante.setMarca(true);
                }
        listaIteraciones.add(ordenado);

        //Emparejar iteracion 1 y sucesivas
        while (true) {
            ArrayList<Implicante> nuevo = emparejarIteracion(listaIteraciones.get(listaIteraciones.size()-1));
            if (!nuevo.isEmpty())
                listaIteraciones.add(nuevo);
            else
                break;
        }

        //Terminar cuando la iteracion no cree nuevas parejas y añadir como primeros implicantes
        for (Implicante implicante : listaIteraciones.get(listaIteraciones.size()-1))
            if (!implicante.isMarca())
                primerosImplicantes.add(implicante);

        listaIteracionesAdapter.setDataset(listaIteraciones);
    }

    private ArrayList<Implicante> emparejarIteracion(ArrayList<Implicante> implicantes) {
        ArrayList<Implicante> result = new ArrayList<>();

        for (Implicante implicante : implicantes) {
            for (Implicante comparado : implicantes) {
                if (implicante.subseccion+1 == comparado.subseccion &&
                        difierenUnDigito(implicante, comparado)) {
                    ArrayList<Integer> terminos = new ArrayList<>();
                    terminos.addAll(implicante.getTerminos());
                    terminos.addAll(comparado.getTerminos());
                    ArrayList<Binario> binarios = Utils.compareBinarios(implicante.getBinarios(), comparado.getBinarios());
                    result.add(new Implicante(terminos, binarios, implicante.iteracion+1));

                    marcarSimplificados(implicantes, terminos);
                }
            }
        }

        for (Implicante implicante : implicantes)
            if (!implicante.isMarca())
                primerosImplicantes.add(implicante);

        return result;
    }

    private void marcarSimplificados(ArrayList<Implicante> implicantes, ArrayList<Integer> terminos) {
        for (Implicante i : implicantes)
            if (terminos.containsAll(i.getTerminos()))
                i.setMarca(true);
    }

    private boolean difierenUnDigito(Implicante implicante, Implicante comparado) {
        ArrayList<Binario> primero = implicante.getBinarios();
        ArrayList<Binario> segundo = comparado.getBinarios();
        int diferentes = 0;
        for (int i = 0; i < primero.size(); i++) {
            if (!(primero.get(i).getDigito() == segundo.get(i).getDigito()))
                diferentes++;
        }
        return (diferentes == 1);
    }

    //TODO testing purpose. Delete
    private void testingMintermsInciales(ArrayList<Implicante> list) {
        ArrayList<Integer> terminos = new ArrayList<>();
        terminos.add(0);
        list.add(new Implicante(terminos, null, 0));

        terminos = new ArrayList<>();
        terminos.add(2);
        list.add(new Implicante(terminos, null, 0));

        terminos = new ArrayList<>();
        terminos.add(3);
        list.add(new Implicante(terminos, null, 0));

        terminos = new ArrayList<>();
        terminos.add(5);
        list.add(new Implicante(terminos, null, 0));

        terminos = new ArrayList<>();
        terminos.add(7);
        list.add(new Implicante(terminos, null, 0));

        terminos = new ArrayList<>();
        terminos.add(8);
        list.add(new Implicante(terminos, null, 0));

        terminos = new ArrayList<>();
        terminos.add(10);
        list.add(new Implicante(terminos, null, 0));

        terminos = new ArrayList<>();
        terminos.add(11);
        list.add(new Implicante(terminos, null, 0));
    }
}
