package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.exceptions.CredencialesInvalidasException;
import model.exceptions.DatosInvalidosException;
import model.exceptions.EmailDuplicadoException;
import model.exceptions.PerfilNoValidoException;
import model.exceptions.SesionNoIniciadaException;
import model.exceptions.UsuarioNoEncontradoException;

public class SistemaUsuariosService {

    private static final SistemaUsuariosService INSTANCE = new SistemaUsuariosService();

    private final List<String> perfilesTester = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();
    private Usuario usuarioLogueado;

    public static SistemaUsuariosService getInstance() {
        return INSTANCE;
    }

    private SistemaUsuariosService() {
        perfilesTester.add("Tester Junior");
        perfilesTester.add("Tester Senior");
        perfilesTester.add("Tester Líder");

        usuarios.add(new Tester("Marcos", "Campos", "test@test.com", "123456789", "Uruguay", "Tester Junior"));
        usuarios.add(new Admin("Diego", "De La Vega", "test2@test.com", "123456789", "Argentina"));
    }

    public boolean isAdminLogueado() {
        return usuarioLogueado != null && usuarioLogueado.puedeGestionarUsuarios();
    }

    public List<String> getPerfilesTester() {
        return Collections.unmodifiableList(perfilesTester);
    }

    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public String registrarAdministrador(String nombre, String apellido, String email, String contrasena, String repetirContrasena, String pais) {
        validarDatosBase(nombre, apellido, email, contrasena, pais);

        if (!contrasena.equals(repetirContrasena)) {
            throw new DatosInvalidosException("Las contraseñas no coinciden.");
        }

        if (usuarioExiste(email)) {
            throw new EmailDuplicadoException("El email ya está registrado.");
        }

        usuarios.add(new Admin(nombre, apellido, email, contrasena, pais));
        return "Registro exitoso.";
    }

    public String registrarTester(String nombre, String apellido, String email, String contrasena, String pais, String perfil) {
        validarDatosBase(nombre, apellido, email, contrasena, pais);

        if (perfil == null || perfil.trim().isEmpty()) {
            throw new DatosInvalidosException("El perfil del tester no puede estar vacío.");
        }

        if (!perfilesTester.contains(perfil)) {
            throw new PerfilNoValidoException("El perfil del tester no es válido.");
        }

        if (usuarioExiste(email)) {
            throw new EmailDuplicadoException("El email ya está registrado.");
        }

        usuarios.add(new Tester(nombre, apellido, email, contrasena, pais, perfil));
        return "Registro exitoso.";
    }

    public String iniciarSesion(String email, String contrasena) {
        validarEmailYContrasena(email, contrasena);

        Usuario usuario = obtenerUsuarioPorEmail(email);

        if (!usuario.puedeGestionarUsuarios()) {
            throw new PerfilNoValidoException("Perfil de usuario no administrador.");
        }

        if (!contrasena.equals(usuario.getContrasena())) {
            throw new CredencialesInvalidasException("Contraseña incorrecta.");
        }

        usuarioLogueado = usuario;
        return "Inicio de sesión exitoso.";
    }

    public String reiniciarContrasena(String email, String contrasena, String repetirContrasena) {
        validarEmailYContrasena(email, contrasena);

        if (!contrasena.equals(repetirContrasena)) {
            throw new DatosInvalidosException("Las contraseñas no coinciden.");
        }

        Usuario usuario = obtenerUsuarioPorEmail(email);
        if (!usuario.seLePuedeReiniciarContrasena()) {
            throw new PerfilNoValidoException("Perfil de usuario no administrador.");
        }

        usuario.setContrasena(contrasena);
        return "Contraseña actualizada correctamente.";
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return obtenerUsuarioPorEmail(email);
    }

    public String cerrarSesion() {
        usuarioLogueado = null;
        return "Cerrando sesión.";
    }

