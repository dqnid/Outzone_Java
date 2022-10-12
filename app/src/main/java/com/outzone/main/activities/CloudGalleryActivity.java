package com.outzone.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.outzone.main.R;
import com.outzone.main.adapters.CloudGalleryRecyclerViewAdapter;
import com.outzone.main.tools.FileManager;

import java.util.ArrayList;

public class CloudGalleryActivity extends AppCompatActivity {
    private ArrayList<String> imagePaths;
    private RecyclerView imagesRV;
    private CloudGalleryRecyclerViewAdapter imageRVAdapter;
    private final int SELECT_PICTURES = 123;

    //TODO: ¿Pedir permisos?
    //TODO: Botón "Añadir Fotos"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_gallery);
        imagePaths = new ArrayList<>();
        imagesRV = findViewById(R.id.idRVImages);

        prepareRecyclerView();

        getImagePath();

        FloatingActionButton fab = findViewById(R.id.fab2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
//**These following line is the important one!
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Selecciona las imágenes"), SELECT_PICTURES); //startActivityForResult cualdo funcione esto
            }
        });
    }

    private void prepareRecyclerView() {
        imageRVAdapter = new CloudGalleryRecyclerViewAdapter(CloudGalleryActivity.this, imagePaths);

        GridLayoutManager manager = new GridLayoutManager(CloudGalleryActivity.this, 4);

        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);
    }

    private void getImagePath() {
        imagePaths.add("https://upload.wikimedia.org/wikipedia/commons/d/d7/Android_robot.svg");
        imagePaths.add("https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png?20210701224649");
    }

    /**
     * Para gestionar la carga de imágenes
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
                        Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                        //Subir al servidor
                        currentItem = currentItem + 1;
                    }
                } else if(data.getData() != null) {
                    String imagePath = data.getData().getPath();
                    //Subir al servidor
                }
            }
        }
    }
}