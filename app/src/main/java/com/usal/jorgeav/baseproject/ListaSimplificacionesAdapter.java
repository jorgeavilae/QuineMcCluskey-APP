package com.usal.jorgeav.baseproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jorge Avila on 22/03/2017.
 */

public class ListaSimplificacionesAdapter extends RecyclerView.Adapter<ListaSimplificacionesAdapter.ListaSimplificacionesViewHolder> {

        Context mContext;
        ArrayList<String> dataset;

    public ListaSimplificacionesAdapter(Context context) {
        this.mContext = context;
        this.dataset = null;
    }

    public void setDataset(ArrayList<String> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public ListaSimplificacionesAdapter.ListaSimplificacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_lista_simplificaciones;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new ListaSimplificacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListaSimplificacionesAdapter.ListaSimplificacionesViewHolder holder, int position) {
        holder.sublista.setText(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        if (dataset != null)
            return dataset.size();
        else
            return 0;
    }

    public class ListaSimplificacionesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_sublista_iteracion)
        TextView sublista;

        public ListaSimplificacionesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
