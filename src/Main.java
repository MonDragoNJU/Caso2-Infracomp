import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Boolean continuar = true;
        while (continuar) {

            System.out.println("Simulador de memoria virtual");
            System.out.println("=============================");
            System.out.println("Seleccione una opcion:");
            System.out.println("1. Generar archivo de referencias");
            System.out.println("2. Ejecutar simulacion");
            System.out.println("3. Salir");
            System.out.print("Opcion: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre del archivo BMP: ");
                    String nombreImagen = scanner.nextLine();
                    Imagen imagen = new Imagen("src/imagenes/"+nombreImagen);
                    Imagen imagenOut=  new Imagen("src/imagenes/"+nombreImagen);

                    System.out.print("Ingrese el tamaño de página (en bytes): ");
                    int tamPagina = scanner.nextInt();

                    FiltroSobel fs= new FiltroSobel(imagen, imagenOut);
                    fs.applySobel();

                    GeneradorReferencias generador = new GeneradorReferencias(imagen, tamPagina);

                    imagenOut.escribirImagen("src/imagenes/imagenOut.bmp");

                    generador.generarReferencias();
                    break;

                case 2:
                    System.out.println("Indique el numero de marcos de pagina: ");
                    int numMarcos = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Indique el nombre del archivo de referencias: ");
                    String archivoReferencias = scanner.nextLine();
            
                    SimuladorMemoria simulador = new SimuladorMemoria(numMarcos, archivoReferencias);
                    simulador.ejecutar();
                    break;

                case 3:
                    System.out.println("Saliendo del simulador...");
                    continuar = false;
                    break;

                default:
                    System.out.println("Opcion no valida.");
                    break;
            }
        }
        scanner.close();
    }
}
