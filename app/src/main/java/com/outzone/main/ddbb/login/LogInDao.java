package com.outzone.main.ddbb.login;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface LogInDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LogIn login);

    @Query("DELETE FROM login")
    void deleteAll();

    @Delete
    void deleteLogIn(LogIn login);

    @Query("SELECT * from login LIMIT 1")
    LogIn[] getAnyLogIn();

    @Query("SELECT password from login WHERE user=:user")
    String getHash(String user);

    @Query("SELECT rol from login WHERE user=:user")
    int getRol(String user);

    @Query("SELECT members from login WHERE user=:user")
    String getMembers(String user);
}
