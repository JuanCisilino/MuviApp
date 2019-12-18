package com.muviteam.muviapp.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muviteam.muviapp.R;
import com.muviteam.muviapp.controller.ControllerFirestore;
import com.muviteam.muviapp.controller.ControllerPelicula;
import com.muviteam.muviapp.model.Credits;
import com.muviteam.muviapp.model.Famoso;
import com.muviteam.muviapp.model.FirestoreDao;
import com.muviteam.muviapp.model.Pelicula;
import com.muviteam.muviapp.model.Videos;
import com.muviteam.muviapp.utils.ResultListener;
import com.muviteam.muviapp.view.activity.LoginActivity;
import com.muviteam.muviapp.view.activity.YoutubeActivity;
import com.muviteam.muviapp.view.adapter.AdapterFamoso;
import com.muviteam.muviapp.view.adapter.AdapterPelicula;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetallePelicula extends Fragment implements AdapterFamoso.ListenerDelAdapter,
        AdapterPelicula.ListenerDelAdapter{

    public static final String CLAVE_PELICULA = "clavePelicula";

    private RecyclerView recyclerView, recyclerSimilares;
    private TextView textViewTitulo, textViewSinopsis, textViewDirector;
    private Button botonTrailer;
    private ImageView imagenPelicula, posterPelicula;
    private AdapterFamoso adapterFamoso;
    private AdapterPelicula adapterPelicula;
    private ListenerDeFragment listenerDelFragment;
    private Pelicula peliculaSeleccionada;
    private View view;
    private String key, direc;
    private ControllerPelicula peliculaController;
    private FloatingActionButton floatingShare, floatingFavoritos;
    private ControllerFirestore firestoreController;
    private Boolean esFavorita;
    private FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detalle_pelicula, container, false);
        encontrarVariables();
        peliculaSeleccionada = (Pelicula) getArguments().getSerializable(CLAVE_PELICULA);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        adapterFamoso = new AdapterFamoso(this);
        adapterPelicula = new AdapterPelicula(this);
        peliculaController = new ControllerPelicula();
        firestoreController = new ControllerFirestore();

        floatingShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peliculaController.traerTrailer(new ResultListener<Videos>() {
                    @Override
                    public void finish(Videos result) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        key = result.getResults().get(0).getKey();
                        intent.putExtra(Intent.EXTRA_TEXT, "https://youtu.be/" + key + " Mirate el trailer de " + peliculaSeleccionada.getTitulo());
                        intent.setType("text/plain");
                        Intent chooser = Intent.createChooser(intent, "Por donde queres compartir el Trailer?");
                        startActivity(chooser);
                    }
                },peliculaSeleccionada.getId());
            }
        });

        floatingFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser == null) {
                    Toast.makeText(getContext(), "por favor logueateee!!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                firestoreController.agregarPeliculaAFav(peliculaSeleccionada);
                esFavorita = !esFavorita;
                actualizarFav();
            }
        });

        cargarVariables();
        traerLaLista();

        firestoreController.traerListaDeFavorito(new ResultListener<List<Pelicula>>() {
            @Override
            public void finish(List<Pelicula> result) {
                esFavorita = result.contains(peliculaSeleccionada);
                actualizarFav();
                habilitarOnClickDeFav();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(adapterFamoso);
        recyclerSimilares.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerSimilares.setAdapter(adapterPelicula);
        return view;
    }

    public void encontrarVariables(){
        textViewTitulo = view.findViewById(R.id.fragment_detalle_TituloPelicula);
        textViewSinopsis = view.findViewById(R.id.fragment_detalle_DescripcionPelicula);
        textViewDirector = view.findViewById(R.id.Fragment_TextView_Director);
        imagenPelicula = view.findViewById(R.id.detalle_peliculas_imageview);
        botonTrailer = view.findViewById(R.id.detalle_peliculas_boton_trailer);
        posterPelicula = view.findViewById(R.id.poster_detalle_pelicula);
        recyclerView = view.findViewById(R.id.fragment_famoso_recycler);
        recyclerSimilares = view.findViewById(R.id.fragment_similares_recycler);
        floatingShare = view.findViewById(R.id.share_floating);
        floatingFavoritos = view.findViewById(R.id.fav_floating);
    }

    public void cargarVariables(){
        Glide.with(this).load(peliculaSeleccionada.generaURLImagencelda()).placeholder(R.drawable.load)
                .error(R.drawable.logomuvi).into(imagenPelicula);
        Glide.with(this).load(peliculaSeleccionada.generaURLImagen()).placeholder(R.drawable.load)
                .error(R.drawable.logomuvi).into(posterPelicula);
        textViewTitulo.setText(peliculaSeleccionada.getTitulo());
        textViewSinopsis.setText(peliculaSeleccionada.getSinopsis());
        peliculaController.traerCredits(new ResultListener<Credits>() {
            @Override
            public void finish(Credits result) {
                if (result != null) {
                    direc = result.getCrew().get(0).getNombre();
                } else {
                    direc = "Unknown";
                }
                textViewDirector.setText(direc);
            }
        }, peliculaSeleccionada.getId());
        botonTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peliculaController.traerTrailer(new ResultListener<Videos>() {
                    @Override
                    public void finish(Videos result) {
                        key = result.getResults().get(0).getKey();
                        cambiarDeActivity();
                    }
                },peliculaSeleccionada.getId());
            }
        });
    }

    public void traerLaLista(){
        peliculaController.traeListaFamosos(peliculaSeleccionada.getId(), new ResultListener<Credits>() {
            @Override
            public void finish(Credits result) {
                adapterFamoso.setFamosoList(result.getCast());
            }
        });
        peliculaController.traerPeliculaSimilar(peliculaSeleccionada.getId(), new ResultListener<List<Pelicula>>() {
            @Override
            public void finish(List<Pelicula> result) {
                adapterPelicula.setPeliculaList(result);
            }
        });
    }

    private void cambiarDeActivity(){
        Intent intent = new Intent(getContext(), YoutubeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(YoutubeActivity.CLAVE_KEY,key);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerDelFragment = (ListenerDeFragment) context;
    }

    private void habilitarOnClickDeFav() {
        floatingFavoritos.setClickable(true);
    }

    private void actualizarFav(){
        if (esFavorita){
            floatingFavoritos.setImageResource(R.drawable.ic_favorite_black_24dp); }
        else {
            floatingFavoritos.setImageResource(R.drawable.ic_favorite_border_black_24dp); }
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

    public interface ListenerDeFragment{
        public void informarFamoso(Famoso famoso);
        public void informarPelicula(Pelicula pelicula);
    }

}
