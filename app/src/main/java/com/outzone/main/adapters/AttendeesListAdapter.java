package com.outzone.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.outzone.main.R;
import com.outzone.main.ddbb.attendance.AttendanceEventMember;

import java.util.List;

public class AttendeesListAdapter extends RecyclerView.Adapter<AttendeesListAdapter.AttendeeViewHolder>{
    private final LayoutInflater mInflater;
    private List<AttendanceEventMember> attME;
    Context context;


    public AttendeesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;

    }

    @Override
    public AttendeesListAdapter.AttendeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_attendance_item, parent, false);
        return new AttendeesListAdapter.AttendeeViewHolder(itemView);
    }

    /**
     * Aquí es dónde escribimos sobre el ViewHolder
     * */
    @Override
    public void onBindViewHolder(AttendeesListAdapter.AttendeeViewHolder holder, int position) {
        holder.att_name.setText(attME.get(position).getnMember());
        holder.att_ape1.setText(attME.get(position).getApe1());
        holder.att_ape2.setText(attME.get(position).getApe2());
        holder.event_date.setText(attME.get(position).getDate());
        holder.event_name.setText(attME.get(position).getnEvent());
    }

    /**
     * Para asociar una lista con este adaptador
     * se le llama en la actividad
     */
    public void setAttendance(List<AttendanceEventMember> attendance) {
        attME = attendance;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (attME != null)
            return attME.size();
        else return 0;
    }

    class AttendeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView att_name;
        private final TextView att_ape1;
        private final TextView att_ape2;
        private final TextView event_name;
        private final TextView event_date;

        private AttendeeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            att_name = itemView.findViewById(R.id.attendee_name);
            att_ape1 = itemView.findViewById(R.id.attendee_ape1);
            att_ape2 = itemView.findViewById(R.id.attendee_ape2);
            event_name = itemView.findViewById(R.id.event_name);
            event_date = itemView.findViewById(R.id.event_date);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
