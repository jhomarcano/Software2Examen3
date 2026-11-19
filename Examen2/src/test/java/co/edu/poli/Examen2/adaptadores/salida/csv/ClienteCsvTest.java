package co.edu.poli.Examen2.adaptadores.salida.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ClienteCsvTest {

    @TempDir
    Path carpeta;

    private final ClienteCsv cliente = new ClienteCsv();

    @Test
    void escribeTodosLosCamposEntreComillasConPuntoYComa() throws IOException {
        Path f = carpeta.resolve("a.csv");
        cliente.escribir(f, Arrays.<String[]>asList(new String[] {"a", "b"}, new String[] {"c", "d"}));

        String contenido = new String(Files.readAllBytes(f), StandardCharsets.UTF_8);
        assertEquals("\"a\";\"b\"\n\"c\";\"d\"\n", contenido);
    }

    @Test
    void escribirYLeerConservaCaracteresEspeciales() throws IOException {
        Path f = carpeta.resolve("b.csv");
        String[] fila = {"con;punto y coma", "con \"comillas\"", "salto\nde línea", "ñandú", ""};

        cliente.escribir(f, Arrays.<String[]>asList(fila));
        List<String[]> leidas = cliente.leer(f);

        assertEquals(1, leidas.size());
        assertArrayEquals(fila, leidas.get(0));
    }

    @Test
    void leeFinesDeLineaWindows() throws IOException {
        Path f = carpeta.resolve("c.csv");
        Files.write(f, "\"a\";\"b\"\r\n\"c\";\"d\"\r\n".getBytes(StandardCharsets.UTF_8));

        List<String[]> filas = cliente.leer(f);

        assertEquals(2, filas.size());
        assertArrayEquals(new String[] {"a", "b"}, filas.get(0));
        assertArrayEquals(new String[] {"c", "d"}, filas.get(1));
    }

    @Test
    void leeUltimaFilaSinSaltoDeLineaFinal() throws IOException {
        Path f = carpeta.resolve("d.csv");
        Files.write(f, "\"a\";\"b\"".getBytes(StandardCharsets.UTF_8));

        List<String[]> filas = cliente.leer(f);

        assertEquals(1, filas.size());
        assertArrayEquals(new String[] {"a", "b"}, filas.get(0));
    }

    @Test
    void archivoVacioNoTieneFilas() throws IOException {
        Path f = carpeta.resolve("e.csv");
        Files.write(f, new byte[0]);
        assertTrue(cliente.leer(f).isEmpty());
    }

    @Test
    void leerArchivoInexistenteLanzaIOException() {
        assertThrows(IOException.class, () -> cliente.leer(carpeta.resolve("nada.csv")));
    }
}
