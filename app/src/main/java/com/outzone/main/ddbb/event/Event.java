package com.outzone.main.ddbb.event;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "eventos")
public class Event {
    //TODO: clave con long
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "fecha")
    private String date;

    @NonNull
    @ColumnInfo(name = "nombre_evento")
    private String name;

    @ColumnInfo(name = "decripcion")
    private String description;

    @ColumnInfo(name = "referencia")
    private String url;

    @ColumnInfo(name = "ubicacion")
    private String location;

    public Event(@NonNull String id,@NonNull String date, @NonNull String name, String description,String url,String location)
    {
        if (id == "0")
            this.id = String.format("%s_%s", name.replaceAll("\\s",""), date.toString());
        else
            this.id = id;
        this.date = date; this.name = name; this.description = description; this.url = url; this.location = location;
    }

    @NonNull
    public String getId(){
        return id;
    }
    @NonNull
    public String getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    public String getLocation() {
        return location;
    }
    @NonNull
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public String getEvent(){return String.format("%s - %s", name, date);}
}
