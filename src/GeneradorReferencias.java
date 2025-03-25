import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GeneradorReferencias {
    private Imagen imagenIn;
    private int tamPagina;
    private int shiftSobelX, shiftSobelY, shiftImagenResultado;

    public GeneradorReferencias(Imagen nombreImagen, int tamPagina) {
        this.tamPagina = tamPagina;
        this.imagenIn = nombreImagen;
        this.shiftSobelX = calcularTamanoImagen();
        this.shiftSobelY = shiftSobelX + calcularTamanoFiltros();
        this.shiftImagenResultado = shiftSobelY + calcularTamanoFiltros();
    }

    public int obtenerPagina(int offset) {
        return offset / tamPagina;
    }    

    public int calcularDesplazamiento(int offset) {
        return offset % tamPagina;
    }

    public int calcularTamanoImagen() {
        return imagenIn.alto * imagenIn.ancho * 3;
    }

    public int calcularTamanoFiltros() {
        return 3 * 3 * 4;
    }

    public int calcularNumeroPaginas() {
        int tamImagen = calcularTamanoImagen();
        int tamFiltros = calcularTamanoFiltros() * 2;
        int tamRespuesta = tamImagen;
        return (int) Math.ceil((double) (tamImagen + tamFiltros + tamRespuesta) / tamPagina);
    }

    public String canalString(int canal) {
        switch (canal) {
            case 0:
                return "r";
            case 1:
                return "g";
            case 2:
                return "b";
            default:
                return "";
        }
    }

    public void generarReferencias() { 
        System.out.println("Generando referencias...");
        String nombreArchivo = "referencias.txt";
        StringBuilder referencias = new StringBuilder();
        
        referencias.append("TP=" + tamPagina + "\n");
        referencias.append("NF=" + imagenIn.alto + "\n");
        referencias.append("NC=" + imagenIn.ancho + "\n");
        referencias.append("NR=" + ((imagenIn.alto - 2) * (imagenIn.ancho - 2) * 84) + "\n");
        referencias.append("NP=" + calcularNumeroPaginas() + "\n");

        for (int i = 1; i < imagenIn.alto - 1; i++) {
            for (int j = 1; j < imagenIn.ancho - 1; j++) {
                for (int ki = -1; ki <= 1; ki++) {
                    for (int kj = -1; kj <= 1; kj++) {
                        int offsetPixel = ((i + ki) * imagenIn.ancho + (j + kj)) * 3;
                        for (int a = 0; a < 3; a++) {
                            referencias.append("Imagen[" + (i + ki) + "][" + (j + kj) + "]." + canalString(a) + "," + obtenerPagina(offsetPixel + a) + "," + calcularDesplazamiento(offsetPixel + a) + ",R\n");
                        }
                        int offsetFiltro = ((ki + 1) * 3 + (kj + 1)) * 4;
                        for (int a = 0; a < 3; a++) {
                            referencias.append("SOBEL_X[" + (ki + 1) + "][" + (kj + 1) + "]," + obtenerPagina(shiftSobelX + offsetFiltro) + "," + calcularDesplazamiento(shiftSobelX + offsetFiltro) + ",R\n");
                        }
                        for (int a = 0; a < 3; a++) {
                            referencias.append("SOBEL_Y[" + (ki + 1) + "][" + (kj + 1) + "]," + obtenerPagina(shiftSobelY + offsetFiltro) + "," + calcularDesplazamiento(shiftSobelY + offsetFiltro) + ",R\n");
                        }
                    }
                }
                int offsetRtaPixel = shiftImagenResultado + (i * imagenIn.ancho + j) * 3;
                for (int a = 0; a < 3; a++) {
                    referencias.append("Rta[" + i + "][" + j + "]." + canalString(a) + "," + obtenerPagina(offsetRtaPixel + a) + "," + calcularDesplazamiento(offsetRtaPixel + a) + ",W\n");
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write(referencias.toString());
            System.out.println("Referencias generadas en " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al escribir el archivo de referencias");
        }
    }
}