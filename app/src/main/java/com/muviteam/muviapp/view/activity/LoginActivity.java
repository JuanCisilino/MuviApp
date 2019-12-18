package com.muviteam.muviapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muviteam.muviapp.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    //Defino los objetos
    private TextInputLayout myTextInputLayoutEmail;
    private TextInputEditText myTextInputEditTextEmail;

    private TextInputLayout myTextInputLayoutPassword;
    private TextInputEditText myTextInputEditTextPassword;

    private Button myButtonIngresar;
    private Button myButtonRegistrarse;

    private LoginButton myLoginButtonFacebook;
    private CallbackManager myCallbackManager;

    private TextView myTextViewIngresarSinRegistrarse;

    //Declaramos el objeto FirebaseAuth
    private FirebaseAuth myFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        encuentroLosObjetosPorId();

        //Cada vez que inicia la app se desloguea el usuario
        FirebaseAuth.getInstance().signOut();

        //Inicializamos el objeto myFirebaseAuth
        myFirebaseAuth = FirebaseAuth.getInstance();

        configuroLosInputEditText();

        configuroLosOnClick();

        configuroLoginButtonFacebook();

    }

    private void configuroLosInputEditText(){

        myTextInputEditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() < 6){
                    //le seteo el mensaj de error al text imput layout
                    myTextInputLayoutPassword.setError("Tiene que tener minimo 6 caracteres");
                }else {
                    //le borro el mensaje de error cuando el contenido este bien
                    myTextInputLayoutPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void configuroLoginButtonFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        myLoginButtonFacebook.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        myCallbackManager = CallbackManager.Factory.create();
        myLoginButtonFacebook.registerCallback(myCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Login Exitoso", Toast.LENGTH_LONG).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Se Cancelo el logueo", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Se produjo un error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void irAMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //El onActivityResult es para el loginButton de facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        myCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void configuroLosOnClick() {
        myButtonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Invocamos al metodo
                registrarUsuarioFirebase();
            }
        });

        myButtonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Invocamos el metodo
                ingresarUsuarioFirebase();
            }
        });

        myTextViewIngresarSinRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
    }

    private void encuentroLosObjetosPorId() {
        myTextInputLayoutEmail = findViewById(R.id.LoginActivity_TextInputLayout_AlertasDelEmail);
        myTextInputEditTextEmail = findViewById(R.id.LoginActivity_TextInputEditText_Email);

        myTextInputLayoutPassword = findViewById(R.id.LoginActivity_TextInputLayout_AlertasDelPassword);
        myTextInputEditTextPassword = findViewById(R.id.LoginActivity_TextInputEditText_Password);

        myButtonIngresar = findViewById(R.id.LoginActivity_Button_BotonIngresar);
        myButtonRegistrarse = findViewById(R.id.LoginActivity_Button_BotonRegistrarse);

        myLoginButtonFacebook = findViewById(R.id.LoginActivity_LoginButton_FacebookLogin);

        myTextViewIngresarSinRegistrarse = findViewById(R.id.LoginActivity_TextView_IngresarSinRegistrarse);
    }

    private void registrarUsuarioFirebase() {

        //Obtenemos el email y el password desde las cajas de texto
        String stringEmail = myTextInputEditTextEmail.getText().toString().trim();
        String stringPassword = myTextInputEditTextPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacias
        if (TextUtils.isEmpty(stringEmail)) {
            Toast.makeText(this, "Se debe ingresar un Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(stringPassword)) {
            Toast.makeText(this, "Se debe ingresar un Password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Creacion del nuevo usuario
        myFirebaseAuth.createUserWithEmailAndPassword(stringEmail, stringPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Chekeando si se a podido completar correctamente
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                            FirebaseUser myFirebaseUser = myFirebaseAuth.getCurrentUser();
                            updateUI(myFirebaseUser);
                        } else {
                            Toast.makeText(LoginActivity.this, "No se pudo registrar", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void ingresarUsuarioFirebase() {
        //Obtenemos el email y el password desde las cajas de texto
        String stringEmail = myTextInputEditTextEmail.getText().toString().trim();
        String stringPassword = myTextInputEditTextPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacias
        if (TextUtils.isEmpty(stringEmail)) {
            Toast.makeText(this, "Se debe ingresar un Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(stringPassword)) {
            Toast.makeText(this, "Se debe ingresar un Password", Toast.LENGTH_SHORT).show();
            return;
        }

        myFirebaseAuth.signInWithEmailAndPassword(stringEmail, stringPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = myFirebaseAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Fallo Logueo.",
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }

    //Este metodo si tiene un usuario valido va a pasar directamente a la MainActivity
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(this, AltaYPerfilUsuario.class));
        }
    }
    private void updateUIFacebook(Boolean estaAdentroConFacebook) {
        if (estaAdentroConFacebook) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        myFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = myFirebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        //chekea si el usuario ya esta logueado y te manda directamente a la mainActivity
        FirebaseUser myCurrentUser = myFirebaseAuth.getCurrentUser();

        //Chekea si el usuario de facebook ya esta logueado y te manda directamente a la MainActivity si es asi
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        //Llamo a los metodo que me comprueban eso
        updateUI(myCurrentUser);
        updateUIFacebook(isLoggedIn);
    }
}
