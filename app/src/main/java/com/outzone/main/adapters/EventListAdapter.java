package com.outzone.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.outzone.main.R;
import com.outzone.main.ddbb.event.Event;
import com.outzone.main.dialogs.EventInfoDialog;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder>{
    private final LayoutInflater mInflater;
    private List<Event> mEvents; // Cached copy of events
    Context context;

    public EventListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public EventListAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new EventListAdapter.EventViewHolder(itemView, context);
    }

    /**
     * Aquí es dónde escribimos sobre el ViewHolder
     * */
    @Override
    public void onBindViewHolder(EventListAdapter.EventViewHolder holder, int position) {
        if (mEvents != null) {
            Event current = mEvents.get(position);
            holder.eventItemView.setText(current.getEvent());
        } else {
            //En caso de que los datos no estén listos aún.
            holder.eventItemView.setText(R.string.no_event);
        }
    }

    /**
     * Para asociar una lista de usuarios con este adaptador
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

    /**
     * Retorna el usuario en una posición concreta, para la selección y borrado.
     */
    public Event getEventAtPosition(int position) {
        return mEvents.get(position);
    }


    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView eventItemView;
        private Context context;

        private EventViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(this);
            eventItemView = itemView.findViewById(R.id.textView);
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
