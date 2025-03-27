import java.util.*;

public class TablaPaginas {
    private int numMarcos;
    private Map<Integer, Boolean> referencia;
    private Map<Integer, Boolean> modificacion;
    private Queue<Integer> memoria;
    private int hits = 0;
    private int fallas = 0;

    public TablaPaginas(int numMarcos) {
        this.numMarcos = numMarcos;
        this.referencia = new HashMap<>();
        this.modificacion = new HashMap<>();
        this.memoria = new LinkedList<>();
    }

    public synchronized void procesarReferencia(int pagina, boolean esEscritura) {
        if (memoria.contains(pagina)) {
            hits++;
            referencia.put(pagina, true);
            if (esEscritura) {
                modificacion.put(pagina, true);
            }
        } else {
            fallas++;
            if (memoria.size() >= numMarcos) {
                reemplazarPagina();
            }
            memoria.add(pagina);
            referencia.put(pagina, true);
            modificacion.put(pagina, esEscritura);
        }
    }

    //Metodo NRU
    public synchronized void reemplazarPagina() {
        Integer paginaAEliminar = null;
        int mejorClase = 4;
        int indice = 0;
        int tamanoMemoria = memoria.size();
    
        while (indice < tamanoMemoria) {
            Integer pagina = (Integer) memoria.toArray()[indice];
            int clase = 0;
    
            if (referencia.get(pagina)) {
                clase += 2;
            }
            if (modificacion.get(pagina)) {
                clase += 1;
            }
    
            if (clase < mejorClase) {
                mejorClase = clase;
                paginaAEliminar = pagina;
            }
    
            indice++;
        }
    
        if (paginaAEliminar != null) {
            memoria.remove(paginaAEliminar);
            referencia.remove(paginaAEliminar);
            modificacion.remove(paginaAEliminar);
        }
    }    

    public synchronized void actualizarNRU() {
        for (Integer pagina : referencia.keySet()) {
            referencia.put(pagina, false);
        }
    }

    public void imprimirResultados() {
        System.out.println(System.lineSeparator() + "Resultados de la simulacion");
        System.out.println("=============================");
        System.out.println("Numero de hits: " + hits);
        System.out.println("Numero de fallas de página: " + fallas);

        long tiempoHitsMs = (long) hits * 50 / 1_000_000;
        long tiempoFallasMs = (long) fallas * 10_000_000 / 1_000_000;

        System.out.println("Tiempo total (hits): " + tiempoHitsMs + " ms");
        System.out.println("Tiempo total (fallas): " + tiempoFallasMs + " ms");
    }
}
