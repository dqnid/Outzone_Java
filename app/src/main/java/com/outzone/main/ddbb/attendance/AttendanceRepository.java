package com.outzone.main.ddbb.attendance;

import android.app.Activity;
import android.app.Application;

import com.outzone.main.ddbb.GeneralRoomDatabase;

import java.util.List;

public class AttendanceRepository {
    private AttendanceDao mAttDao;

    public AttendanceRepository(Application application){
        GeneralRoomDatabase db = GeneralRoomDatabase.getDatabase(application);
        mAttDao = db.attendanceDao();
    }

    /**
     * Por ahora todas las consultas en primer plano
     * pero mantengo el repositorio para facilitar futuros cambios
     **/
    public void insertAttendee(Attendance att){
        mAttDao.insert(att);
    }

    public void deleteAll(){
        mAttDao.deleteAll();
    }

    public void deleteAttendee(String dni, String id){
        mAttDao.deleteAttendance(dni,id);
    }

    public Attendance isAttending(String dni, String id){
        return mAttDao.getAttendanceMemberEvent(dni,id);
    }

    public Attendance[] getAnyAttendee(){
        return mAttDao.getAnyAttendance();
    }

    public List<Attendance> getAllAttendance(){
        return  mAttDao.getAllAttendance();
    }

    public List<AttendanceEventMember> getAttendanceEventMember(){
        return mAttDao.getAttendanceEventMember();
    }
}
