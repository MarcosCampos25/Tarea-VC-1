package model;

public class Tester extends Usuario {
    private String perfil;

    public Tester(String nombre, String apellido, String email, String contrasena, String pais, String perfil) {
        super(nombre, apellido, email, contrasena, pais);
        this.perfil = perfil;
    }

}
