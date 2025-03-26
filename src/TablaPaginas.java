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
            referencia.put(pagina, true);  // La página ha sido usada
            if (esEscritura) {
                modificacion.put(pagina, true);  // Si es escritura, marcar como modificada
            }
        } else {
            fallas++;
            if (memoria.size() >= numMarcos) {
                reemplazarPagina();  // Aplicar NRU para liberar espacio
            }
            memoria.add(pagina);
            referencia.put(pagina, true);
            modificacion.put(pagina, esEscritura);
        }
    }

    private void reemplazarPagina() {
        Integer paginaAEliminar = null;

        for (Integer pagina : memoria) {
            if (!referencia.get(pagina) && !modificacion.get(pagina)) {
                paginaAEliminar = pagina;
                break;
            }
        }

        if (paginaAEliminar == null) {
            for (Integer pagina : memoria) {
                if (!referencia.get(pagina) && modificacion.get(pagina)) {
                    paginaAEliminar = pagina;
                    break;
                }
            }
        }

        if (paginaAEliminar == null) {
            for (Integer pagina : memoria) {
                if (referencia.get(pagina) && !modificacion.get(pagina)) {
                    paginaAEliminar = pagina;
                    break;
                }
            }
        }

        if (paginaAEliminar == null) {
            paginaAEliminar = memoria.peek();  // Última opción, eliminar la primera página de la cola
        }

        // Eliminar la página seleccionada
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
        System.out.println("Número de hits: " + hits);
        System.out.println("Número de fallas de página: " + fallas);

        long tiempoHitsMs = (long) hits * 50 / 1_000_000;
        long tiempoFallasMs = (long) fallas * 10_000_000 / 1_000_000;

        System.out.println("Tiempo total (hits): " + tiempoHitsMs + " ms");
        System.out.println("Tiempo total (fallas): " + tiempoFallasMs + " ms");
    }
}