    public String obtenerDatosPersonales() {
        if (usuarioLogueado == null) {
            throw new SesionNoIniciadaException("No hay un usuario autenticado.");
        }

        return "Nombre: " + usuarioLogueado.getNombre() + "\n" +
                "Apellido: " + usuarioLogueado.getApellido() + "\n" +
                "Email: " + usuarioLogueado.getEmail() + "\n" +
                "País: " + usuarioLogueado.getPais() + "\n" +
                "Perfil: " + usuarioLogueado.getPerfil();
    }

    public String actualizarDatosPersonales(String nombre, String apellido, String email, String pais) {
        if (usuarioLogueado == null) {
            throw new SesionNoIniciadaException("No hay un usuario autenticado.");
        }

        validarDatosPersonales(nombre, apellido, email, pais);

        int indiceUsuario = indiceUsuarioPorEmail(email);
        if (indiceUsuario != -1 && usuarios.get(indiceUsuario) != usuarioLogueado) {
            throw new EmailDuplicadoException("El email ya está registrado.");
        }

        usuarioLogueado.setNombre(nombre.trim());
        usuarioLogueado.setApellido(apellido.trim());
        usuarioLogueado.setEmail(email.trim());
        usuarioLogueado.setPais(pais.trim());
        return "Datos personales actualizados correctamente.";
    }

    public String eliminarUsuario(String email) {
        if (usuarioLogueado == null) {
            throw new SesionNoIniciadaException("No hay un usuario autenticado.");
        }

        if (!usuarioLogueado.puedeGestionarUsuarios()) {
            throw new PerfilNoValidoException("Perfil de usuario no administrador.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new DatosInvalidosException("El email no puede estar vacío.");
        }

        Usuario usuario = obtenerUsuarioPorEmail(email);
        if (usuario.puedeGestionarUsuarios()) {
            throw new PerfilNoValidoException("No se puede eliminar un usuario con perfil administrador.");
        }

        usuarios.remove(usuario);
        return "Usuario eliminado correctamente.";
    }

    private boolean usuarioExiste(String email) {
        return indiceUsuarioPorEmail(email) != -1;
    }

    private int indiceUsuarioPorEmail(String email) {
        if (email == null) {
            return -1;
        }

        int i = 0;
        while (i < usuarios.size()) {
            Usuario usuario = usuarios.get(i);
            if (email.trim().equalsIgnoreCase(usuario.getEmail())) {
                return i;
            }
            i++;
        }

        return -1;
    }

    private void validarDatosBase(String nombre, String apellido, String email, String contrasena, String pais) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede estar vacío.");
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            throw new DatosInvalidosException("El apellido no puede estar vacío.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new DatosInvalidosException("El email no puede estar vacío.");
        }

        if (!email.contains("@")) {
            throw new DatosInvalidosException("Formato email incorrecto.");
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new DatosInvalidosException("La contraseña no puede estar vacía.");
        }

        if (contrasena.length() < 8) {
            throw new DatosInvalidosException("La contraseña debe tener al menos 8 caracteres.");
        }

        if (pais == null || pais.trim().isEmpty()) {
            throw new DatosInvalidosException("El país no puede estar vacío.");
        }
    }

    private void validarEmailYContrasena(String email, String contrasena) {
        if (email == null || email.trim().isEmpty()) {
            throw new DatosInvalidosException("El email no puede estar vacío.");
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new DatosInvalidosException("La contraseña no puede estar vacía.");
        }
    }

    private void validarDatosPersonales(String nombre, String apellido, String email, String pais) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede estar vacío.");
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            throw new DatosInvalidosException("El apellido no puede estar vacío.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new DatosInvalidosException("El email no puede estar vacío.");
        }

        if (!email.contains("@")) {
            throw new DatosInvalidosException("Formato email incorrecto.");
        }

        if (pais == null || pais.trim().isEmpty()) {
            throw new DatosInvalidosException("El país no puede estar vacío.");
        }
    }

    private Usuario obtenerUsuarioPorEmail(String email) {
        int indiceUsuario = indiceUsuarioPorEmail(email);
        if (indiceUsuario == -1) {
            throw new UsuarioNoEncontradoException("Usuario no existe.");
        }

        return usuarios.get(indiceUsuario);
    }
}