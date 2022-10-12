package com.outzone.main.ddbb.login;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "login")
public class LogIn {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user")
    private String usr;

    @NonNull
    @ColumnInfo(name = "password")
    private String passwd;

    @NonNull
    @ColumnInfo(name = "rol")
    private int rol;

    //dni para asociar login con miembros (787889M;78979800J;...)
    //El primero es el propio: puedo tener logins fantasma
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "members")
    private String members;

    public LogIn(@NonNull String usr, @NonNull String passwd, @NonNull int rol, String id,String members){
        this.usr = usr;
        this.passwd = passwd;
        this.rol = rol;
        this.id = id;
        this.members = members;
    }

    public String getPasswd() {
        return passwd;
    }
    public String getUsr() {
        return usr;
    }
    public int getRol() {
        return rol;
    }
    public String getMembers() {
        return members;
    }

    public String getId() {
        return id;
    }
}
