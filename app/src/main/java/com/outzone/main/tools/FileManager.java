package com.outzone.main.tools;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.outzone.main.BuildConfig;

import java.io.File;

public class FileManager {
    public static final String BASE_DIR = "Outzone";

    public static void createBaseDirectory(Context context, Activity activity){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + File.separator + BASE_DIR);
        if (!myDir.exists())
            myDir.mkdirs();
    }

    public static File[] getFilesDir(Context context, Activity activity){
        String path = Environment.getExternalStorageDirectory().toString()+File.separator+BASE_DIR;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();

        if (files != null) {
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
            }
        }
        return files;
    }

    public static String get_mime_type(String url) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
    }

    public static void viewPDF(Context context,String filename) {
        File file = new File(filename);

        Uri uriPdfPath = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
        pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenIntent.setClipData(ClipData.newRawUri("", uriPdfPath));
        pdfOpenIntent.setDataAndType(uriPdfPath, "application/pdf");
        pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |  Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {
            context.startActivity(pdfOpenIntent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText(context,"There is no app to load corresponding PDF",Toast.LENGTH_LONG).show();

        }
    }

    public static void selectImage(Context context){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
//**These following line is the important one!
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        int SELECT_PICTURES = 1;
        context.startActivity(Intent.createChooser(intent, "Select Picture")); //startActivityForResult cualdo funcione esto
    }
}
