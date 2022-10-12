package com.outzone.main.ddbb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.outzone.main.ddbb.login.LogIn;
import com.outzone.main.ddbb.login.LogInDao;
import com.outzone.main.tools.EncriptionManager;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Database(entities = {LogIn.class}, version = 4, exportSchema = false)
public abstract class LogInRoomDatabase extends RoomDatabase {
    /*
     * Constructor para el Dao, no crearé objetos GeneralDao si no que accederé a este
     * */
    public abstract LogInDao loginDao();
    /*
     * Código para implementar como singleton
     */
    private static LogInRoomDatabase INSTANCE;
    public static LogInRoomDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null) {
            synchronized (LogInRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Creación de la base de datos
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    LogInRoomDatabase.class, "logindatabase")
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
    private static Callback sRoomDatabaseCallback = new Callback(){
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new LogInRoomDatabase.PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final LogInDao mDao;

        private static String [] usr = {"uno","dos","tres","Uno"};
        private static String [] passwd;
        private static int [] roles = {1,2,3,4};
        private static String [] members = {"70939119N","70459129J", "70159178F",""};

        static {
                passwd = new String[]{EncriptionManager.getHashString("uno"),
                        EncriptionManager.getHashString("dos"),
                       EncriptionManager.getHashString("tres"), EncriptionManager.getHashString("dos")};
        }

        PopulateDbAsync(LogInRoomDatabase db) {
            mDao = db.loginDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            if (mDao.getAnyLogIn().length < 1) {
                for (int i = 0; i <= usr.length - 1; i++) {
                    LogIn login = new LogIn(usr[i],passwd[i],roles[i],members[i],members[(int) (Math.random()%usr.length)]);
                    mDao.insert(login);
                }
            }
            return null;
        }
    }
}