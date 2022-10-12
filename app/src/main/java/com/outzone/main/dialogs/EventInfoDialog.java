package com.outzone.main.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.outzone.main.R;
import com.outzone.main.ddbb.event.Event;

public class EventInfoDialog extends Dialog implements View.OnClickListener{

    private TextView tvName,tvDate,tvLocation,tvId,tvDescription,tvUrl;
    Event event;

    public EventInfoDialog(@NonNull Context context) {
        super(context);
        //TODO: ajustar dimensiones en función de la pantalla
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_event_info);
        tvName = findViewById(R.id.event_name);
        tvDate = findViewById(R.id.event_date);
        tvLocation = findViewById(R.id.event_location);
        tvId = findViewById(R.id.event_id);
        tvDescription = findViewById(R.id.event_description);
        tvUrl = findViewById(R.id.event_url);

        //tvName.setText(TextUtils.isEmpty(event.getName()) ? "" : event.getName());
        if (!TextUtils.isEmpty(event.getName()))
        {
            tvName.setText(event.getName());
            tvName.setVisibility(View.VISIBLE);
        } else {
            tvName.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(event.getDate())) {
            tvDate.setText((event.getDate()));
            tvDate.setVisibility(View.VISIBLE);
            findViewById(R.id.event_date_hint).setVisibility(View.VISIBLE);
        } else {
            tvDate.setVisibility(View.GONE);
            findViewById(R.id.event_date_hint).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty((event.getLocation())))
        {
            tvLocation.setText((event.getLocation()));
            tvLocation.setVisibility(View.VISIBLE);
            findViewById(R.id.event_location_hint).setVisibility(View.VISIBLE);
        } else {
            tvLocation.setVisibility(View.GONE);
            findViewById(R.id.event_location_hint).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(event.getId()))
        {
            tvId.setText(event.getId());
            tvId.setVisibility(View.VISIBLE);
            findViewById(R.id.event_id_hint).setVisibility(View.VISIBLE);
        } else {
            tvId.setVisibility(View.GONE);
            findViewById(R.id.event_id_hint).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(event.getDescription()))
        {
            tvDescription.setText(event.getDescription());
            tvDescription.setVisibility(View.VISIBLE);
            findViewById(R.id.event_description_hint).setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
            findViewById(R.id.event_description_hint).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(event.getUrl()))
        {
            tvUrl.setText(event.getUrl());
            tvUrl.setVisibility(View.VISIBLE);
            findViewById(R.id.event_url_hint).setVisibility(View.VISIBLE);
        } else {
            tvUrl.setVisibility(View.GONE);
            findViewById(R.id.event_url_hint).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    public void setEvent(Event e){
        this.event = e;
    }
}
