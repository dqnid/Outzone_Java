package com.outzone.main.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.outzone.main.R;
import com.outzone.main.adapters.AttendeesListAdapter;
import com.outzone.main.adapters.EventListAdapter;
import com.outzone.main.ddbb.attendance.Attendance;
import com.outzone.main.ddbb.attendance.AttendanceEventMember;
import com.outzone.main.ddbb.attendance.AttendanceRepository;
import com.outzone.main.ddbb.event.Event;
import com.outzone.main.ddbb.event.EventViewModel;
import com.outzone.main.ddbb.member.MemberViewModel;
import com.outzone.main.fragments.CalendarFragment;

import java.util.ArrayList;
import java.util.List;

public class ListAttendeesActivity extends AppCompatActivity {
    private AttendanceRepository attRep;
    private List<AttendanceEventMember> attList;
    private RecyclerView attendeesRV;
    private AttendeesListAdapter attAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_attendees);

        /**
         * Inflo el reclycreview
         **/
        RecyclerView recyclerView = findViewById(R.id.recyclerview_attendance);
        final AttendeesListAdapter adapter = new AttendeesListAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        setAttendanceInAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void setAttendanceInAdapter(AttendeesListAdapter adapter){
        attRep = new AttendanceRepository(getApplication());
        attList = attRep.getAttendanceEventMember();

        adapter.setAttendance(attList);
    }
}