package com.usal.jorgeav.baseproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TableLayout;

import butterknife.BindView;

public class DetailActivity extends AppCompatActivity {
    private static String cellIdFormat = "%02d%02d";

    @BindView(R.id.rv_lista_simplificaciones)
    RecyclerView recyclerViewListaSimplificaciones;
    @BindView(R.id.table)
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }


//    public void dibujarTabla(ArrayList<Implicante> primerosImplicantes, int[] minTerms) {
//        String marcaCelda;
//
//        //Cabecera
//        TableRow row = new TableRow(context);
//        TextView tv = getNewTextView(context, "", true, -1, -1);
//        row.addView(tv);
//        for (int i = 0; i < minTerms.length; i++) {
//            tv = getNewTextView(context, Integer.toString(minTerms[i]), true, -1, -1);
//            row.addView(tv);
//        }
//        tableLayout.addView(row);
//
//        //Filas
//        for (Implicante implicante : primerosImplicantes) {
//            row = new TableRow(context);
//            tv = getNewTextView(context, implicante.terminosToString(), true, -1, -1);
//            row.addView(tv);
//            for (int i = 0; i < minTerms.length; i++) {
//                if (implicante.getTerminos().contains(minTerms[i])) {
//                    tablaMarcas[primerosImplicantes.indexOf(implicante)][i] = true;
//                    marcaCelda = "X";
//                } else {
//                    tablaMarcas[primerosImplicantes.indexOf(implicante)][i] = false;
//                    marcaCelda = "";
//                }
//                tv = getNewTextView(context, marcaCelda, false, primerosImplicantes.indexOf(implicante), i);
//                row.addView(tv);
//            }
//            tableLayout.addView(row);
//        }
//
//        tableLayout.setColumnStretchable(0, false);
//        return tablaMarcas;
//    }
//
//    private TextView getNewTextView(Context context, String text, boolean isHeader, int x, int y) {
//        TextView tv = new TextView(context);
//        tv.setText(text);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            tv.setTextAppearance(android.R.style.TextAppearance_Material_Subhead);
//        else
//            tv.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Subtitle);
//        if (!isHeader)
//            tv.setTypeface(Typeface.MONOSPACE);
//        if (x != -1 && y != -1)
//            tv.setId(Integer.valueOf(String.format(Locale.ENGLISH, cellIdFormat, x, y)));
//        tv.setGravity(Gravity.CENTER);
//        tv.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_shape));
//        return tv;
//    }
//
//    void pintarImplicantesEsenciales() {
//        for (int i = 0; i < tablaMarcas[i].length; i++) {
//            int index = indexOfUnicaMarca(tablaMarcas, i);
//            if (index != -1) {
//                TextView tv = (TextView)tableLayout.findViewById(Integer.valueOf(String.format(Locale.ENGLISH, cellIdFormat, index, i)));
//                tv.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
//            }
//        }
//    }
}
