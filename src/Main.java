import model.Usuario;

import java.util.Scanner;

public class Main {
    static String nombre;
    static String apellido;
    static String email;
    static String pais;
    static String contrasena;
    public static void main(String[] args) {

        int opcion;
        do {
            System.out.println("Elija una opción");
            System.out.println("1- Login");
            System.out.println("2- Registro");
            System.out.println("3- Salir");
            Scanner scan = new Scanner(System.in);
            opcion = scan.nextInt();
            switch (opcion){
                case 1:
                    login();
                    break;
                case 2:
                    registro();
                    break;
                default:
                    System.out.println("Saliendo...");
                    opcion = 3;
                    break;
            }
        } while (opcion != 3);

    }

    private static void registro() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Ingrese nombre:");
        nombre = scan.nextLine();
        while (nombre == null || nombre.equals("") || nombre.length() <= 5) {
            System.out.println("No supera la cantidad minima de caracteres, ingrese nuevamente el nombre:");
            nombre = scan.nextLine();
        }

        System.out.println("Ingrese apellido:");
        apellido = scan.nextLine();
        while (apellido == null || apellido.equals("")) {
            System.out.println("No supera la cantidad minima de caracteres, ingrese nuevamente el apellido:");
            apellido = scan.nextLine();
        }

        System.out.println("Ingrese email:");
        email = scan.nextLine();
        while (email == null || !email.contains("@")) {
            System.out.println("Formato email incorrecto, ingrese nuevamente el email:");
            email = scan.nextLine();
        }

        System.out.println("Ingrese país:");
        pais = scan.nextLine();
        while (pais == null || pais.equals("")) {
            System.out.println("No supera la cantidad minima de caracteres, ingrese nuevamente el pais:");
            pais = scan.nextLine();
        }

        System.out.println("Ingrese contraseña:");
        contrasena = scan.nextLine();
        while (contrasena == null || contrasena.equals("") || contrasena.length() < 8) {
            System.out.println("No supera la cantidad minima de caracteres, ingrese nuevamente la contraseña:");
            contrasena = scan.nextLine();
        }
        System.out.println("Registro exitoso:");
    }

    private static void login() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Ingrese nombre de usuario: ");
        String nombreIngresado = scan.nextLine();
        System.out.print("Ingrese Contraseña: ");
        String contrasenaIngresada = scan.nextLine();

        if (nombreIngresado.equals(nombre)) {
            if (contrasenaIngresada.equals(contrasena)) {
                System.out.println("Usuario logueado con exito");
            } else {
                System.out.println("Contraseña incorrecta");
            }
        }
         else {
            System.out.println("No existe el usuario en el sistema.");
        }
    }

}
