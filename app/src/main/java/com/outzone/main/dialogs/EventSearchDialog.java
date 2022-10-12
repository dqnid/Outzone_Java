package com.outzone.main.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.outzone.main.R;
import com.outzone.main.ddbb.event.Event;
import com.outzone.main.adapters.EventListAdapter;
import com.outzone.main.ddbb.event.EventViewModel;

import java.util.List;

public class EventSearchDialog extends Dialog implements View.OnClickListener{
    String keyword;
    Button bSearch;
    EditText tvSearch;
    Context context;
    Fragment fragment;
    public EventSearchDialog(@NonNull Context context, Fragment fragment) {
        super(context);
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_event_search);

        /**
         * Inflo el recyclerview
         **/
        RecyclerView recyclerView = findViewById(R.id.recyclerview_calendar_search);
        final EventListAdapter adapter = new EventListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EventViewModel mEventViewModel = ViewModelProviders.of(fragment).get(EventViewModel.class);

        /**
         * Gestiono la pulsación del botón
         **/
        bSearch = findViewById(R.id.search_event_button);
        tvSearch = findViewById(R.id.search_event_text);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = "%" + tvSearch.getText().toString() + "%";
                List<Event> eventos = mEventViewModel.getEventsKeyWord(keyword);
                adapter.setEvents(eventos);
            }
        });
    }
}
