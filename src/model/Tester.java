package model;

public class Tester extends Usuario {

    public Tester(String nombre, String apellido, String email, String contrasena, String pais, String perfil) {
        super(nombre, apellido, email, contrasena, pais, perfil);
    }

    @Override
    public boolean puedeGestionarUsuarios() {
        return false;
    }

}
