package com.outzone.main.ddbb.event;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.outzone.main.ddbb.GeneralRoomDatabase;

import java.util.List;

public class EventRepository {
    private EventDao mEventDao;
    private LiveData<List<Event>> mAllEvents;

    EventRepository(Application application) {
        GeneralRoomDatabase db = GeneralRoomDatabase.getDatabase(application);
        mEventDao = db.eventDao();
        mAllEvents = mEventDao.getAllEvents();
    }

    LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }

    List<Event> getAllEventsList(){return mEventDao.getAllEventsList();}

    public List<Event> getEventsDate(String date) {
        return mEventDao.getEventsDate(date);
    }

    public List<Event> getEventKeyWord(String keyword){
        return mEventDao.getEventsKeyWord(keyword);
    }

    List<String> getDateOfEventsMonth(String month) { return mEventDao.getDateOfEventsMonth(month);}

    public void insert(Event event) {
        new EventRepository.insertAsyncTask(mEventDao).execute(event);
    }

    public void deleteAll() {
        new EventRepository.deleteAllEventsAsyncTask(mEventDao).execute();
    }

    public void deleteEvent(Event event) {
        new EventRepository.deleteEventAsyncTask(mEventDao).execute(event);
    }

    /**
     * Inserta usuario en la base de datos.
     */
    private static class insertAsyncTask extends AsyncTask<Event, Void, Void> {

        private EventDao mAsyncTaskDao;

        insertAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Elimina todas las entradas de la tabla, pero no la tabla como tal
     */
    private static class deleteAllEventsAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao mAsyncTaskDao;

        deleteAllEventsAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     * Eliminar el usuario parámetro de la tabla.
     */
    private static class deleteEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao mAsyncTaskDao;

        deleteEventAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.deleteEvent(params[0]);
            return null;
        }
    }


}
