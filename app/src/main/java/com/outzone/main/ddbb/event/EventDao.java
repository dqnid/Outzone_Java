package com.outzone.main.ddbb.event;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    @Query("DELETE FROM eventos")
    void deleteAll();

    @Delete
    void deleteEvent(Event event);

    @Query("SELECT * from eventos LIMIT 1")
    Event[] getAnyEvent();

    @Query("SELECT * from eventos ORDER BY nombre_evento ASC")
    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * from eventos ORDER BY nombre_evento ASC")
    List<Event> getListEvents();

    @Query("SELECT * from eventos WHERE fecha=:date ORDER BY nombre_evento ASC")
    List<Event> getEventsDate(String date);

    @Query("SELECT * from eventos WHERE nombre_evento LIKE :keyword")
    List<Event> getEventsKeyWord(String keyword);

    @Query("SELECT fecha from eventos WHERE fecha LIKE :month")
    List<String> getDateOfEventsMonth(String month);

    @Query("SELECT * from eventos ORDER BY nombre_evento ASC")
    List<Event> getAllEventsList();
}