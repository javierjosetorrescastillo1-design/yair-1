import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GestionUsuarios g = null;

        try {
            g = new GestionUsuarios();
            int opcion;

            do {
                System.out.println("\n===== PAT Colectivo - Control de acceso =====");
                System.out.println("1. Mostrar todos los usuarios");
                System.out.println("2. Buscar por rol");
                System.out.println("3. Ver roles disponibles");
                System.out.println("4. Cambiar acceso (Autorizado <-> Denegado)");
                System.out.println("5. Agregar usuario");
                System.out.println("0. Salir");
                System.out.print("Opción: ");

                opcion = leerEntero(sc);

                switch (opcion) {
                    case 1:
                        g.mostrarTodos().forEach(System.out::println);
                        break;
                    case 2:
                        System.out.print("Rol (Administrador/Usuario/Gestor): ");
                        String rol = sc.nextLine().trim();
                        var encontrados = g.buscarPorRol(rol);
                        if (encontrados.isEmpty()) System.out.println("No hay usuarios con ese rol.");
                        else encontrados.forEach(u -> System.out.println("  → " + u));
                        break;
                    case 3:
                        System.out.println("Roles disponibles: " + g.rolesDisponibles());
                        break;
                    case 4:
                        g.mostrarTodos().forEach(System.out::println);
                        System.out.print("ID del usuario a modificar: ");
                        int id = leerEntero(sc);
                        if (g.alternarEstado(id)) {
                            System.out.println("Acceso actualizado correctamente.");
                            g.mostrarTodos().stream()
                                    .filter(u -> u.getId() == id)
                                    .forEach(System.out::println);
                        } else {
                            System.out.println("No existe un usuario con ese ID.");
                        }
                        break;
                    case 5:
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine().trim();
                        System.out.print("Rol (Administrador/Usuario/Gestor): ");
                        String r = sc.nextLine().trim();
                        System.out.print("Acceso (Autorizado/Denegado): ");
                        String estado = sc.nextLine().trim();
                        g.agregar(nombre, r, estado);
                        System.out.println("Usuario agregado.");
                        break;
                    case 0:
                        System.out.println("Hasta luego.");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } while (opcion != 0);

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        } finally {
            if (g != null) g.cerrar();
            sc.close();
        }
    }

    private static int leerEntero(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
