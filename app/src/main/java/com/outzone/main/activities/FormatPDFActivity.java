package com.outzone.main.activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.outzone.main.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FormatPDFActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format_pdfactivity);

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        Button bGen = findViewById(R.id.button_gen_pdf);
        bGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formatPDF();
            }
        });
    }

    private void formatPDF(){
        Bitmap bmp, logo;
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_oz_foreground);
        logo = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        int pageHeight = 1120;
        int pagewidth = 792;
        int npages = 1;

        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, npages).create();

        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        /**
         * Logo
         **/
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(logo, 56, 40, paint);

        /**
         * Texto
         **/
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, R.color.blue_complementary));
        canvas.drawText("Título", 209, 100, title);
        canvas.drawText("Esto es un subtítulo", 209, 80, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));
        title.setTextSize(15);
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Este documento se ha creado a mano en la aplicación.", 396, 560, title);

        /**
         * Fin página
         **/
        pdfDocument.finishPage(myPage);

        /**
         * Escritura en fichero
         * //TODO: crear en directorio específico
         **/
        File file = new File(Environment.getExternalStorageDirectory(), "prueba.pdf");

        try{
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(getApplicationContext(), R.string.pdf_generated, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
    }

    /**
     * Solicita y comprueba permisos
     **/
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, R.string.permissions_granted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.permissions_denied, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}