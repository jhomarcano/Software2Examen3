package co.edu.poli.Examen2.adaptadores.salida.csv;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import co.edu.poli.Examen2.soporte.Fixtures;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExamenRespaldoCsvAdapterTest {

    private static final String ENCABEZADO = "\"codigo\";\"paciente\";\"costo\";\"tipoSangre\";\"factorRh\"\n";

    @TempDir
    Path carpeta;

    private Path archivo;
    private ExamenRespaldoCsvAdapter adapter;

    @BeforeEach
    void preparar() {
        archivo = carpeta.resolve("examenes.csv");
        adapter = new ExamenRespaldoCsvAdapter(new ClienteCsv(), archivo);
    }

    private void escribir(String contenido) throws IOException {
        Files.write(archivo, contenido.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void guardarYCargarConservaLosDatos() {
        adapter.guardarRespaldo(Arrays.asList(
            Fixtures.examen("EXM-001", "Ana Pérez", "150.50", TipoSangre.AB, FactorRH.NEGATIVO),
            Fixtures.examen("EXM-002", "Luis Gómez", "80", TipoSangre.O, FactorRH.POSITIVO)));

        List<ExamenSangre> cargados = adapter.cargarRespaldo();

        assertEquals(2, cargados.size());
        assertEquals("Ana Pérez", cargados.get(0).paciente().valor());
        assertEquals("150.50", cargados.get(0).costo().monto().toPlainString());
        assertEquals("AB-", cargados.get(0).grupoSanguineo().simbolo());
        assertEquals("O+", cargados.get(1).grupoSanguineo().simbolo());
    }

    @Test
    void elArchivoGeneradoTieneEncabezadoYUnaFilaPorExamen() throws IOException {
        adapter.guardarRespaldo(Collections.singletonList(Fixtures.examen("EXM-001")));

        List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
        assertEquals(2, lineas.size());
        assertEquals("\"codigo\";\"paciente\";\"costo\";\"tipoSangre\";\"factorRh\"", lineas.get(0));
        assertTrue(lineas.get(1).startsWith("\"EXM-001\";"));
    }

    @Test
    void nombresConSeparadorYComillasSobrevivenAlViaje() {
        ExamenSangre e = Fixtures.examen("EXM-001", "Ana; \"La Mejor\" Pérez", "10",
            TipoSangre.A, FactorRH.POSITIVO);

        adapter.guardarRespaldo(Collections.singletonList(e));

        assertEquals("Ana; \"La Mejor\" Pérez", adapter.cargarRespaldo().get(0).paciente().valor());
    }

    @Test
    void respaldoVacioSeCargaComoListaVacia() {
        adapter.guardarRespaldo(Collections.<ExamenSangre>emptyList());
        assertTrue(adapter.cargarRespaldo().isEmpty());
    }

    @Test
    void cargarSinArchivoLanzaIllegalState() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
        assertTrue(ex.getMessage().contains("No existe un respaldo"));
    }

    @Test
    void encabezadoIncorrectoLanzaIllegalState() throws IOException {
        escribir("\"x\";\"y\"\n");
        IllegalStateException ex = assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
        assertTrue(ex.getMessage().contains("encabezado"));
    }

    @Test
    void archivoVacioLanzaIllegalState() throws IOException {
        escribir("");
        assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
    }

    @Test
    void filaConCantidadDeCamposIncorrectaLanzaIllegalState() throws IOException {
        escribir(ENCABEZADO + "\"EXM-001\";\"Ana\"\n");
        IllegalStateException ex = assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
        assertTrue(ex.getMessage().contains("Fila 2"));
    }

    @Test
    void datosQueViolanElDominioSonRechazados() throws IOException {
        escribir(ENCABEZADO + "\"MALO\";\"Ana\";\"10\";\"A\";\"POSITIVO\"\n");
        IllegalStateException ex = assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
        assertTrue(ex.getMessage().contains("fila 2"));
    }

    @Test
    void tipoDeSangreInexistenteEsRechazado() throws IOException {
        escribir(ENCABEZADO + "\"EXM-001\";\"Ana\";\"10\";\"X\";\"POSITIVO\"\n");
        assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
    }

    @Test
    void costoNoNumericoEsRechazado() throws IOException {
        escribir(ENCABEZADO + "\"EXM-001\";\"Ana\";\"abc\";\"A\";\"POSITIVO\"\n");
        assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
    }

    @Test
    void rechazaDependenciasNulas() {
        assertThrows(NullPointerException.class, () -> new ExamenRespaldoCsvAdapter(null, archivo));
        assertThrows(NullPointerException.class, () -> new ExamenRespaldoCsvAdapter(new ClienteCsv(), null));
    }
}
