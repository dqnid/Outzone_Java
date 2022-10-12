package com.outzone.main.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.outzone.main.R;
import com.outzone.main.activities.CloudGalleryActivity;
import com.outzone.main.activities.ListAttendeesActivity;
import com.outzone.main.ddbb.GeneralRoomDatabase;
import com.outzone.main.ddbb.attendance.AttendanceDao;
import com.outzone.main.ddbb.event.Event;
import com.outzone.main.adapters.EventCheckInAdapter;
import com.outzone.main.ddbb.event.EventDao;
import com.outzone.main.ddbb.event.EventViewModel;
import com.outzone.main.ddbb.member.MemberViewModel;
import com.outzone.main.tools.DateManager;

import java.util.ArrayList;
import java.util.List;

public class CheckInFragment extends Fragment {
    static Button bDate;
    View rootView;
    EventViewModel eVM;
    String member;
    List<Event> allEvents;
    int rol;

    public CheckInFragment() {
    }

    public static CheckInFragment newInstance() {
        return new CheckInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_check_in, container, false);

        /**
         * Obtengo la información del usuario mediante un bundle
         **/
        member = getArguments().getString("dni");
        rol = getArguments().getInt("rol");

        /**
         * Limito las opciones
         **/
        Button listButton = rootView.findViewById(R.id.check_attendance);
        if (rol < 3){
            listButton.setVisibility(View.GONE);
        } else {
            listButton.setVisibility(View.VISIBLE);
            listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Abrir listado de attendees
                    Intent intent = new Intent(getContext(), ListAttendeesActivity.class);
                    //intent.putExtra("id", member);
                    startActivity(intent);
                }
            });
        }

        /**
         * Inflo el recyclerview
         **/
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_card_check_in);
        final EventCheckInAdapter adapter = new EventCheckInAdapter(rootView.getContext(),member, getActivity().getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        eVM = ViewModelProviders.of(this).get(EventViewModel.class);
        allEvents = new ArrayList<>();
        allEvents = eVM.getAllEventsList();
        adapter.setEvents(allEvents);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getParentFragmentManager(), "check_in");
    }

    public static void setDateButton(int year, int month, int day)
    {
        if (bDate != null)
        {
            bDate.setText(DateManager.formatDate(year,month,day));
        }
    }
}