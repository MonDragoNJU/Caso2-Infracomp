import java.io.*;
import java.util.*;

public class SimuladorMemoria {
    private TablaPaginas tablaPaginas;
    private List<String> referencias;
    
    public SimuladorMemoria(int numMarcos, String archivoReferencias) {
        this.referencias = new ArrayList<>();
        this.tablaPaginas = new TablaPaginas(numMarcos);
        cargarReferencias(archivoReferencias);
    }

    private void cargarReferencias(String archivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.startsWith("NF=") && !linea.startsWith("NC=") && 
                           !linea.startsWith("NR=") && !linea.startsWith("NP=") &&
                           !linea.startsWith("TP=")) {
                    referencias.add(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al leer el archivo de referencias.");
        }
    }

    public void ejecutar() {
        Procesador procesador = new Procesador(tablaPaginas, referencias);
        Actualizador actualizador = new Actualizador(tablaPaginas);

        procesador.start();
        actualizador.start();

        try {
            procesador.join();
            actualizador.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tablaPaginas.imprimirResultados();
    }

}
