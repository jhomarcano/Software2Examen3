// adaptadores/salida/csv/ClienteCsv.java
package co.edu.poli.Examen2.adaptadores.salida.csv;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Patrón Adapter: Adaptee.</b> Simula un componente externo (como OpenCSV) con
 * una API propia, incompatible con {@code ExamenRespaldoPuerto}: trabaja con filas de
 * texto ({@code String[]}) y no conoce el dominio. Escribe todos los campos entre comillas
 * y usa {@code ;} como separador.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class ClienteCsv {

    private static final char SEPARADOR = ';';

    /**
     * Escribe las filas en un archivo, sobrescribiéndolo.
     *
     * @param destino archivo de salida
     * @param filas   filas de campos de texto
     * @throws IOException si no puede escribirse
     */
    public void escribir(Path destino, List<String[]> filas) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String[] fila : filas) {
            for (int i = 0; i < fila.length; i++) {
                if (i > 0) {
                    sb.append(SEPARADOR);
                }
                sb.append('"').append(fila[i].replace("\"", "\"\"")).append('"');
            }
            sb.append('\n');
        }
        Files.write(destino, sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Lee un archivo CSV como lista de filas de campos, respetando campos entre comillas.
     *
     * @param origen archivo de entrada
     * @return las filas leídas
     * @throws IOException si no puede leerse
     */
    public List<String[]> leer(Path origen) throws IOException {
        String contenido = new String(Files.readAllBytes(origen), StandardCharsets.UTF_8);
        List<String[]> filas = new ArrayList<>();
        List<String> campos = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean entreComillas = false;

        for (int i = 0; i < contenido.length(); i++) {
            char c = contenido.charAt(i);
            if (entreComillas) {
                if (c == '"') {
                    if (i + 1 < contenido.length() && contenido.charAt(i + 1) == '"') {
                        campo.append('"');
                        i++;
                    } else {
                        entreComillas = false;
                    }
                } else {
                    campo.append(c);
                }
            } else if (c == '"') {
                entreComillas = true;
            } else if (c == SEPARADOR) {
                campos.add(campo.toString());
                campo.setLength(0);
            } else if (c == '\n' || c == '\r') {
                if (c == '\r' && i + 1 < contenido.length() && contenido.charAt(i + 1) == '\n') {
                    i++;
                }
                campos.add(campo.toString());
                campo.setLength(0);
                filas.add(campos.toArray(new String[0]));
                campos.clear();
            } else {
                campo.append(c);
            }
        }
        if (campo.length() > 0 || !campos.isEmpty()) {
            campos.add(campo.toString());
            filas.add(campos.toArray(new String[0]));
        }
        return filas;
    }
}