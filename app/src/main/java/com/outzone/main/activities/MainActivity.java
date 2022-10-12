package com.outzone.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.outzone.main.R;
import com.outzone.main.fragments.CheckInFragment;
import com.outzone.main.fragments.DocumentManagerFragment;
import com.outzone.main.ddbb.GeneralRoomDatabase;
import com.outzone.main.tools.SessionHash;
import com.outzone.main.fragments.CalendarFragment;
import com.outzone.main.fragments.MemberListFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener{

    private static final int USR_LIST_ACTIVITY_REQUEST_CODE = 1;
    private DrawerLayout drawerLayout;
    private MenuItem menuItem; //default item
    public static SessionHash sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Genero la sesión
         * accedo a la base de datos, desencripto el resultado y escribo en
         * -- Cuidado con el ciclo de vida que igual lo tumba --
         **/
        GeneralRoomDatabase db = GeneralRoomDatabase.getDatabase(getApplicationContext());
        Bundle extras = getIntent().getExtras();

        String usr;
        String hash;
        int rol;
        String members;
        Intent intent = getIntent();
        if (extras != null) {
            usr = intent.getStringExtra("usr");
            hash = intent.getStringExtra("hash"); //mantengo el hash para verificar cualquier comunicación sin necesidad de gestionar sesiones
            rol = intent.getIntExtra("rol",0);
            members = intent.getStringExtra("members");
        }else{
            usr = "unouser";
            hash = "hash";
            rol = 0;
            members = "";
        }
        sesion = SessionHash.getSesionHash(usr, hash, rol,members);
        if (rol == 0){
            Toast.makeText(getApplicationContext(), R.string.nouser2, Toast.LENGTH_LONG).show();
        }

        /*
         * Drawer and toolbar
         * */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * Establecer el calendario como pantalla principal
         **/
        menuItem = navigationView.getMenu().getItem(0).getSubMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);

        /**
         * Mostrar menús en función del rol
         **/
        MenuItem administra;
        if (sesion.getRol() < 2) {
            administra = navigationView.getMenu().getItem(1);
            administra.setEnabled(false);
            administra.setVisible(false);
        } else {
            administra = navigationView.getMenu().getItem(1);
            administra.setEnabled(true);
            administra.setVisible(true);
        }

        drawerLayout.addDrawerListener(this);
    }

    /**
     * Si botón de retroceso:
     *   recoger el menú
     **/
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    /**
     * Gestiono las opciones del drawer por id
     **/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toast toast;
        if (item.getItemId() != R.id.calendar)
            menuItem.setChecked(false); //reset del checked, que en un principio está el calendario

        String members = sesion.getMembers();
        String[] temp = members.split(";");
        Bundle bundle = new Bundle();
        bundle.putString("dni", temp[0]);
        bundle.putInt("rol",sesion.getRol());


        switch (item.getItemId()) {
            case R.id.checkin:
                CheckInFragment fragment = CheckInFragment.newInstance();

                fragment.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                setTitle(R.string.checkin);

                //.setCustomAnimations(R.anim.nav_enter, R.anim.nav_exit)
                break;
            case R.id.listUsrs:
                MemberListFragment uFragment = MemberListFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, uFragment)
                        .commit();
                setTitle(R.string.listUsrs);
                break;
            case R.id.calendar:
                CalendarFragment cFragment = CalendarFragment.newInstance();
                cFragment.setSesion(sesion);
                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, cFragment)
                        .commit();
                setTitle(R.string.calendar);

                //.setCustomAnimations(R.anim.nav_enter, R.anim.nav_exit)
                break;
            case R.id.gallery:
                Intent intent = new Intent(getApplicationContext(), CloudGalleryActivity.class);
                startActivity(intent);
                break;
            case R.id.documents:
                DocumentManagerFragment dFragment = DocumentManagerFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, dFragment)
                        .commit();
                setTitle(R.string.documents);
                //Intent fpdfIntent = new Intent(getApplicationContext(), FormatPDFActivity.class);
                //startActivity(fpdfIntent);
                break;
            case R.id.close_session:
                Intent closeIntent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(closeIntent);

                SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("usr");
                editor.remove("hash");
                editor.apply();

                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Acceder al enlace de la web
     **/
    public void accessWeb(View view) {
        String url = getString(R.string.web);
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Implicit web intent failed!");
        }
    }
}