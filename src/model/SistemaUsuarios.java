package model;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class SistemaUsuarios {

    static List<String> perfilesTester = new ArrayList<>();
    static List<Usuario> listaUsuario = new ArrayList<>();
    static boolean adminLogged = false;
    static boolean salir = false;

    public static void main(String[] args) {
        perfilesTester.add("Tester Junior");
        perfilesTester.add("Tester Senior");
        perfilesTester.add("Tester Líder");

        listaUsuario.add(new Tester("Marcos", "Campos", "test@test.com", "123456789", "Uruguay", "Tester Junior"));
        listaUsuario.add(new Admin("Diego", "De La Vega", "test2@test.com", "123456789", "Argentina"));

        while (!salir) {
            if (adminLogged) {
                mostrarMenuLogueado();
            } else {
                mostrarMenuDeslogueado();
            }
        }
    }

    public static int usuarioEstaRegistrado(String email) {
        int i = 0;
        while (i < listaUsuario.size()) {
            Usuario usuario = listaUsuario.get(i);
            if (email != null && email.equalsIgnoreCase(usuario.getEmail())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static void registro() {

        Scanner scan = new Scanner(System.in);
        System.out.println("== Registro de usuario ==");
        System.out.println("Ingrese nombre:");
        String nombre = scan.nextLine();

        while (nombre == null || nombre.equals("")) {
            System.out.println("El nombre no puede estar vacío. Ingrese nuevamente el nombre:");
            nombre = scan.nextLine();
        }

        System.out.println("Ingrese apellido:");
        String apellido = scan.nextLine();
        while (apellido == null || apellido.equals("")) {
            System.out.println("El apellido no puede estar vacío. Ingrese nuevamente el apellido:");
            apellido = scan.nextLine();
        }

        System.out.println("Ingrese email:");
        String email = scan.nextLine();

        while (email == null || !email.contains("@") || usuarioEstaRegistrado(email) != -1) {
            if (usuarioEstaRegistrado(email) != -1) {
                System.out.println("El email ya está registrado, ingrese nuevamente el email:");
            } else {
                System.out.println("Formato email incorrecto, ingrese nuevamente el email:");
            }
            email = scan.nextLine();
        }

        System.out.println("Ingrese país:");
        String pais = scan.nextLine();
        while (pais == null || pais.equals("")) {
            System.out.println("El país no puede estar vacío. Ingrese nuevamente el país:");
            pais = scan.nextLine();
        }

        System.out.println("Ingrese contraseña:");
        String contrasena = scan.nextLine();
        while (contrasena == null || contrasena.equals("") || contrasena.length() < 8) {
            System.out.println("La contraseña debe tener al menos 8 caracteres. Ingrese nuevamente la contraseña:");
            contrasena = scan.nextLine();
        }

        if (adminLogged) {

            String perfil = seleccionarPerfilTester(scan);
            listaUsuario.add(new Tester(nombre, apellido, email, contrasena, pais, perfil));
            System.out.println("Registro exitoso.");
            return;
        }

        listaUsuario.add(new Admin(nombre, apellido, email, contrasena, pais));
        System.out.println("Registro exitoso.");
    }

    public static void login() {
        Scanner scan = new Scanner(System.in);
        System.out.println("== Inicio de sesión ==");
        System.out.print("Ingrese email: ");
        String emailIngresado = scan.nextLine();
        System.out.print("Ingrese contraseña: ");
        String contrasenaIngresada = scan.nextLine();

        int i = usuarioEstaRegistrado(emailIngresado); // devuelve -1 si no se encuentra, si se encuentre devuelve el indice

        if (i == -1) {
            System.out.println("El usuario no está registrado.");
            return;
        }

        if (!listaUsuario.get(i).puedeGestionarUsuarios()) {
            System.out.println("Perfil de usuario no administrador.");
            return;
        }

        if (!contrasenaIngresada.equals(listaUsuario.get(i).getContrasena())) {
            System.out.println("Contraseña incorrecta.");
            return;
        }

        adminLogged = true;
        System.out.println("Inicio de sesión exitoso.");

    }

    public static void listarUsuarios () {
        System.out.println("== Lista de usuarios ==");
        if (listaUsuario.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        for (Usuario usuario : listaUsuario) {
            mostrarUsuario(usuario);
        }
    }

    public static void buscarUsuario() {
        Scanner scan = new Scanner(System.in);
        System.out.println("== Búsqueda de usuario ==");
        System.out.println("Ingrese el email del usuario:");
        String email = scan.nextLine();

        while (email.isEmpty()) {
            System.out.println("El email no puede estar vacío. Ingrese el email del usuario:");
            email = scan.nextLine();
        }

        int i = usuarioEstaRegistrado(email);
        if (i == -1) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        Usuario usuario = listaUsuario.get(i);
        System.out.println("Usuario encontrado:");
        mostrarUsuario(usuario);
    }

    public static void mostrarUsuario(Usuario usuario) {
        System.out.println(
                usuario.getNombre() + " | " +
                        usuario.getApellido() + " | " +
                        usuario.getEmail() + " | " +
                        usuario.getPais() + " | " +
                        usuario.getPerfil()
        );
    }

    public static String seleccionarPerfilTester(Scanner scan) {
        String opcion;

        do {
            System.out.println("Seleccione el perfil del tester:");
            for (int i = 0; i < perfilesTester.size(); i++) {
                System.out.println((i + 1) + "- " + perfilesTester.get(i));
            }

            System.out.print("Opción: ");
            opcion = scan.nextLine();
            if (!opcionEsValida(opcion)) {
                System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (!opcionEsValida(opcion));

        return perfilesTester.get(Integer.parseInt(opcion) - 1);
    }

    public static boolean opcionEsValida(String opcion) {
        return "1".equals(opcion) || "2".equals(opcion) || "3".equals(opcion);
    }

    public static void mostrarMenuLogueado() {
        String opcion;
        System.out.println("\n== Menú (Administrador logueado) ==");
        System.out.println("1- Crear Usuario");
        System.out.println("2- Listar Usuarios");
        System.out.println("3- Buscar Usuario");
        System.out.println("4- Cerrar sesión");
        System.out.println("5- Salir");
        Scanner scan = new Scanner(System.in);
        System.out.print("Opción: ");
        opcion = scan.nextLine();
        switch (opcion){
            case "1":
                SistemaUsuarios.registro();
                break;
            case "2":
                SistemaUsuarios.listarUsuarios();
                break;
            case "3":
                SistemaUsuarios.buscarUsuario();
                break;
            case "4":
                System.out.println("Cerrando sesión.");
                adminLogged = false;
                break;
            case "5":
                System.out.println("Saliendo...");
                salir = true;
                break;
            default:
                System.out.println("Opción inválida.");
                break;
        }
    }

    public static void mostrarMenuDeslogueado() {
        String opcion;
        System.out.println("\n== Menú principal ==");
        System.out.println("1- Login");
        System.out.println("2- Crear cuenta Administrador");
        System.out.println("3- Salir");
        Scanner scan = new Scanner(System.in);
        System.out.print("Opción: ");
        opcion = scan.nextLine();
        switch (opcion){
            case "1":
                SistemaUsuarios.login();
                break;
            case "2":
                SistemaUsuarios.registro();
                break;
            case "3":
                System.out.println("Saliendo...");
                salir = true;
                break;
            default:
                System.out.println("Opción inválida.");
                break;
        }
    }



}
