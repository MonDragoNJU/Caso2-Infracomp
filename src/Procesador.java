import java.util.List;

public class Procesador extends Thread {
    private TablaPaginas tablaPaginas;
    private List<String> referencias;

    public Procesador(TablaPaginas tablaPaginas, List<String> referencias) {
        this.tablaPaginas = tablaPaginas;
        this.referencias = referencias;
    }

    @Override
    public void run() {
        for (int i = 0; i < referencias.size(); i++) {
            String[] partes = referencias.get(i).split(",");
            int pagina = Integer.parseInt(partes[1]);
            boolean esEscritura = partes[3].equals("W");
            
            tablaPaginas.procesarReferencia(pagina, esEscritura);

            if (i % 10000 == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
