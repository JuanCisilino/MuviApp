package com.muviteam.muviapp.view.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.muviteam.muviapp.R;
import com.muviteam.muviapp.model.Pelicula;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class AdapterFavorito  extends RecyclerView.Adapter<AdapterFavorito.ViewHolderPeliculas> {

    private List<Pelicula> favoritoList;
    private ListenerDelAdapter listenerDelAdapter;
    private View vistaDeLaCelda;
    private ProgressBar progressBar;

    public AdapterFavorito(List<Pelicula> favoritoList) {
        this.favoritoList = favoritoList;
    }

    public AdapterFavorito(ListenerDelAdapter listenerDelAdapter) {
        favoritoList = new ArrayList<>();
        this.listenerDelAdapter = listenerDelAdapter;
    }

    @NonNull
    @Override
    public AdapterFavorito.ViewHolderPeliculas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        vistaDeLaCelda = inflater.inflate(R.layout.celda_pelicula, parent, false);
        return new ViewHolderPeliculas(vistaDeLaCelda);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPeliculas holder, int position) {
        Pelicula peliculaMostrada = favoritoList.get(position);
        holder.cargarPelicula(peliculaMostrada);
    }

    @Override
    public int getItemCount() {
        return favoritoList.size(); }

    public void setFavoritoList(List<Pelicula> favoritoList) {
        this.favoritoList = favoritoList;
        notifyDataSetChanged();
    }

    public List<Pelicula> getFavoritoList() {
        return favoritoList;
    }

    public class ViewHolderPeliculas extends RecyclerView.ViewHolder {
        private TextView textViewTitulo;
        private TextView textViewPuntaje;
        private ImageView imageViewImagenDePelicula;
        private String puntaje;

        public ViewHolderPeliculas(@NonNull View itemView) {
            super(itemView);
            imageViewImagenDePelicula = itemView.findViewById(R.id.CeldaPelicula_ImageView_ImagenPelicula);
            textViewTitulo = itemView.findViewById(R.id.CeldaPelicula_TextView_NombrePelicula);
            textViewPuntaje = itemView.findViewById(R.id.CeldaPelicula_TextView_RatingPelicula);
            progressBar = itemView.findViewById(R.id.celdaPelicula_ProgressBar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pelicula peliculaSeleccionada = favoritoList.get(getAdapterPosition());
                    listenerDelAdapter.informarPelicula(peliculaSeleccionada);
                }
            });
        }

        public void cargarPelicula(Pelicula pelicula) {
            Glide.with(imageViewImagenDePelicula.getContext())
                    .load(pelicula.generaURLImagen())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(GONE);
                            return false;
                        }
                    })
                    .error(R.drawable.logomuvi).into(imageViewImagenDePelicula);
            textViewTitulo.setText(pelicula.getTitulo());
            puntaje = Float.toString(pelicula.getPopularity());
            textViewPuntaje.setText(puntaje);
        }
    }

    public interface ListenerDelAdapter {
        public void informarPelicula(Pelicula pelicula);
    }
}
