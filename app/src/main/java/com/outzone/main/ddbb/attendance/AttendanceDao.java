package com.outzone.main.ddbb.attendance;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Attendance att);

    @Query("DELETE FROM asistentes")
    void deleteAll();

    @Query("DELETE FROM asistentes WHERE dniAsistente=:dni and idEvento=:id")
    void deleteAttendance(String dni, String id);

    @Query("SELECT * FROM asistentes WHERE dniAsistente=:dni and idEvento=:id")
    Attendance getAttendanceMemberEvent(String dni, String id);

    @Query("SELECT * from asistentes LIMIT 1")
    Attendance[] getAnyAttendance();

    @Query("SELECT * from asistentes")
    List<Attendance> getAllAttendance();

    @Query("SELECT e.nombre_evento, e.fecha, m.nombre, m.apellido1, m.apellido2 from asistentes a,eventos e,miembros m where m.dni=a.dniAsistente and e.id=a.idEvento")
    List<AttendanceEventMember> getAttendanceEventMember();
}
