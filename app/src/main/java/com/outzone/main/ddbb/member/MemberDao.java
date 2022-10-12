package com.outzone.main.ddbb.member;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Member member);

    @Query("DELETE FROM miembros")
    void deleteAll();

    @Delete
    void deleteMember(Member member);

    @Query("SELECT * from miembros LIMIT 1")
    Member[] getAnyMember();

    @Query("SELECT * from miembros ORDER BY nombre ASC")
    LiveData<List<Member>> getAllMembers();
}
