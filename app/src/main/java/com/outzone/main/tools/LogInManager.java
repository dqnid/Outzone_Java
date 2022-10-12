package com.outzone.main.tools;

import android.content.Context;

import com.outzone.main.ddbb.LogInRoomDatabase;
import com.outzone.main.ddbb.login.LogIn;
import com.outzone.main.ddbb.login.LogInDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

public class LogInManager {
    private static LogInDao mLogInDao;
    private static Context context;

    public LogInManager(Context context) {
        this.context = context;
    }

    public static String getAccess(String usr, String passwd) throws NoSuchAlgorithmException {
        LogInRoomDatabase db = LogInRoomDatabase.getDatabase(context);
        mLogInDao = db.loginDao();
        String str = mLogInDao.getHash(usr);
        String hashedInput = EncriptionManager.byteToString(EncriptionManager.DigestSHA256(passwd));
        if (hashedInput.equals(str))
            return hashedInput;
        return null;
    }

    /**
     * Entiendo que es un error de seguridad, en caso de que se filtrasen los hash se filtraría el acceso al sistema
     * por lo que se pierde gran parte de la funcionalidad del hash.
     * Pero es la única manera que se me ocurre sin cifrar la contraseña como tal y almacenarla así.
     **/
    public static String getAccessWithHash(String usr, String hash) throws NoSuchAlgorithmException {
        LogInRoomDatabase db = LogInRoomDatabase.getDatabase(context);
        mLogInDao = db.loginDao();
        String str = mLogInDao.getHash(usr);
        if (hash.equals(str))
            return hash;
        return null;
    }

    public static int getUsrRol(String usr) {
        LogInRoomDatabase db = LogInRoomDatabase.getDatabase(context);
        mLogInDao = db.loginDao();
        int rol = mLogInDao.getRol(usr);
        return rol;
    }

    public static String getUsrMembers(String usr) {
        LogInRoomDatabase db = LogInRoomDatabase.getDatabase(context);
        mLogInDao = db.loginDao();
        String members = mLogInDao.getMembers(usr);
        return members;
    }

    /**
     * Serverside
     **/
    public static boolean singUp(LogIn usr) {
        if (usr == null)
            return false;

        String key = EncriptionManager.getHashString(usr.getPasswd());
        LogIn encryptedUsr = new LogIn(
                EncriptionManager.byteToString(EncriptionManager.BlowFishEncrypt(usr.getUsr(), key)),
                EncriptionManager.byteToString(EncriptionManager.BlowFishEncrypt(usr.getPasswd(), key)),
                usr.getRol(),
                EncriptionManager.byteToString(EncriptionManager.BlowFishEncrypt(usr.getMembers(), key)),
                EncriptionManager.byteToString(EncriptionManager.BlowFishEncrypt(usr.getMembers(), key))
        );

        LogInRoomDatabase db = LogInRoomDatabase.getDatabase(context);
        mLogInDao = db.loginDao();
        mLogInDao.insert(encryptedUsr);
        return true;
    }

    public static boolean singIn(String usr, String hash) {
        if (usr == null || hash == null)
            return false;
        //Encripto el usuario y busco con ello en la base de datos
        String primaryKey = EncriptionManager.byteToString(EncriptionManager.BlowFishEncrypt(usr, hash));
        LogInRoomDatabase db = LogInRoomDatabase.getDatabase(context);
        mLogInDao = db.loginDao();
        String encryptedHash = mLogInDao.getHash(primaryKey);
        String desencryptedHash = EncriptionManager.byteToString(EncriptionManager.BlowFishDecrypt(encryptedHash.getBytes(), hash));
        if (desencryptedHash.equals(hash))
            return true;
        return false;
    }

    //Pacto claves con el servidor
    //Envío los datos de login cifrados
    public static SessionHash getAccessServer(String dir, String usr, String passwd) {
        if (usr == null || passwd == null || dir == null || usr.isEmpty() || passwd.isEmpty() || dir.isEmpty()) {
            return null;
        }
        SessionHash session;
        JSONObject sessionData = getServerResponse(dir, usr, passwd);
        if (sessionData == null)
            return null;
        String hash = EncriptionManager.getHashString(passwd);
        try {
            session = SessionHash.getSesionHash(usr, hash, sessionData.getInt("rol"), sessionData.getString("members"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getServerResponse(String dir, String usr, String hash) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        JSONObject finalObject = null;
        try {
            URL url = new URL(dir + "?user=" + usr + "&" + "?passwd=" + hash);
            connection = (HttpURLConnection) url.openConnection();

            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();

            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String finalJson = buffer.toString();

            JSONObject parentObject = new JSONObject();
            JSONArray parentArray = parentObject.getJSONArray("");
            finalObject = parentArray.getJSONObject(0);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
        return finalObject;
    }
}