package com.muviteam.muviapp.view.fragment;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.muviteam.muviapp.R;
import com.muviteam.muviapp.controller.ControllerPelicula;
import com.muviteam.muviapp.model.Credits;
import com.muviteam.muviapp.model.Pelicula;
import com.muviteam.muviapp.utils.ResultListener;
import com.muviteam.muviapp.view.adapter.AdapterPelicula;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentViewPager extends Fragment implements AdapterPelicula.ListenerDelAdapter{

    public static final String CLAVE_PELICULA = "clavePelicula";

    private TextView titulo, director;
    private ImageView imagenFondo, imagenPoster;
    private View view;
    private Pelicula pelicula;
    private String direc;
    private ListenerDeFragment listenerDelFragment;
    private ProgressBar progressBarPoster;
    private ProgressBar progressBarFondo;



    public static FragmentViewPager dameUnFragment(Pelicula pelicula) {
        FragmentViewPager fragmentViewPager = new FragmentViewPager();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CLAVE_PELICULA, pelicula);
        fragmentViewPager.setArguments(bundle);
        return fragmentViewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        buscarVariables();
        Bundle bundle = getArguments();
        pelicula = (Pelicula) bundle.getSerializable(CLAVE_PELICULA);
        cargarPelicula();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerDelFragment.recibirPelicula(pelicula);
            }
        });
        return view;
    }

    public void buscarVariables() {
        titulo = view.findViewById(R.id.FragmentViewPager_TextView_NombrePelicula);
        imagenFondo = view.findViewById(R.id.FragmentViewPager_ImageView_FondoPelicula);
        imagenPoster = view.findViewById(R.id.FragmentViewPager_ImageView_PosterPelicula);
        director = view.findViewById(R.id.Fragment_TextView_Director);
        progressBarPoster = view.findViewById(R.id.celdaViewPager_ProgressBarPoster);
        progressBarFondo = view.findViewById(R.id.celdaViewPager_ProgressBarFondo);
    }

    public void cargarPelicula() {
        titulo.setText(pelicula.getTitulo());
        Glide.with(getContext())
                .load(pelicula.generaURLImagencelda())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBarFondo.setVisibility(GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBarFondo.setVisibility(GONE);
                        return false;
                    }
                })
                .error(R.drawable.logomuvi)
                .into(imagenFondo);
        Glide.with(getContext())
                .load(pelicula.generaURLImagen())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBarPoster.setVisibility(GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBarPoster.setVisibility(GONE);
                        return false;
                    }
                })
                .error(R.drawable.logomuvi)
                .into(imagenPoster);
        ControllerPelicula controllerPelicula = new ControllerPelicula();
        controllerPelicula.traerCredits(new ResultListener<Credits>() {
            @Override
            public void finish(Credits result) {
                if (result != null) {
                    direc = result.getCrew().get(0).getNombre();
                } else {
                    direc = "Unknown";
                }
                director.setText(direc);
            }
        }, pelicula.getId());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerDelFragment = (ListenerDeFragment) context;
    }

    public interface ListenerDeFragment {
        public void recibirPelicula(Pelicula pelicula);
    }

    @Override
    public void informarPelicula(Pelicula pelicula) {
        listenerDelFragment.recibirPelicula(pelicula);
    }

    @Override
    public void informarGenero(Integer genero) {

    }
}

