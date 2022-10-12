package com.outzone.main.adapters;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.outzone.main.R;
import com.outzone.main.ddbb.attendance.Attendance;
import com.outzone.main.ddbb.attendance.AttendanceRepository;
import com.outzone.main.ddbb.event.Event;
import com.outzone.main.dialogs.EventInfoDialog;

import java.util.List;

public class EventCheckInAdapter extends RecyclerView.Adapter<EventCheckInAdapter.EventViewHolder>{
    private final LayoutInflater mInflater;
    private List<Event> mEvents; // Cached copy of events
    AttendanceRepository attRep;
    Context context;
    String userID;

    public EventCheckInAdapter(Context context, String member, Application application) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.userID = member;
        attRep = new AttendanceRepository(application);
    }

    @Override
    public EventCheckInAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_card_item, parent, false);
        return new EventCheckInAdapter.EventViewHolder(itemView, context);
    }

    /**
     * Aquí es dónde escribimos sobre el ViewHolder
     * */
    //TODO:Cambiar logo en función del tipo de actividad
    @Override
    public void onBindViewHolder(EventCheckInAdapter.EventViewHolder holder, int position) {
        if (mEvents != null) {
            Event current = mEvents.get(position);
            holder.eventNameView.setText(current.getName());
            holder.eventDateView.setText(current.getDate());

            //Leer de la base de datos
            /**
             * Problema, por cada elemento de cada refresco hará una consulta a la ddbb
             **/
            Attendance temp = attRep.isAttending(userID, current.getId());
            if (temp != null){
                holder.attendanceSwitch.setChecked(true);
            } else {
                holder.attendanceSwitch.setChecked(false);
            }

        } else {
            //En caso de que los datos no estén listos aún.
            holder.eventNameView.setText(R.string.no_event);
        }
    }

    /**
     * Para asociar una lista con este adaptador
     * se le llama en la actividad
     */
    public void setEvents(List<Event> events) {
        mEvents = events;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mEvents != null)
            return mEvents.size();
        else return 0;
    }

    public Event getEventAtPosition(int position) {
        return mEvents.get(position);
    }


    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView eventDateView;
        private final TextView eventNameView;
        private final Switch attendanceSwitch;
        private Context context;

        private EventViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(this);
            eventDateView = itemView.findViewById(R.id.event_date);
            eventNameView = itemView.findViewById(R.id.event_text);
            attendanceSwitch = itemView.findViewById(R.id.attendance_switch);
            attendanceSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Event temp = getEventAtPosition(getAdapterPosition());
                    if (attendanceSwitch.isChecked()) {
                        Attendance attendee = new Attendance(userID, temp.getId(), 0, 0);
                        attRep.insertAttendee(attendee);
                    } else {
                        attRep.deleteAttendee(userID, temp.getId());
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            Event toShow = getEventAtPosition(getAdapterPosition());
            EventInfoDialog tempDialog = new EventInfoDialog(context);
            tempDialog.setEvent(toShow);
            tempDialog.show();
        }
    }
}
