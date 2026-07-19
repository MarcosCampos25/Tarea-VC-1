package model;

import java.util.List;
import java.util.Scanner;

import model.exceptions.SistemaUsuariosException;
import model.exceptions.UsuarioNoEncontradoException;

public class SistemaUsuarios {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final SistemaUsuariosService servicio = SistemaUsuariosService.getInstance();
    private static boolean salir = false;

    public static void main(String[] args) {
        while (!salir) {
            if (servicio.isAdminLogueado()) {
                mostrarMenuLogueado();
            } else {
                mostrarMenuDeslogueado();
            }
        }
    }

    public static void mostrarMenuLogueado() {
        System.out.println("\n== Menú (Administrador logueado) ==");
        System.out.println("1- Crear Usuario Tester");
        System.out.println("2- Listar Usuarios");
        System.out.println("3- Buscar Usuario");
        System.out.println("4- Ver y editar datos personales");
        System.out.println("5- Reiniciar Contraseña");
        System.out.println("6- Eliminar Usuario");
        System.out.println("7- Cerrar sesión");
        System.out.println("8- Salir");

        switch (leerOpcionMenu()) {
            case "1":
                registrarTester();
                break;
            case "2":
                listarUsuarios();
                break;
            case "3":
                buscarUsuario();
                break;
            case "4":
                verYEditarDatosPersonales();
                break;
            case "5":
                reiniciarContrasena();
                break;
            case "6":
                eliminarUsuario();
                break;
            case "7":
                System.out.println(servicio.cerrarSesion());
                break;
            case "8":
                System.out.println("Saliendo...");
                salir = true;
                break;
            default:
                System.out.println("Opción inválida.");
                break;
        }
    }

    public static void mostrarMenuDeslogueado() {
        System.out.println("\n== Menú principal ==");
        System.out.println("1- Login");
        System.out.println("2- Crear cuenta Administrador");
        System.out.println("3- Reiniciar Contraseña");
        System.out.println("4- Salir");

        switch (leerOpcionMenu()) {
            case "1":
                iniciarSesion();
                break;
            case "2":
                registrarAdministrador();
                break;
            case "3":
                reiniciarContrasena();
                break;
            case "4":
                System.out.println("Saliendo...");
                salir = true;
                break;
            default:
                System.out.println("Opción inválida.");
                break;
        }
    }

