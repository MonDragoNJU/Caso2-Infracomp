import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicitar el nombre del archivo BMP
        System.out.print("Ingrese el nombre del archivo BMP: ");
        String nombreImagen = scanner.nextLine();
        Imagen imagen = new Imagen(nombreImagen);

        // Solicitar el tamaño de página en bytes
        System.out.print("Ingrese el tamaño de página (en bytes): ");
        int tamPagina = scanner.nextInt();

        // Crear la instancia del generador de referencias
        GeneradorReferencias generador = new GeneradorReferencias(imagen, tamPagina);

        // Escribir las referencias en un archivo
        generador.generarReferencias();

        System.out.println("Generación de referencias completada. Archivo: referencias.txt");

        scanner.close();
    }
}
