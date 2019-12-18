package com.muviteam.muviapp.view.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muviteam.muviapp.R;
import com.muviteam.muviapp.controller.ControllerPelicula;
import com.muviteam.muviapp.model.Famoso;
import com.muviteam.muviapp.model.Pelicula;
import com.muviteam.muviapp.utils.ResultListener;
import com.muviteam.muviapp.view.adapter.AdapterFamoso;
import com.muviteam.muviapp.view.adapter.AdapterPelicula;
import com.muviteam.muviapp.view.adapter.AdapterViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment implements AdapterPelicula.ListenerDelAdapter,
        AdapterFamoso.ListenerDelAdapter {

    private RecyclerView recyclerView,recyclerEstrenos,recyclerPopulares;
    private ListenerDeFragment listenerDelFragment;
    private View view;
    private AdapterPelicula adapterPeliculas,adapterUpcoming,adapterPopulares;
    private ControllerPelicula peliculaController;
    private ViewPager viewPager;
    private AdapterViewPager adapterViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        encontrarVariables();
        reconocerAdapters();
        viewpaigearlaToda();
        traePopulares();
        traePelis();
        traeUpcom();
        cargarYSetearRecyclers();
        return view;
    }

    public void encontrarVariables(){
        recyclerPopulares = view.findViewById(R.id.MainActivity_MultiSnapRecyclerView_PopularListMovie);
        recyclerView = view.findViewById(R.id.MainActivity_MultiSnapRecyclerView_TopRatedListMovie);
        recyclerEstrenos = view.findViewById(R.id.MainActivity_MultiSnapRecyclerView_UpcomingListMovie);
        viewPager = view.findViewById(R.id.fragment_view_Pager);
    }

    public void reconocerAdapters(){
        adapterPeliculas = new AdapterPelicula(this);
        adapterUpcoming = new AdapterPelicula(this);
        adapterPopulares = new AdapterPelicula(this);
        peliculaController = new ControllerPelicula();
        adapterViewPager = new AdapterViewPager(getActivity().getSupportFragmentManager(), fragmentList);
    }

    public void viewpaigearlaToda(){
        peliculaController.traerListaPeliculas(new ResultListener<List<Pelicula>>() {
            @Override
            public void finish(List<Pelicula> result) {
                for (Pelicula pelicula : result) {
                    FragmentViewPager fragmentViewPager = FragmentViewPager.dameUnFragment(pelicula);
                    fragmentList.add(fragmentViewPager);
                }
                adapterViewPager.actualizarListFragment(fragmentList);
            }
        });
    }

    public void traePelis(){
        peliculaController.traePelicula(new ResultListener<List<Pelicula>>() {
            @Override
            public void finish(List<Pelicula> result) {
                adapterPeliculas.setPeliculaList(result);
            }
        });
    }

    public void traePopulares(){
        peliculaController.traePopulares(new ResultListener<List<Pelicula>>() {
            @Override
            public void finish(List<Pelicula> result) {
                adapterPopulares.setPeliculaList(result);
            }
        });
    }

    public void traeUpcom(){
        peliculaController.traeUpcoming(new ResultListener<List<Pelicula>>() {
            @Override
            public void finish(List<Pelicula> result) {
                adapterUpcoming.setPeliculaList(result);
            }
        });
    }


    public void cargarYSetearRecyclers(){
        recyclerPopulares.setLayoutManager(new LinearLayoutManager(getContext(),recyclerView.HORIZONTAL,false));
        recyclerPopulares.setAdapter(adapterPopulares);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),recyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(adapterPeliculas);
        recyclerEstrenos.setLayoutManager(new LinearLayoutManager(getContext(),recyclerEstrenos.HORIZONTAL, false));
        recyclerEstrenos.setAdapter(adapterUpcoming);
        viewPager.setAdapter(adapterViewPager);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerDelFragment = (ListenerDeFragment) context;
    }

    @Override
    public void informarPelicula(Pelicula pelicula) {
        listenerDelFragment.recibirPelicula(pelicula);
    }

    @Override
    public void informarGenero(Integer genero) {

    }

    @Override
    public void informarFamoso(Famoso famoso) {
        listenerDelFragment.recibirFamoso(famoso);
    }

    public interface ListenerDeFragment {
        public void recibirPelicula(Pelicula pelicula);
        public void recibirFamoso(Famoso famoso);
    }



}
