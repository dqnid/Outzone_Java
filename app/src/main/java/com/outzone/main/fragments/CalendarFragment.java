package com.outzone.main.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.outzone.main.R;
import com.outzone.main.ddbb.event.Event;
import com.outzone.main.adapters.EventListAdapter;
import com.outzone.main.ddbb.event.EventViewModel;
import com.outzone.main.tools.SessionHash;
import com.outzone.main.dialogs.EventSearchDialog;
import com.outzone.main.dialogs.NewEventDialog;
import com.outzone.main.tools.DateManager;

import java.util.List;

public class CalendarFragment extends Fragment {
    private CalendarView calendar;
    private View rootView;
    private EventViewModel mEventViewModel;
    private Fragment fragment = this;
    private int previousMonth = -1;
    SessionHash sesion;

    public CalendarFragment() {
    }

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int[] today = DateManager.getActualDateInt();

        /**
         * Inflo el layout general (calendario)
         **/
        rootView = inflater.inflate(R.layout.fragment_calendar_tello, container, false);

        /**
         * Inflo el reclycreview
         **/
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_calendar);
        final EventListAdapter adapter = new EventListAdapter(rootView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        mEventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        mEventViewModel.getAllEvents().observe(getViewLifecycleOwner(),
                new Observer<List<Event>>() {
                    @Override
                    public void onChanged(@Nullable List<Event> events) {
                        //Carga inicial, necesaria para que se muestren correctamente los eventos del día actual en primera carga
                        SelectDataTask task = new SelectDataTask(requireActivity(),adapter,today[0], today[1], today[2]);
                        task.execute();
                    }
                });

        /**
         * Inflo el calendario (CalendarView)
         **/
        calendar = rootView.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                //Recargo el recyclerview
                today[0] = i; today[1] = i1+1; today[2] = i2;
                SelectDataTask task = new SelectDataTask(requireActivity(),adapter, i, i1+1, i2);
                task.execute();

                //Si cambia el mes, hago otra cosa
                if (previousMonth != i1){
                    previousMonth = i1;
                    SelectEventsMonthTask task1 = new SelectEventsMonthTask(requireActivity(),calendar,i,i1+1,i2);
                    task1.execute();
                }
            }
        });

        /**
         * Inflo los botones
         **/
        Button bSearch = rootView.findViewById(R.id.bSearch);
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventSearchDialog tempDialog = new EventSearchDialog(view.getContext(), fragment);
                tempDialog.show();
            }
        });

        Button bAdd = rootView.findViewById(R.id.bAdd);
        if (sesion.getRol() < 2){
            bAdd.setEnabled(false);
            bAdd.setVisibility(View.GONE);
        } else {

            /**
             * Botón de añadir eventos
             **/
            bAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewEventDialog tempNDialog = new NewEventDialog(view.getContext(), fragment, mEventViewModel);
                    tempNDialog.show();
                }
            });
            bAdd.setEnabled(true);
            bAdd.setVisibility(View.VISIBLE);

            /**
             * Borrado de elementos por deslizamiento
             **/
            ItemTouchHelper helper = new ItemTouchHelper(
                    new ItemTouchHelper.SimpleCallback(0,
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                        @Override
                        // We are not implementing onMove() in this app
                        public boolean onMove(RecyclerView recyclerView,
                                              RecyclerView.ViewHolder viewHolder,
                                              RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                            int position = viewHolder.getAdapterPosition();
                            Event myEvent = adapter.getEventAtPosition(position);

                            /**
                             * Diálogo de confirmación
                             **/
                            AlertDialog.Builder confirmAlertBuilder = new AlertDialog.Builder(getContext());
                            confirmAlertBuilder.setTitle(getResources().getString(R.string.confirm_delete_title));
                            confirmAlertBuilder.setMessage(getResources().getString(R.string.confirm_delete_message));
                            confirmAlertBuilder.setPositiveButton(getResources().getString(R.string.positive), new
                                    DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            mEventViewModel.deleteEvent(myEvent);
                                            Toast.makeText(getContext(),
                                                    getString(R.string.delete_event_preamble) + ": " +
                                                            myEvent.getEvent(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                            confirmAlertBuilder.setNegativeButton(getResources().getString(R.string.negative), new
                                    DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getContext(),getResources().getString(R.string.negative_message),Toast.LENGTH_SHORT).show();
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                            confirmAlertBuilder.show();
                        }
                    });
            // Attach the item touch helper to the recycler view
            helper.attachToRecyclerView(recyclerView);
        }
        return rootView;
    }

    /**
     * Comunicación con la actividad
     **/
    public void setSesion(SessionHash sesion){
        this.sesion = sesion;
    }


    /**
     * Tarea de query de eventos en un día.
     **/
    private class SelectDataTask extends AsyncTask<Void, Void, List<Event>> {

        private final FragmentActivity activity;
        private final EventListAdapter adapter;
        private final int anio;
        private final int mes;
        private final int dia;

        public SelectDataTask(final FragmentActivity activity, final EventListAdapter adapter, final int anio, final int mes, final int dia) {
            this.activity = activity;
            this.adapter = adapter;
            this.anio = anio;
            this.mes = mes;
            this.dia = dia;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Event> doInBackground(Void... voids) {
            return mEventViewModel.getEventsDate(DateManager.formatDate(anio, mes, dia));
        }

        @Override
        protected void onPostExecute(List<Event> eventos) {
            super.onPostExecute(eventos);
            adapter.setEvents(eventos);
        }
    }

    /**
     * Tarea de query de eventos en un mes y actualización del calendario.
     **/
    private class SelectEventsMonthTask extends AsyncTask<Void, Void, List<String>> {

        private final FragmentActivity activity;
        private final CalendarView calendar;
        private final int anio;
        private final int mes;
        private final int dia;

        public SelectEventsMonthTask(final FragmentActivity activity, CalendarView calendar,final int anio, final int mes, final int dia) {
            this.activity = activity;
            this.calendar = calendar;
            this.anio = anio;
            this.mes = mes;
            this.dia = dia;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return mEventViewModel.getDateOfEventsMonth(DateManager.formatMonthQuery(anio,mes));
        }

        @Override
        protected void onPostExecute(List<String> eventos) {
            super.onPostExecute(eventos);
            for (String l : eventos){
                DateManager.getDateFromString(l);
                //TODO:Cambiar color o añadir un indicador al día.
            }
        }
    }
}