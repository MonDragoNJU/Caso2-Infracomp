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

    private void reemplazarPagina() {
        for (int pagina : memoria) {
            if (!referencia.get(pagina) && !modificacion.get(pagina)) {
                memoria.remove(pagina);
                referencia.remove(pagina);
                modificacion.remove(pagina);
                return;
            }
        }
        memoria.poll();
    }

    public synchronized void actualizarNRU() {
        for (Integer pagina : referencia.keySet()) {
            referencia.put(pagina, false);
        }
    }

    public void imprimirResultados() {
        long tiempoHitsMs = (long) hits * 50 / 1_000_000;
        long tiempoFallasMs = (long) fallas * 10_000_000 / 1_000_000;

        System.out.println("Número de hits: " + hits);
        System.out.println("Número de fallas de página: " + fallas);
        System.out.println("Tiempo total (hits): " + tiempoHitsMs + " ms");
        System.out.println("Tiempo total (fallas): " + tiempoFallasMs + " ms");
    }
}
