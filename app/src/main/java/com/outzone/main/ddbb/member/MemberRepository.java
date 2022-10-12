package com.outzone.main.ddbb.member;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.outzone.main.ddbb.GeneralRoomDatabase;

import java.util.List;

/**
 * El repositorio permite acceder a varios recursos, varias fuentes de datos
 * En este caso accedemos al Dao local, pero podríamos añadir funcionalidad para acceder aun servidor
 **/
public class MemberRepository {

    private MemberDao mMemberDao;
    private LiveData<List<Member>> mAllMembers;

    MemberRepository(Application application) {
        GeneralRoomDatabase db = GeneralRoomDatabase.getDatabase(application);
        mMemberDao = db.memberDao();
        mAllMembers = mMemberDao.getAllMembers();
    }

    LiveData<List<Member>> getAllMembers() {
        return mAllMembers;
    }

    public void insert(Member member) {
        new MemberRepository.insertAsyncTask(mMemberDao).execute(member);
    }

    public void deleteAll() {
        new MemberRepository.deleteAllMembersAsyncTask(mMemberDao).execute();
    }

    public void deleteMember(Member member) {
        new MemberRepository.deleteMemberAsyncTask(mMemberDao).execute(member);
    }

    /**
     * Inserta usuario en la base de datos.
     */
    private static class insertAsyncTask extends AsyncTask<Member, Void, Void> {

        private MemberDao mAsyncTaskDao;

        insertAsyncTask(MemberDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Member... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Elimina todas las entradas de la tabla, pero no la tabla como tal
     */
    private static class deleteAllMembersAsyncTask extends AsyncTask<Void, Void, Void> {
        private MemberDao mAsyncTaskDao;

        deleteAllMembersAsyncTask(MemberDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Eliminar el usuario parámetro de la tabla.
     */
    private static class deleteMemberAsyncTask extends AsyncTask<Member, Void, Void> {
        private MemberDao mAsyncTaskDao;

        deleteMemberAsyncTask(MemberDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Member... params) {
            mAsyncTaskDao.deleteMember(params[0]);
            return null;
        }
    }
}
