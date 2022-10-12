package com.outzone.main.ddbb.member;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "miembros")
public class Member {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "dni")
    private String dni;

    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    @NonNull
    @ColumnInfo(name="apellido1")
    private String apellido1;

    @ColumnInfo(name = "apellido2")
    private String apellido2;

    @NonNull
    @ColumnInfo(name = "nacimiento")
    private String fecha;

    public Member(@NonNull String dni, @NonNull String nombre, @NonNull String apellido1, String apellido2, @NonNull String fecha)
    {
        this.dni = dni; this.nombre = nombre; this.apellido1 = apellido1; this.apellido2 = apellido2; this.fecha = fecha;
    }

    public String getDni(){ return dni; }
    public String getNombre(){ return nombre;}
    public String getApellido1(){ return apellido1; }
    public String getApellido2(){ return apellido2; }
    public String getFecha(){ return fecha; }
    public String getMember(){ return String.format("%s %s, %s - %s", apellido1, apellido2, nombre, fecha.toString()); }
}
