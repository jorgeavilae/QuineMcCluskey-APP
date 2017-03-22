package com.usal.jorgeav.baseproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jorge Avila on 22/03/2017.
 */

public class ListaSimplificacionesAdapter extends RecyclerView.Adapter<ListaSimplificacionesAdapter.ListaSimplificacionesViewHolder> {

        Context mContext;
        ArrayList<Implicante> dataset;

    public ListaSimplificacionesAdapter(Context context) {
        this.mContext = context;
        this.dataset = null;
    }

    public void setDataset(ArrayList<Implicante> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public ListaSimplificacionesAdapter.ListaSimplificacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_lista_implicante;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new ListaSimplificacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListaSimplificacionesAdapter.ListaSimplificacionesViewHolder holder, int position) {
        Implicante implicante = dataset.get(position);
        holder.tv_terminos.setText(implicante.terminosToString());
        holder.tv_binario.setText(implicante.binariosToString());
        holder.cb_marca.setChecked(implicante.isMarca());
    }

    @Override
    public int getItemCount() {
        if (dataset != null)
            return dataset.size();
        else
            return 0;
    }

    public class ListaSimplificacionesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_decimal)
        TextView tv_terminos;
        @BindView(R.id.item_binario)
        TextView tv_binario;
        @BindView(R.id.item_marca)
        CheckBox cb_marca;

        public ListaSimplificacionesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
