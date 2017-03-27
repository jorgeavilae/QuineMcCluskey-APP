package com.usal.jorgeav.baseproject;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.usal.jorgeav.baseproject.model.Implicante;
import com.usal.jorgeav.baseproject.utils.UtilsLista;
import com.usal.jorgeav.baseproject.utils.UtilsTabla;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static int numVariables = 0;

    @BindView(R.id.et_funcion)
    EditText etFuncion;
    @BindView(R.id.et_noni)
    EditText etNoNi;
    @BindView(R.id.constraint_resultados)
    ConstraintLayout constraintResultados;

    @BindView(R.id.tv_sumatorio)
    TextView tvSumatorio;
    @BindView(R.id.tv_funcion_minterm)
    TextView tvFuncionMinterm;
    @BindView(R.id.tv_puertas_minterm)
    TextView tvPuertasMinterm;

    @BindView(R.id.tv_producto)
    TextView tvProducto;
    @BindView(R.id.tv_funcion_maxterm)
    TextView tvFuncionMaxterm;
    @BindView(R.id.tv_puertas_maxterm)
    TextView tvPuertasMaxterm;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_error)
    TextView tvError;

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
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        listaIteracionesAdapter = new ListaIteracionesAdapter(this);
//        listaIteraciones = new ArrayList<>();
//        recyclerViewListaSimplificaciones.setAdapter(listaIteracionesAdapter);
//        recyclerViewListaSimplificaciones.setLayoutManager(linearLayoutManager);
//
//        primerosImplicantes = new ArrayList<>();
//
//        tableLayout.setGravity(Gravity.CENTER);

    }

    public void quineMccluskey(View view) {
        showResultados();
    }

    public void detallesMinterm(View view) {
        showError("Err");
    }

    public void detallesMaxterm(View view) {
        showProgressBar();
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

    private void showError(String error) {
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(error);
        progressBar.setVisibility(View.INVISIBLE);
        constraintResultados.setVisibility(View.INVISIBLE);

    }

    private void showProgressBar() {
        tvError.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        constraintResultados.setVisibility(View.INVISIBLE);

    }

    private void showResultados() {
        tvError.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        constraintResultados.setVisibility(View.VISIBLE);

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
