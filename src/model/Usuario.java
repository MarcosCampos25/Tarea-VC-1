package model;

public class Usuario {
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private String pais;

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getPais() {
        return pais;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Usuario(String nombre, String apellido, String email, String contrasena, String pais) {
        setNombre(nombre);
        setApellido(apellido);
        setContrasena(contrasena);
        setEmail(email);
        setPais(pais);
    }

}

