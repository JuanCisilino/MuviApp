package com.muviteam.muviapp.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muviteam.muviapp.R;
import com.muviteam.muviapp.controller.ControllerFirestore;
import com.muviteam.muviapp.model.Pelicula;
import com.muviteam.muviapp.utils.ResultListener;
import com.muviteam.muviapp.view.activity.LoginActivity;
import com.muviteam.muviapp.view.adapter.AdapterFavorito;
import com.muviteam.muviapp.view.adapter.AdapterPelicula;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWatchlist extends Fragment implements AdapterFavorito.ListenerDelAdapter {

    @BindView(R.id.FragmentLista_MultiSnapRecyclerView_ListMovie)
    RecyclerView recyclerViewLista;
    private AdapterFavorito adapterFavoritos;
    private View view;
    private ControllerFirestore firestoreController;
    private ListenerDeFragment listenerDelFragment;
    private ItemTouchHelper itemTouchHelper;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        ButterKnife.bind(this, view);
        firestoreController = new ControllerFirestore();
        adapterFavoritos = new AdapterFavorito(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firestoreController.traerListaDeFavorito(new ResultListener<List<Pelicula>>() {
            @Override
            public void finish(List<Pelicula> result) {
                adapterFavoritos.setFavoritoList(result);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        recyclerViewLista.setLayoutManager(gridLayoutManager);
        recyclerViewLista.setAdapter(adapterFavoritos);
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

            Collections.swap(adapterFavoritos.getFavoritoList(), fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


}
