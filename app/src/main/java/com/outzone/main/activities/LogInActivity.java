package com.outzone.main.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.outzone.main.R;
import com.outzone.main.ddbb.login.LogIn;
import com.outzone.main.tools.EncriptionManager;
import com.outzone.main.tools.LogInManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Lo inicializo para que las funciones estáticas tengan el contexto, está mal o las clase estáticas o mantener el contexto en la inicialización.
        LogInManager lgm = new LogInManager(getApplicationContext());

        /**
         * Obtengo las preferencias compartidas si las hay
         * e inicio sesión
         **/
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String retrievedUser = sharedPref.getString("usr",null);
        String retrievedHash = sharedPref.getString("hash",null);

        if (retrievedHash != null && retrievedUser != null) {
            int rol = LogInManager.getUsrRol(retrievedUser);
            String members = LogInManager.getUsrMembers(retrievedUser);
            if (retrievedUser != null && retrievedHash != null && rol > 0 && members != null) {
                if (!retrievedUser.isEmpty() && !retrievedHash.isEmpty()) { //Separado del anterior para evitar fallos al llamar a .empty() en un null
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    intent.putExtra("usr", retrievedUser);
                    intent.putExtra("hash", retrievedHash);
                    intent.putExtra("rol", rol);
                    intent.putExtra("members", members);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getApplicationContext(), R.string.login_with_retrieved, Toast.LENGTH_LONG).show();

                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.login_retrieved_error, Toast.LENGTH_LONG).show();
            }
        }

        TextInputEditText usr = findViewById(R.id.login_usr);
        TextInputEditText passwd = findViewById(R.id.login_passwd);

        /*Button bSignup = findViewById(R.id.signupButton);
        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn newUsr = new LogIn("dqnid", EncriptionManager.getHashString("1234"), 3, "");
                LogInManager.singUp(newUsr);
            }
        });*/

        Button bLogin = findViewById(R.id.loginButton);
        bLogin.setEnabled(true);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hash;
                //TODO: bool para bloquear la acción del botón o para el botón
                //android:launchMode="singleTop"
                bLogin.setEnabled(false);
                try {
                    if ((hash = LogInManager.getAccess(usr.getText().toString(), passwd.getText().toString())) != null) {
                        String user = usr.getText().toString();
                        int rol = LogInManager.getUsrRol(usr.getText().toString());
                        String members = LogInManager.getUsrMembers(usr.getText().toString());

                        //En teoría estas comprobaciones las está gestionando la base de datos pero por si la he liado con algo
                        if (user != null && hash != null && rol > 0 && members != null) {
                            if (!user.isEmpty() && !hash.isEmpty()) { //Separado del anterior para evitar fallos al llamar a .empty() en un null
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                intent.putExtra("usr", user);
                                intent.putExtra("hash", hash);
                                intent.putExtra("rol", rol);
                                intent.putExtra("members", members);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                CheckBox cb = findViewById(R.id.save_session_cb);
                                if (cb.isChecked()){
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("usr", user);
                                    editor.putString("hash", hash);
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(), R.string.session_saved, Toast.LENGTH_LONG).show();
                                }

                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_LONG).show();
                            bLogin.setEnabled(true);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.wrong_login, Toast.LENGTH_LONG).show();
                        bLogin.setEnabled(true);
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    bLogin.setEnabled(true);
                }
            }
        });

        Button signup = findViewById(R.id.signupButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}