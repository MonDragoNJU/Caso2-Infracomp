import java.util.*;

public class TablaPaginas {
    private int numMarcos;
    private Map<Integer, Boolean> referencia;
    private Map<Integer, Boolean> modificacion;
    private ArrayList<Integer> memoria;
    private int hits = 0;
    private int fallas = 0;
    private Object lock = new Object();

    public TablaPaginas(int numMarcos) {
        this.numMarcos = numMarcos;
        this.referencia = new HashMap<>();
        this.modificacion = new HashMap<>();
        this.memoria = new ArrayList<>();
    }

    public void procesarReferencia(int pagina, boolean esEscritura) {
        synchronized (lock) {
            if (memoria.contains(pagina)) {
                hits++;
            } else {
                fallas++;
                if (memoria.size() >= numMarcos) {
                    reemplazarPagina();
                }
                memoria.add(pagina);
            }
            referencia.put(pagina, true);
            modificacion.put(pagina, esEscritura);
        }
    }

    //Metodo NRU
    public synchronized void reemplazarPagina() {
        Integer paginaAEliminar = null;
        int mejorClase = 4;
    
        for (Integer pagina : memoria) {
            int clase = (referencia.get(pagina) ? 2 : 0) + (modificacion.get(pagina) ? 1 : 0);
    
            if (clase < mejorClase) {
                mejorClase = clase;
                paginaAEliminar = pagina;
                if (mejorClase == 0) break;
            }
        }
    
        if (paginaAEliminar != null) {
            memoria.remove(paginaAEliminar);
            referencia.remove(paginaAEliminar);
            modificacion.remove(paginaAEliminar);
        }
    }     

    public void actualizarNRU() {
        synchronized (lock) {
            for (Integer pagina : referencia.keySet()) {
                referencia.put(pagina, false);
            }
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