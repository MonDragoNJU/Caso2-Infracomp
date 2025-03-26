public class Actualizador extends Thread {
    private TablaPaginas tablaPaginas;

    public Actualizador(TablaPaginas tablaPaginas) {
        this.tablaPaginas = tablaPaginas;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1);
                tablaPaginas.actualizarNRU();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
