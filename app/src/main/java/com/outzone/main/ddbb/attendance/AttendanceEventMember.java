package com.outzone.main.ddbb.attendance;

import androidx.room.ColumnInfo;

public class AttendanceEventMember {

    @ColumnInfo(name = "nombre_evento")
    String nEvent;
    @ColumnInfo(name = "fecha")
    String date;
    @ColumnInfo(name = "nombre")
    String nMember;
    @ColumnInfo(name = "apellido1")
    String ape1;
    @ColumnInfo(name = "apellido2")
    String ape2;

    public String getnMember() {
        return nMember;
    }

    public String getnEvent() {
        return nEvent;
    }

    public String getDate() {
        return date;
    }

    public String getApe2() {
        return ape2;
    }

    public String getApe1() {
        return ape1;
    }
}
