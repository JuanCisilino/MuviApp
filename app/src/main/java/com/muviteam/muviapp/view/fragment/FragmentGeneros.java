package com.muviteam.muviapp.view.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.muviteam.muviapp.R;
import com.muviteam.muviapp.controller.ControllerPelicula;
import com.muviteam.muviapp.model.Pelicula;
import com.muviteam.muviapp.utils.ResultListener;
import com.muviteam.muviapp.view.adapter.AdapterPelicula;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGeneros extends Fragment implements AdapterPelicula.ListenerDelAdapter {

    private ImageView accion, aventura, animacion, cienciaFiccion, comedia, crimen, documental, drama, familia, fantasia, guerra, historia, horror, misterio, musica, peliculasdetv, romance, western;
    private View view;
    private ControllerPelicula controllerPelicula;
    private AdapterPelicula adapterPelicula;
    private ListenerDeFragment listenerDeFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_generos, container, false);
        controllerPelicula = new ControllerPelicula();
        adapterPelicula = new AdapterPelicula(this);
        encontrarVariables();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerDeFragment = (ListenerDeFragment) context;
    }

    public void encontrarVariables() {
        accion = view.findViewById(R.id.boton_genero1);
        accion.setOnClickListener(new OnClickDeLosBotones());

        aventura = view.findViewById(R.id.boton_genero2);
        aventura.setOnClickListener(new OnClickDeLosBotones());

        animacion = view.findViewById(R.id.boton_genero3);
        animacion.setOnClickListener(new OnClickDeLosBotones());

        cienciaFiccion = view.findViewById(R.id.boton_genero4);
        cienciaFiccion.setOnClickListener(new OnClickDeLosBotones());

        comedia = view.findViewById(R.id.boton_genero5);
        comedia.setOnClickListener(new OnClickDeLosBotones());

        crimen = view.findViewById(R.id.boton_genero6);
        crimen.setOnClickListener(new OnClickDeLosBotones());

        documental = view.findViewById(R.id.boton_genero7);
        documental.setOnClickListener(new OnClickDeLosBotones());

        drama = view.findViewById(R.id.boton_genero8);
        drama.setOnClickListener(new OnClickDeLosBotones());

        familia = view.findViewById(R.id.boton_genero9);
        familia.setOnClickListener(new OnClickDeLosBotones());

        fantasia = view.findViewById(R.id.boton_genero10);
        fantasia.setOnClickListener(new OnClickDeLosBotones());

        guerra = view.findViewById(R.id.boton_genero11);
        guerra.setOnClickListener(new OnClickDeLosBotones());

        historia = view.findViewById(R.id.boton_genero12);
        historia.setOnClickListener(new OnClickDeLosBotones());

        horror = view.findViewById(R.id.boton_genero13);
        horror.setOnClickListener(new OnClickDeLosBotones());

        misterio = view.findViewById(R.id.boton_genero14);
        misterio.setOnClickListener(new OnClickDeLosBotones());

        musica = view.findViewById(R.id.boton_genero15);
        musica.setOnClickListener(new OnClickDeLosBotones());

        peliculasdetv = view.findViewById(R.id.boton_genero16);
        peliculasdetv.setOnClickListener(new OnClickDeLosBotones());

        romance = view.findViewById(R.id.boton_genero17);
        romance.setOnClickListener(new OnClickDeLosBotones());

        western = view.findViewById(R.id.boton_genero18);
        western.setOnClickListener(new OnClickDeLosBotones());
    }



    @Override
    public void informarPelicula(Pelicula pelicula) {

    }

    @Override
    public void informarGenero(Integer genero) {
        listenerDeFragment.recibirGenero(genero);
    }


    public interface ListenerDeFragment {
        public void recibirGenero(Integer genero);
    }

    private class OnClickDeLosBotones implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.boton_genero1:
                    Toast.makeText(getContext(), "Peliculas de accion", Toast.LENGTH_LONG).show();
                    listenerDeFragment.recibirGenero(28);
                    break;
                case R.id.boton_genero2:
                    Toast.makeText(getContext(), "Peliculas de aventura", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(12);

                    break;
                case R.id.boton_genero3:
                    Toast.makeText(getContext(), "Peliculas de animacion", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(16);

                    break;
                case R.id.boton_genero4:
                    Toast.makeText(getContext(), "Peliculas de ciencia ficcion", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(28);

                    break;
                case R.id.boton_genero5:
                    Toast.makeText(getContext(), "Peliculas de comedia", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(35);

                    break;
                case R.id.boton_genero6:
                    Toast.makeText(getContext(), "Peliculas de crimen", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(80);

                    break;
                case R.id.boton_genero7:
                    Toast.makeText(getContext(), "Peliculas de documental", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(99);

                    break;
                case R.id.boton_genero8:
                    Toast.makeText(getContext(), "Peliculas de drama", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(18);

                    break;
                case R.id.boton_genero9:
                    Toast.makeText(getContext(), "Peliculas de familia", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(10751);

                    break;
                case R.id.boton_genero10:
                    Toast.makeText(getContext(), "Peliculas de fantasia", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(14);

                    break;
                case R.id.boton_genero11:
                    Toast.makeText(getContext(), "Peliculas de guerra", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(10752);

                    break;
                case R.id.boton_genero12:
                    Toast.makeText(getContext(), "Peliculas de historia", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(36);

                    break;
                case R.id.boton_genero13:
                    Toast.makeText(getContext(), "Peliculas de horror", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(27);

                    break;
                case R.id.boton_genero14:
                    Toast.makeText(getContext(), "Peliculas de misterio", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(9648);

                    break;
                case R.id.boton_genero15:
                    Toast.makeText(getContext(), "Peliculas de musica", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(10402);

                    break;
                case R.id.boton_genero16:
                    Toast.makeText(getContext(), "Peliculas de tv", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(10770);

                    break;
                case R.id.boton_genero17:
                    Toast.makeText(getContext(), "Peliculas de romance", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(10749);

                    break;
                case R.id.boton_genero18:
                    Toast.makeText(getContext(), "Peliculas de western", Toast.LENGTH_SHORT).show();
                    listenerDeFragment.recibirGenero(37);

                    break;

            }
        }
    }

}
