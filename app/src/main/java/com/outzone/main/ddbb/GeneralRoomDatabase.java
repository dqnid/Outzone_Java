package com.outzone.main.ddbb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.outzone.main.ddbb.attendance.Attendance;
import com.outzone.main.ddbb.attendance.AttendanceDao;
import com.outzone.main.ddbb.event.Event;
import com.outzone.main.ddbb.event.EventDao;
import com.outzone.main.ddbb.member.Member;
import com.outzone.main.ddbb.member.MemberDao;
import com.outzone.main.tools.DateManager;

@Database(entities = {Member.class, Attendance.class, Event.class}, version = 5, exportSchema = false)
public abstract class GeneralRoomDatabase extends RoomDatabase {
    /*
     * Constructor para el Dao, no crearé objetos GeneralDao si no que accederé a este
     * */
    public abstract MemberDao memberDao();
    public abstract EventDao eventDao();
    public abstract AttendanceDao attendanceDao();
    /*
     * Código para implementar como singleton
     */
    private static GeneralRoomDatabase INSTANCE;
    public static GeneralRoomDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null) {
            synchronized (GeneralRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Creación de la base de datos
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GeneralRoomDatabase.class, "generaldatabase")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    /*
     * Este callback se llama cuando se abre la base de datos
     * En este caso particular lo uso para rellenar la base de datos con los datos iniciales
     *  en caso de que esté vacía.
     * */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new GeneralRoomDatabase.PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MemberDao mMemberDao;
        private final EventDao mEventDao;
        private final AttendanceDao mAttendanceDao;

        // Initial data set
        private static String [] dni = {"70939119N","70459129J", "70159178F"};
        private static String [] nombres = {"Daniel","Aitor","Jorge"};
        private static String [] ape1 =  {"Heras", "González", "Pérez"};
        private static String [] ape2 = {"Quesada","Gómez","Prieto"};
        private static String [] fecha = {DateManager.formatDate(1999,9,30),DateManager.formatDate(1970,4,10),DateManager.formatDate(2009,1,2)};

        // Event initial
        private static String [] id = {"uno","dos","tres","cuatro"};
        private static String [] fechas = {DateManager.formatDate(2022,8,17),DateManager.formatDate(2022,8,17),DateManager.formatDate(2022,8,17),DateManager.formatDate(2022,8,17)};
        private static String [] nombres2 = {"Evento 1","Evento 2", "Evento 3", "Evento 4"};


        PopulateDbAsync(GeneralRoomDatabase db) {
            mMemberDao = db.memberDao();
            mEventDao = db.eventDao();
            mAttendanceDao = db.attendanceDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Miembros
            if (mMemberDao.getAnyMember().length < 1) {
                for (int i = 0; i <= dni.length - 1; i++) {
                    Member member = new Member(dni[i],nombres[i], ape1[i], ape2[i], fecha[i]);
                    mMemberDao.insert(member);
                }
            }

            //Eventos
            if (mEventDao.getAnyEvent().length < 1) {
                for (int i = 0; i <= id.length - 1; i++) {
                    Event event= new Event(id[i],fechas[i],nombres2[i], null, null, null);
                    mEventDao.insert(event);
                }
            }

            //Asistencias
            if (mAttendanceDao.getAnyAttendance().length < 1) {
                for (int i = 0; i <= dni.length - 1; i++) {
                    Attendance attendance= new Attendance(dni[i],id[i],0,0);
                    mAttendanceDao.insert(attendance);
                }
            }
            return null;
        }
    }
}