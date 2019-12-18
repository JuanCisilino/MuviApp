package com.muviteam.muviapp.view.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muviteam.muviapp.R;
import com.muviteam.muviapp.controller.ControllerPelicula;
import com.muviteam.muviapp.model.Pelicula;
import com.muviteam.muviapp.utils.ResultListener;
import com.muviteam.muviapp.view.adapter.AdapterPelicula;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBusqueda extends Fragment implements AdapterPelicula.ListenerDelAdapter {

    public static final String BUSQUEDA = "consulta";

    private View view;
    private ControllerPelicula controllerPelicula;
    private ListenerDeFragment listenerDelFragment;
    private ItemTouchHelper itemTouchHelper;
    private AdapterPelicula adapterPelicula;
    private RecyclerView recyclerViewLista;
    private String consultaQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_busqueda, container, false);
        recyclerViewLista = view.findViewById(R.id.FragmentBusqueda_MultiSnapRecyclerView_Lista);
        consultaQuery = getArguments().getString(BUSQUEDA);
        controllerPelicula = new ControllerPelicula();
        adapterPelicula = new AdapterPelicula(this);
        controllerPelicula.traerPeliculasPorBusqueda(consultaQuery, new ResultListener<List<Pelicula>>() {
            @Override
            public void finish(List<Pelicula> result) {
                adapterPelicula.setPeliculaList(result);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        recyclerViewLista.setLayoutManager(gridLayoutManager);
        recyclerViewLista.setAdapter(adapterPelicula);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewLista);
        return view;
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

    public interface ListenerDeFragment {
        public void recibirPelicula(Pelicula pelicula);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
            ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(adapterPelicula.getPeliculaList(), fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}