    private static void registrarAdministrador() {
        System.out.println("== Registro de administrador ==");
        try {
            String nombre = leerTextoNoVacio("Ingrese nombre: ", "El nombre no puede estar vacío.");
            String apellido = leerTextoNoVacio("Ingrese apellido: ", "El apellido no puede estar vacío.");
            String email = leerTextoNoVacio("Ingrese email: ", "El email no puede estar vacío.");
            String pais = leerTextoNoVacio("Ingrese país: ", "El país no puede estar vacío.");
            String contrasena = leerTextoNoVacio("Ingrese contraseña: ", "La contraseña no puede estar vacía.");
            String repetirContrasena = leerTextoNoVacio("Repita la contraseña: ", "La repetición no puede estar vacía.");

            System.out.println(servicio.registrarAdministrador(nombre, apellido, email, contrasena, repetirContrasena, pais));
        } catch (SistemaUsuariosException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void registrarTester() {
        System.out.println("== Registro de usuario tester ==");
        try {
            String nombre = leerTextoNoVacio("Ingrese nombre: ", "El nombre no puede estar vacío.");
            String apellido = leerTextoNoVacio("Ingrese apellido: ", "El apellido no puede estar vacío.");
            String email = leerTextoNoVacio("Ingrese email: ", "El email no puede estar vacío.");
            String pais = leerTextoNoVacio("Ingrese país: ", "El país no puede estar vacío.");
            String contrasena = leerTextoNoVacio("Ingrese contraseña: ", "La contraseña no puede estar vacía.");
            String perfil = seleccionarPerfilTester();

            System.out.println(servicio.registrarTester(nombre, apellido, email, contrasena, pais, perfil));
        } catch (SistemaUsuariosException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void iniciarSesion() {
        System.out.println("== Inicio de sesión ==");
        try {
            String email = leerTextoNoVacio("Ingrese email: ", "El email no puede estar vacío.");
            String contrasena = leerTextoNoVacio("Ingrese contraseña: ", "La contraseña no puede estar vacía.");

            System.out.println(servicio.iniciarSesion(email, contrasena));
        } catch (SistemaUsuariosException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void reiniciarContrasena() {
        System.out.println("== Reinicio de contraseña ==");
        try {
            String email = leerTextoNoVacio("Ingrese el email del usuario: ", "El email no puede estar vacío.");
            String contrasena = leerTextoNoVacio("Ingrese la nueva contraseña: ", "La contraseña no puede estar vacía.");
            String repetirContrasena = leerTextoNoVacio("Repita la nueva contraseña: ", "La repetición no puede estar vacía.");

            System.out.println(servicio.reiniciarContrasena(email, contrasena, repetirContrasena));
        } catch (SistemaUsuariosException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void listarUsuarios() {
        System.out.println("== Lista de usuarios ==");
        List<Usuario> usuarios = servicio.listarUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        for (Usuario usuario : usuarios) {
            mostrarUsuario(usuario);
        }
    }

    private static void buscarUsuario() {
        System.out.println("== Búsqueda de usuario ==");
        try {
            String email = leerTextoNoVacio("Ingrese el email del usuario: ", "El email no puede estar vacío.");

            Usuario usuario = servicio.buscarUsuarioPorEmail(email);
            System.out.println("Usuario encontrado:");
            mostrarUsuario(usuario);
        } catch (UsuarioNoEncontradoException ex) {
            System.out.println(ex.getMessage());
        } catch (SistemaUsuariosException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void verYEditarDatosPersonales() {
        while (true) {
            try {
                System.out.println("== Mis datos personales ==");
                System.out.println(servicio.obtenerDatosPersonales());
                System.out.println("1- Volver");
                System.out.println("2- Editar");

                String opcion = leerOpcionMenu();
                if ("1".equals(opcion)) {
                    return;
                }

                if ("2".equals(opcion)) {
                    String nombre = leerTextoNoVacio("Ingrese nuevo nombre: ", "El nombre no puede estar vacío.");
                    String apellido = leerTextoNoVacio("Ingrese nuevo apellido: ", "El apellido no puede estar vacío.");
                    String email = leerTextoNoVacio("Ingrese nuevo email: ", "El email no puede estar vacío.");
                    String pais = leerTextoNoVacio("Ingrese nuevo país: ", "El país no puede estar vacío.");

                    System.out.println(servicio.actualizarDatosPersonales(nombre, apellido, email, pais));
                } else {
                    System.out.println("Opción inválida.");
                }
            } catch (SistemaUsuariosException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void eliminarUsuario() {
        System.out.println("== Eliminar usuario ==");
        try {
            String email = leerTextoNoVacio("Ingrese el email del usuario a eliminar: ", "El email no puede estar vacío.");

            System.out.println(servicio.eliminarUsuario(email));
        } catch (SistemaUsuariosException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void mostrarUsuario(Usuario usuario) {
        System.out.println(
                usuario.getNombre() + " | " +
                        usuario.getApellido() + " | " +
                        usuario.getEmail() + " | " +
                        usuario.getPais() + " | " +
                        usuario.getPerfil()
        );
    }

    private static String seleccionarPerfilTester() {
        List<String> perfilesTester = servicio.getPerfilesTester();
        String opcion;

        do {
            System.out.println("Seleccione el perfil del tester:");
            for (int i = 0; i < perfilesTester.size(); i++) {
                System.out.println((i + 1) + "- " + perfilesTester.get(i));
            }

            opcion = leerOpcionMenu();
            if (!opcionEsValida(opcion, perfilesTester.size())) {
                System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (!opcionEsValida(opcion, perfilesTester.size()));

        return perfilesTester.get(Integer.parseInt(opcion) - 1);
    }

    private static boolean opcionEsValida(String opcion, int cantidadOpciones) {
        if (opcion == null) {
            return false;
        }

        try {
            int valor = Integer.parseInt(opcion);
            return valor >= 1 && valor <= cantidadOpciones;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static String leerOpcionMenu() {
        System.out.print("Opción: ");
        String opcion = SCANNER.nextLine();
        return opcion == null ? "" : opcion.trim();
    }

    private static String leerTextoNoVacio(String mensaje, String mensajeError) {
        String valor;

        do {
            System.out.print(mensaje);
            valor = SCANNER.nextLine();

            if (valor == null || valor.trim().isEmpty()) {
                System.out.println(mensajeError);
            }
        } while (valor == null || valor.trim().isEmpty());

        return valor.trim();
    }
}
