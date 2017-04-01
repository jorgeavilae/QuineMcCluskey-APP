package com.usal.jorgeav.baseproject;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.usal.jorgeav.baseproject.model.Implicante;
import com.usal.jorgeav.baseproject.utils.Utils;
import com.usal.jorgeav.baseproject.utils.UtilsTabla;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {
    private static String cellIdFormat = "%02d%02d";

    @BindView(R.id.rv_lista_simplificaciones)
    RecyclerView recyclerViewListaSimplificaciones;
    @BindView(R.id.table)
    TableLayout tableLayout;
    @BindView(R.id.detail_resultado)
    TextView textViewImplicantes;
    @BindView(R.id.detail_funcion)
    TextView textViewFuncion;

    ListaIteracionesAdapter listaIteracionesAdapter;
    boolean isMinterm;
    MainActivity mainActivity;

    int[] terms;
    ArrayList<ArrayList<Implicante>> listaIteraciones;
    boolean[][] tablaMarcas;
    ArrayList<Implicante> primerosImplicantes;
    ArrayList<Implicante> primerosImplicantesTotales;

    public DetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listaIteracionesAdapter = new ListaIteracionesAdapter(getActivity());
        recyclerViewListaSimplificaciones.setAdapter(listaIteracionesAdapter);
        recyclerViewListaSimplificaciones.setLayoutManager(linearLayoutManager);

        if (isMinterm) {
            terms = mainActivity.minTerms;
            listaIteraciones = mainActivity.listaIteracionesMinterm;
            tablaMarcas = mainActivity.tablaMarcasMinterm;
            primerosImplicantes = mainActivity.primerosImplicantesMinterm;
            primerosImplicantesTotales = mainActivity.primerosImplicantesTotalesMinterm;
        } else {
            terms = mainActivity.maxTerms;
            listaIteraciones = mainActivity.listaIteracionesMaxterm;
            tablaMarcas = mainActivity.tablaMarcasMaxterm;
            primerosImplicantes = mainActivity.primerosImplicantesMaxterm;
            primerosImplicantesTotales = mainActivity.primerosImplicantesTotalesMaxterm;
        }

        //Actualizar lista
        listaIteracionesAdapter.setDataset(listaIteraciones);

        //Actualizar tabla
        tableLayout.setGravity(Gravity.CENTER);
        dibujarTabla(primerosImplicantes, terms);
        pintarImplicantesEsenciales(tablaMarcas);

        //Actualizar TextViews
        textViewImplicantes.setText(Utils.printArrayListImplicante(primerosImplicantesTotales));
        textViewFuncion.setText(String.format("F = %s", Utils.escribirFuncionFromImplicantes(primerosImplicantesTotales, isMinterm)));

        return view;
    }

    private void dibujarTabla(ArrayList<Implicante> primerosImplicantes, int[] minTerms) {
        String marcaCelda;

        //Cabecera
        TableRow row = new TableRow(mainActivity);
        TextView tv = getNewTextView("", true, -1, -1);
        row.addView(tv);
        for (int i = 0; i < minTerms.length; i++) {
            tv = getNewTextView(String.format(Locale.ENGLISH,"\t%2d \t",minTerms[i]), true, -1, -1);
            row.addView(tv);
        }
        tableLayout.addView(row);

        //Filas
        for (Implicante implicante : primerosImplicantes) {
            row = new TableRow(mainActivity);
            String fila = String.format(Locale.ENGLISH,"\t%s \t",implicante.terminosToString());
            tv = getNewTextView(fila, true, -1, -1);
            row.addView(tv);
            for (int i = 0; i < minTerms.length; i++) {
                if (tablaMarcas[primerosImplicantes.indexOf(implicante)][i]) marcaCelda = "X";
                else marcaCelda = "";

                tv = getNewTextView(marcaCelda, false, primerosImplicantes.indexOf(implicante), i);
                row.addView(tv);
            }
            tableLayout.addView(row);
        }

        tableLayout.setColumnStretchable(0, false);
    }

    private TextView getNewTextView(String text, boolean isHeader, int x, int y) {
        TextView tv = new TextView(mainActivity);
        tv.setText(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            tv.setTextAppearance(android.R.style.TextAppearance_Material_Subhead);
        else
            tv.setTextAppearance(mainActivity, android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Subtitle);
        if (!isHeader)
            tv.setTypeface(Typeface.MONOSPACE);
        if (x != -1 && y != -1)
            tv.setId(Integer.valueOf(String.format(Locale.ENGLISH, cellIdFormat, x, y)));
        tv.setGravity(Gravity.CENTER);
        tv.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.cell_shape));
        return tv;
    }

    private void pintarImplicantesEsenciales (boolean[][] tablaMarcas) {
        if (tablaMarcas.length > 0) {
            for (int i = 0; i < tablaMarcas[0].length; i++) {
                int index = UtilsTabla.indexOfUnicaMarca(tablaMarcas, i);
                if (index != -1) {
                    TextView tv = (TextView) tableLayout.findViewById(Integer.valueOf(String.format(Locale.ENGLISH, cellIdFormat, index, i)));
                    tv.setTextColor(ContextCompat.getColor(mainActivity, R.color.colorAccent));
                }
            }
        }
    }

    public void setMinterm(boolean minterm) {
        isMinterm = minterm;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must created by MainActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
}
