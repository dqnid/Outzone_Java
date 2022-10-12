package com.outzone.main.tools;

public class SessionHash {
    private String hash;
    private String user;
    /**
     * Roles:
     *  - 1 es el mínimo, sería un usuario normal
     *  - 2 será un usuario avanzado, con acceso a la creación de eventos y listado de miembros
     *  - 3 será el máximo y podrá modificar usuarios y miembros
     **/
    private int rol;
    private String members;

    private static SessionHash instancia;

    private SessionHash(String user, String hash, int rol, String members){
        this.user = user;
        this.hash = hash;
        this.rol = rol;
        this.members = members;
    }

    public static SessionHash getSesionHash(String user, String hash, int rol, String members){
        if (instancia == null){
            instancia = new SessionHash(user, hash, rol, members);
        }
        return instancia;
    }

    public String getHash(){
        return hash;
    }
    public int getRol() {
        return rol;
    }
    public String getUser() {
        return user;
    }
    public String getMembers() {
        return members;
    }
}
