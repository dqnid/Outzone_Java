package com.outzone.main.ddbb.event;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private EventRepository mRepository;

    private LiveData<List<Event>> mAllEvents;

    public EventViewModel(Application application) {
        super(application);
        mRepository = new EventRepository(application);
        mAllEvents = mRepository.getAllEvents();
    }

    public LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void insert(Event event){ mRepository.insert(event); }

    public void deleteEvent(Event Event) {
        mRepository.deleteEvent(Event);
    }

    public List<Event> getEventsDate(String date){return mRepository.getEventsDate(date);}

    public List<Event> getEventsKeyWord(String keyword){return mRepository.getEventKeyWord(keyword);}

    public List<String> getDateOfEventsMonth(String month){
        return mRepository.getDateOfEventsMonth(month);
    }

    public List<Event> getAllEventsList(){return mRepository.getAllEventsList();}
}