package com.muviteam.muviapp.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muviteam.muviapp.utils.ResultListener;

import java.util.List;

public class FirestoreDao {

    public static final String PELICULAS_FAVORITAS = "peliculasFavoritas";
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private ContainerPelicula containerPeliculas;

    public FirestoreDao() {
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        traerPeliculasFavoritas();
    }


    public void agregarPeliculaAFav(Pelicula pelicula) {
        if (currentUser == null)
            return;
     if (containerPeliculas.contieneLaPelicula(pelicula)){
            containerPeliculas.removerPelicula(pelicula);
        }
        else {
            containerPeliculas.agregarPelicula(pelicula);
        }
        firestore.collection(PELICULAS_FAVORITAS)
                .document(currentUser.getUid())
                .set(containerPeliculas);
    }

    private void traerPeliculasFavoritas() {
        if (currentUser== null){
            containerPeliculas = new ContainerPelicula();
            return;
        }
        firestore.collection(PELICULAS_FAVORITAS)
                .document(currentUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            //en el listener del on succes intento tranfomar el documento a un container
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                containerPeliculas = documentSnapshot.toObject(ContainerPelicula.class);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    //en el on failure del listener inicializo un container vacio
                    public void onFailure(@NonNull Exception e) {
                        containerPeliculas = new ContainerPelicula();
                    }
                });
    }

    public void traerPeliculasFavoritas(final ResultListener<List<Pelicula>> listenerDelController){
        if (currentUser == null){
            containerPeliculas = new ContainerPelicula();
            listenerDelController.finish(containerPeliculas.getResults());
            return;
        }
        firestore.collection(PELICULAS_FAVORITAS)
                .document(currentUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            //en el listener del on succes intento tranfomar el documento a un container
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                containerPeliculas = documentSnapshot.toObject(ContainerPelicula.class);
                if (containerPeliculas == null){
                    containerPeliculas = new ContainerPelicula();
                }
                listenerDelController.finish(containerPeliculas.getResults());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    //en el on failure del listener inicializo un container vacio
                    public void onFailure(@NonNull Exception e) {
                        containerPeliculas = new ContainerPelicula();
                        listenerDelController.finish(containerPeliculas.getResults());
                    }
                });
    }
}
