package model;

import java.util.Scanner;

public class SistemaUsuarios {
    static Usuario[] listaUsuarios = new Usuario[10];

    public static void main(String[] args) {
        listaUsuarios[0] = new Usuario("Marcos", "Campos", "test@test.com", "123456789", "Uruguay");
        listaUsuarios[1] = new Usuario("Diego", "De La Vega", "test2@test.com", "123456789", "Argentina");

        String opcion;
        do {
            System.out.println("\nElija una opción");
            System.out.println("1- Login");
            System.out.println("2- Registro");
            System.out.println("3- Salir");
            Scanner scan = new Scanner(System.in);
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
                    break;
                default:
                    System.out.println("Opción invalida");
                    break;
            }
        } while (!opcion.equals("3"));

    }

    public static int usuarioEstaRegistrado(String email) {
        int i = 0;
        while (i < listaUsuarios.length && listaUsuarios[i] != null) {
            if (email != null && email.equalsIgnoreCase(listaUsuarios[i].getEmail())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static void registro() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Ingrese nombre:");
        String nombre = scan.nextLine();

        while (nombre == null || nombre.equals("")) {
            System.out.println("No supera la cantidad minima de caracteres, ingrese nuevamente el nombre:");
            nombre = scan.nextLine();
        }

        System.out.println("Ingrese apellido:");
        String apellido = scan.nextLine();
        while (apellido == null || apellido.equals("")) {
            System.out.println("No supera la cantidad minima de caracteres, ingrese nuevamente el apellido:");
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
            System.out.println("No supera la cantidad minima de caracteres, ingrese nuevamente el pais:");
            pais = scan.nextLine();
        }

        System.out.println("Ingrese contraseña:");
        String contrasena = scan.nextLine();
        while (contrasena == null || contrasena.equals("") || contrasena.length() < 8) {
            System.out.println("No supera la cantidad minima de caracteres, ingrese nuevamente la contraseña:");
            contrasena = scan.nextLine();
        }


        // Se han obtenido los datos de usuario necesarios
        // Dado que tengo un array limitado necesito saber si hay "espacio" para agregar otro usuario
        int i = 0;
        while (i < listaUsuarios.length && listaUsuarios[i] != null) {
            i++;
        }

        if (i >= listaUsuarios.length) {
            System.out.println("Hay demasiados usuarios registrados");
            return;
        }

        listaUsuarios[i] = new Usuario(nombre, apellido, email, contrasena, pais);
        System.out.println("Registro exitoso");
    }

    public static void login() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Ingrese email: ");
        String emailIngresado = scan.nextLine();
        System.out.print("Ingrese Contraseña: ");
        String contrasenaIngresada = scan.nextLine();

        int i = usuarioEstaRegistrado(emailIngresado); // devuelve -1 si no se encuentra, si se encuentre devuelve el indice

        if (i == -1) {
            System.out.println("El usuario no está registrado");
            return;
        }

        if (!contrasenaIngresada.equals(listaUsuarios[i].getContrasena())) {
            System.out.println("Contraseña incorrecta");
            return;
        }

        System.out.println("Logueado con éxito");

    }

}
