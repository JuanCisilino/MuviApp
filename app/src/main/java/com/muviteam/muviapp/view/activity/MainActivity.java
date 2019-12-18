package com.muviteam.muviapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muviteam.muviapp.R;
import com.muviteam.muviapp.controller.ControllerPelicula;
import com.muviteam.muviapp.model.Famoso;
import com.muviteam.muviapp.model.Pelicula;
import com.muviteam.muviapp.model.Usuario;
import com.muviteam.muviapp.utils.ResultListener;
import com.muviteam.muviapp.view.adapter.AdapterFavorito;
import com.muviteam.muviapp.view.fragment.FragmentBusqueda;
import com.muviteam.muviapp.view.fragment.FragmentGeneros;
import com.muviteam.muviapp.view.fragment.FragmentLista;
import com.muviteam.muviapp.view.fragment.FragmentWatchlist;
import com.muviteam.muviapp.view.fragment.ToolbarFragment;
import com.muviteam.muviapp.view.adapter.AdapterFamoso;
import com.muviteam.muviapp.view.adapter.AdapterPelicula;
import com.muviteam.muviapp.view.fragment.FragmentDetalleFamoso;
import com.muviteam.muviapp.view.fragment.FragmentDetallePelicula;
import com.muviteam.muviapp.view.fragment.FragmentHome;
import com.muviteam.muviapp.view.fragment.FragmentViewPager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterPelicula.ListenerDelAdapter
        , FragmentHome.ListenerDeFragment, FragmentViewPager.ListenerDeFragment, FragmentDetallePelicula.ListenerDeFragment, FragmentGeneros.ListenerDeFragment,
        FragmentLista.ListenerDeFragment, AdapterFamoso.ListenerDelAdapter, FragmentDetalleFamoso.ListenerDeFragment, AdapterFavorito.ListenerDelAdapter,
        FragmentWatchlist.ListenerDeFragment, FragmentBusqueda.ListenerDeFragment {

    private Toolbar myToolbar;
    private ArrayAdapter<String> myArrayAdapterString;
    private DrawerLayout myDrawerLayout;
    private NavigationView myNavigationView;
    private String letraGenero;

    private ToolbarFragment toolbarFragment;

    private FirebaseAuth myFirebaseAuth; //El firebase Auth te da las cosas del user
    private LoginManager myLoginManager; //El LoginManager me deja mover las cosas del user de facebook
    private FirebaseUser firebaseUser;

    private Fragment currentFragment;
    private AdapterPelicula adapterPelicula;
    private ControllerPelicula controllerPelicula;
    private RecyclerView recyclerView;
    private SearchView mySearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.FragmentLista_MultiSnapRecyclerView_ListMovie);
        adapterPelicula = new AdapterPelicula(this);
        controllerPelicula = new ControllerPelicula();
        myFirebaseAuth = FirebaseAuth.getInstance();
        myLoginManager = LoginManager.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        encuentroVariablesPorId();
        configuroToolbar();
        toolbarFragment = new ToolbarFragment();
        pegarFragment(toolbarFragment);
    }

    private void encuentroVariablesPorId() {
        myDrawerLayout = findViewById(R.id.MainActivity_DrawerLayout_Contenedor);
        myNavigationView = findViewById(R.id.MainActivity_NavigationView);
        myToolbar = findViewById(R.id.MainActivity_Include_Toolbar);
        recyclerView = findViewById(R.id.MainActivity_contenedorSearch);
    }


    private void configuroToolbar() {
        setSupportActionBar(myToolbar);

        ActionBarDrawerToggle myActionBarDrawerToggle =
                new ActionBarDrawerToggle(this,
                        myDrawerLayout,
                        myToolbar,
                        R.string.open_drawer,
                        R.string.close_drawer); //Animacion de la hamburguesa (Open, Close Drawer)

        myDrawerLayout.addDrawerListener(myActionBarDrawerToggle);
        myActionBarDrawerToggle.syncState();

        //le seteo el onClick,le digo this por que la MainActivity implementa el listener (NavigationView.OnNavigationItemSelectedListener)
        myNavigationView.setNavigationItemSelectedListener(this);
    }

    //Este metodo te lo da la extencion con AppCompatActivity
    //En este caso lo vamos a usar para configurar una parte de la Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflo el toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        //Encuentro los componentes del menu
        MenuItem myMenuItemSearch = menu.findItem(R.id.ToolBarMenu_Item_action_search);
        MenuItem myMenuItemProfile = menu.findItem(R.id.ToolBarMenu_Item_perfilUser);

        //Configuro que pasa si se clickea el Perfil
        myMenuItemProfile.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (firebaseUser == null) {
                    Toast.makeText(getApplicationContext(), "Por Favor Inicie Sesion!!!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Ingresando a su Perfil", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(), AltaYPerfilUsuario.class));
                }
                return true;
            }
        });


        mySearchView = (SearchView) myMenuItemSearch.getActionView();
        mySearchView.setQueryHint("Que pelicula buscas?");

        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FragmentBusqueda fragmentBusqueda = new FragmentBusqueda();
                Bundle bundle = new Bundle();
                bundle.putString(fragmentBusqueda.BUSQUEDA, query);
                fragmentBusqueda.setArguments(bundle);
                currentFragment = fragmentBusqueda;
                pegarFragment(fragmentBusqueda);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    //Este metodo se tiene que implementar por (NavigationView.OnNavigationItemSelectedListener) para configurar los items Del NavigationView
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int intItemSeleccionadoId = menuItem.getItemId();

        switch (intItemSeleccionadoId) {
            case R.id.MenuPrincipal_Item_Home:
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.MenuPrincipal_Item_Configuracion:
                if (firebaseUser == null) {
                    Toast.makeText(this, "Por Favor Inicie Sesion!!!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                } else {
                    Toast.makeText(this, "Ingresando a su Perfil", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(), AltaYPerfilUsuario.class));
                }
                break;
            case R.id.MenuPrincipal_Item_CerrarSesion:
                Toast.makeText(this, "Cerrar sesion", Toast.LENGTH_LONG).show();
                //Cierro la sesion de firebase y la de facebook y abro el LoginActivity
                myLoginManager.logOut();
                myFirebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
        myDrawerLayout.closeDrawers();
        return true;
    }

    private void pegarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.MainActivity_FrameLayout_ContenedorDeFragments, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void recontraPegarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.MainActivity_FrameLayout_ContenedorDeFragments, fragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void recibirPelicula(Pelicula pelicula) {
        Toast.makeText(this, pelicula.getTitulo(), Toast.LENGTH_SHORT).show();
        FragmentDetallePelicula fragment_detallePelicula = new FragmentDetallePelicula();
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragment_detallePelicula.CLAVE_PELICULA, pelicula);
        fragment_detallePelicula.setArguments(bundle);
        currentFragment = fragment_detallePelicula;
        pegarFragment(fragment_detallePelicula);
    }

    @Override
    public void recibirFamoso(Famoso famoso) {
        Toast.makeText(this, famoso.getNombre(), Toast.LENGTH_SHORT).show();
        FragmentDetalleFamoso fragment_detalleFamoso = new FragmentDetalleFamoso();
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragment_detalleFamoso.CLAVE_FAMOSO, famoso);
        fragment_detalleFamoso.setArguments(bundle);
        currentFragment = fragment_detalleFamoso;
        pegarFragment(fragment_detalleFamoso);
    }


    public void showHideFragment(final Fragment fragment) {
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);
        if (fragment.isHidden()) {
            fragTransaction.show(fragment);
        } else {
            fragTransaction.hide(fragment);
        }
        fragTransaction.commit();
    }

    @Override
    public void informarFamoso(Famoso famoso) {
        Toast.makeText(this, famoso.getNombre(), Toast.LENGTH_SHORT).show();
        FragmentDetalleFamoso fragmentDetalleFamoso = new FragmentDetalleFamoso();
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragmentDetalleFamoso.CLAVE_FAMOSO, famoso);
        fragmentDetalleFamoso.setArguments(bundle);
        currentFragment = fragmentDetalleFamoso;
        pegarFragment(fragmentDetalleFamoso);
    }

    @Override
    public void informarPelicula(Pelicula pelicula) {
        Toast.makeText(this, pelicula.getTitulo(), Toast.LENGTH_SHORT).show();
        FragmentDetallePelicula fragment_detallePelicula = new FragmentDetallePelicula();
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragment_detallePelicula.CLAVE_PELICULA, pelicula);
        fragment_detallePelicula.setArguments(bundle);
        currentFragment = fragment_detallePelicula;
        pegarFragment(fragment_detallePelicula);
    }

    @Override
    public void informarGenero(Integer genero) {
        FragmentLista fragmentLista = new FragmentLista();
        Bundle bundle = new Bundle();
        letraGenero = String.valueOf(genero);
        bundle.putString(fragmentLista.VALORGENERO, letraGenero);
        fragmentLista.setArguments(bundle);
        currentFragment = fragmentLista;
        pegarFragment(fragmentLista);
    }

    @Override
    public void recibirGenero(Integer genero) {
        FragmentLista fragmentLista = new FragmentLista();
        Bundle bundle = new Bundle();
        letraGenero = String.valueOf(genero);
        bundle.putString(fragmentLista.VALORGENERO, letraGenero);
        fragmentLista.setArguments(bundle);
        currentFragment = fragmentLista;
        pegarFragment(fragmentLista);
    }
}
