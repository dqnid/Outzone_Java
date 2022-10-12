package com.outzone.main.adapters;

import static com.outzone.main.tools.FileManager.BASE_DIR;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.outzone.main.R;
import com.outzone.main.tools.FileManager;

import java.io.File;
import java.util.ArrayList;

public class FilesRecyclerViewAdapter extends RecyclerView.Adapter<FilesRecyclerViewAdapter.RecyclerViewHolder>{

    private final Context context;
    private ArrayList<String> filePath;
    private Activity activity;

    public FilesRecyclerViewAdapter(Activity activity, Context context, ArrayList<String> filePath) {
        this.context = context;
        this.filePath = filePath;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_file_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv.setText(filePath.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verPDF
                String filename = Environment.getExternalStorageDirectory().toString()+ File.separator + BASE_DIR + File.separator + filePath.get(position);
                FileManager.viewPDF(v.getContext(), filename);
            }
        });
    }

    public void setFilePath(ArrayList<String> filePath){
        this.filePath = filePath;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (filePath!=null)
            return filePath.size();
        return 0;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.file_name_tv);
        }
    }
}
