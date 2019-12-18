package com.muviteam.muviapp.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;
import com.muviteam.muviapp.R;
import com.muviteam.muviapp.model.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AltaYPerfilUsuario extends AppCompatActivity {

    private Uri uri;
    private int PICK_IMAGE_REQUEST = 1;
    @BindView(R.id.UsuarioActivity_ImageView_imagenUsuario)
    ImageView profilePic;
    @BindView(R.id.UsuarioActivity_Button_CargarImagen)
    Button botonCargarImagen;
    @BindView(R.id.UsuarioActivity_Button_GuardarCambios) Button botonGuardarCambios;
    @BindView(R.id.UsuarioActivity_TextInputEditText_Nombre)
    EditText editTextNombre;
    @BindView(R.id.UsuarioActivity_TextInputEditText_Apellido) EditText editTextApellido;
    @BindView(R.id.UsuarioActivity_ConstraintLayout)
    ConstraintLayout layout;
    @BindView(R.id.UsuarioActivity_DayNightSwitch)
    DayNightSwitch dayNightSwitch;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUsuer;
    private StorageReference path;
    private Usuario usuarioImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_yperfil_usuario);
        ButterKnife.bind(this);
        storage = FirebaseStorage.getInstance();
        currentUsuer = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        path = storage.getReference().child("ProfiePics").child(currentUsuer.getUid());
        botonCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        botonGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreUsuario = editTextNombre.getText().toString();
                String apellidoUsuario = editTextApellido.getText().toString();
                Usuario usuario = new Usuario(nombreUsuario,apellidoUsuario,profilePic.toString());
                guardarInfoUsuario(usuario);
                cargarImagenAFirebase();
            }
        });
        dayNightSwitch.setDuration(450);
        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean isNight) {
                if (isNight) {
                    layout.setAlpha(1f);
                } else {
                    layout.setAlpha(0.7f);
                }
            }
        });
        traerUsuarioLogueado();
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cargarImagenAFirebase() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bm=((BitmapDrawable)profilePic.getDrawable()).getBitmap();
        bm.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = path.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AltaYPerfilUsuario.this, "imagen Cargada exitosamente", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(this, MainActivity.class));
    }

    private void guardarInfoUsuario(final Usuario usuario){
        firestore.collection("Usuarios")
                .document(currentUsuer.getUid())
                .set(usuario);
        cargarImagenDelStorageAlDatabase();
    }

    private void cargarImagenDelStorageAlDatabase() {
        path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                firestore.collection("Usuarios")
                        .document(currentUsuer.getUid())
                        .update("imagenUrl",uri);
            }
        });
    }

    private void traerUsuarioLogueado() {
        firestore.collection("Usuarios")
                .document(currentUsuer.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        usuarioImagen = documentSnapshot.toObject(Usuario.class);
                        if (usuarioImagen != null) {
                            editTextNombre.setHint(usuarioImagen.getNombre());
                            editTextApellido.setHint(usuarioImagen.getApellido());
                            if (usuarioImagen.getImagenUrl() != null) {
                                Glide.with(AltaYPerfilUsuario.this)
                                        .load(usuarioImagen.getImagenUrl())
                                        .into(profilePic);
                            }
                        }
                    }
                });
    }

}
