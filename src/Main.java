import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al simulador de memoria virtual");
        Boolean seguir= true;
        while(seguir){
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

                    System.out.print("Ingrese el tamaño de página (en bytes): ");
                    int tamPagina = scanner.nextInt();

                    GeneradorReferencias generador = new GeneradorReferencias(imagen, tamPagina);

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
                    seguir = false;
                    System.out.println("Saliendo del simulador...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
                    break;
            }
        }
        scanner.close();
    }
}
