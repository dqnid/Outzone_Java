package com.outzone.main.ddbb.attendance;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * Tabla para relacionar eventos con miembros
 **/
@Entity(tableName = "asistentes", primaryKeys = {"dniAsistente","idEvento"})
public class Attendance {
    @NonNull
    @ColumnInfo(name = "dniAsistente")
    private String dni;

    @NonNull
    @ColumnInfo(name = "idEvento")
    private String id;

    //Dejo esto aquí porque solo en la relación dni evento hay un precio o coste, no es global al evento ni particular al asistente
    @NonNull
    @ColumnInfo(name = "costeAsistente")
    private float coste;

    @NonNull
    @ColumnInfo(name = "precioEvento")
    private float precio;

    public Attendance(@NonNull String dni, @NonNull String id, @NonNull float coste, @NonNull float precio)
    {
        this.dni = dni; this.id = id; this.coste = coste; this.precio = precio;
    }

    public float getCoste() {
        return coste;
    }
    public float getPrecio() {
        return precio;
    }
    @NonNull
    public String getDni() {
        return dni;
    }
    @NonNull
    public String getId() {
        return id;
    }
}
