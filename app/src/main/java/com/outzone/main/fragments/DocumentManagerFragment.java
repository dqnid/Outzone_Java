package com.outzone.main.fragments;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.outzone.main.R;
import com.outzone.main.adapters.FilesRecyclerViewAdapter;
import com.outzone.main.tools.FileManager;

import java.io.File;
import java.util.ArrayList;

/**
 * Comprueba si existe la ruta por defecto:
 * - si existe, carga un listado con los documentos
 * - si no, la crea
 * + botón de añadir documento
 * + documentos clickables
 * + compartir documentos
 * ~ editar eventos (puede que sea pasarse)
 **/
public class DocumentManagerFragment extends Fragment {
    private View rootView;
    private ArrayList<String> filePath;
    public static final int PERMISSION_REQUEST_CODE = 200;
    private final int SELECT_PICTURES = 234;

    public DocumentManagerFragment() {
        // Required empty public constructor
    }

    public static DocumentManagerFragment newInstance() {
        DocumentManagerFragment fragment = new DocumentManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Solicito los permisos necesarios antes de nada
         **/
        if (checkFilePermission(getContext())) {
            Toast.makeText(getContext(), "Usando permisos de acceso a disco", Toast.LENGTH_SHORT).show();
        } else {
            requestFilePermission(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FileManager.createBaseDirectory(getContext(),getActivity());

        /**
         * Obtengo la lista de ficheros
         **/
        File[] files = FileManager.getFilesDir(getContext(),getActivity());
        if (files != null) {
            filePath = new ArrayList<>();
            for (File f : files) {
                filePath.add(f.getName());
            }
        }

        /**
         * Inflo el RV
         **/
        rootView = inflater.inflate(R.layout.fragment_document_manager, container, false);

        RecyclerView filesRV = rootView.findViewById(R.id.idRVdocs);
        FilesRecyclerViewAdapter fileRVAdapter = new FilesRecyclerViewAdapter(getActivity(),getContext(),filePath);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        filesRV.setLayoutManager(manager);
        filesRV.setAdapter(fileRVAdapter);

        FloatingActionButton fab = rootView.findViewById(R.id.fab3);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("documents/*"); //allows any image file type. Change * to specific extension to limit it
//**These following line is the important one!
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Selecciona los documentos"), SELECT_PICTURES); //startActivityForResult cualdo funcione esto
            }
        });

        return rootView;
    }

    /**
     * Solicitud de permisos
     **/
    public static boolean checkFilePermission(Context context) {
        int permission1 = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestFilePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(getContext(), R.string.permissions_granted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.permissions_denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Carga de documentos
     **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICTURES) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    int currentItem = 0;
                    while(currentItem < count) {
                        Uri documentUri = data.getClipData().getItemAt(currentItem).getUri();
                        //Subir el documento
                        currentItem = currentItem + 1;
                    }
                } else if(data.getData() != null) {
                    String docPath = data.getData().getPath();
                    //Subir el documento
                }
            }
        }
    }
}