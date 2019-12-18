package com.muviteam.muviapp.view.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.muviteam.muviapp.R;
import com.muviteam.muviapp.controller.ControllerPelicula;
import com.muviteam.muviapp.model.Famoso;
import com.muviteam.muviapp.model.Pelicula;
import com.muviteam.muviapp.utils.ResultListener;
import com.muviteam.muviapp.view.adapter.AdapterFamoso;
import com.muviteam.muviapp.view.adapter.AdapterPelicula;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetalleFamoso extends Fragment implements AdapterFamoso.ListenerDelAdapter,
        AdapterPelicula.ListenerDelAdapter {

    public static final String CLAVE_FAMOSO = "claveFamoso";

    private TextView textViewNombreFamoso, textViewBioFamoso;
    private ImageView imagenFamoso;
    private AdapterFamoso adapterFamoso;
    private View view;
    private Famoso famosoSeleccionado;
    private RecyclerView contenedorPeliculas;
    private ControllerPelicula peliculaController;
    private ListenerDeFragment listenerDelFragment;
    private AdapterPelicula adapterPelicula;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detalle_famoso, container, false);
        encontrarVariables();
        famosoSeleccionado = (Famoso) getArguments().getSerializable(CLAVE_FAMOSO);
        adapterFamoso = new AdapterFamoso(this);
        adapterPelicula = new AdapterPelicula(this);
        peliculaController = new ControllerPelicula();
        traerFamoso();
        cargarVariables();
        contenedorPeliculas.setLayoutManager(new LinearLayoutManager(getContext(), contenedorPeliculas.HORIZONTAL, false));
        contenedorPeliculas.setAdapter(adapterPelicula);
        return view;
    }


    public void encontrarVariables() {
        textViewNombreFamoso = view.findViewById(R.id.Fragment_TextView_NombreFamoso);
        textViewBioFamoso = view.findViewById(R.id.Fragment_TextView_BioFamoso);
        imagenFamoso = view.findViewById(R.id.Fragment_ImageView_Famoso);
        contenedorPeliculas = view.findViewById(R.id.Fragment_Recicler_contenedorDeApareceEn);
    }

      public void traerFamoso(){
        peliculaController.traerFamoso(famosoSeleccionado.getId(), new ResultListener<Famoso>() {
            @Override
            public void finish(Famoso result) {
                famosoSeleccionado = result;
                textViewBioFamoso.setText(famosoSeleccionado.getBiography());

            }
        });
        peliculaController.traePeliculaDeFamoso(famosoSeleccionado.getId(), new ResultListener<List<Pelicula>>() {
            @Override
            public void finish(List<Pelicula> result) {
                adapterPelicula.setPeliculaList(result);
            }
        });
    }

    public void cargarVariables() {
        Glide.with(this).load(famosoSeleccionado.generaURLImagen()).placeholder(R.drawable.load)
                .error(R.drawable.logomuvi).into(imagenFamoso);
        textViewNombreFamoso.setText(famosoSeleccionado.getNombre());
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerDelFragment = (ListenerDeFragment) context;
    }

    public interface ListenerDeFragment {
        public void informarFamoso(Famoso famoso);
        public void informarPelicula(Pelicula pelicula);
    }

    @Override
    public void informarFamoso(Famoso famoso) {
        listenerDelFragment.informarFamoso(famoso);
    }

    @Override
    public void informarPelicula(Pelicula pelicula) {
        listenerDelFragment.informarPelicula(pelicula);
    }

    @Override
    public void informarGenero(Integer genero) {

    }
}
