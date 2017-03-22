package com.usal.jorgeav.baseproject;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usal.jorgeav.baseproject.model.Implicante;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jorge Avila on 22/03/2017.
 */

public class ListaIteracionesAdapter  extends RecyclerView.Adapter<ListaIteracionesAdapter.ListaIteracionesViewHolder> {

    Context mContext;
    ArrayList<ArrayList<Implicante>> dataset;

    public ListaIteracionesAdapter(Context context) {
        this.mContext = context;
        this.dataset = null;
    }

    public void setDataset(ArrayList<ArrayList<Implicante>> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public ListaIteracionesAdapter.ListaIteracionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_lista_simplificaciones;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new ListaIteracionesAdapter.ListaIteracionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListaIteracionesAdapter.ListaIteracionesViewHolder holder, int position) {
        holder.setDataset(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        if (dataset != null)
            return dataset.size();
        else
            return 0;
    }

    public class ListaIteracionesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_sublista_iteracion)
        RecyclerView sublista;

        ListaSimplificacionesAdapter listaSimplificacionesAdapter;

        public ListaIteracionesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            listaSimplificacionesAdapter = new ListaSimplificacionesAdapter(mContext);
            sublista.setAdapter(listaSimplificacionesAdapter);
            sublista.setLayoutManager(linearLayoutManager);
        }

        public void setDataset(ArrayList<Implicante> dataset) {
            this.listaSimplificacionesAdapter.setDataset(dataset);
        }
    }
}
