package co.edu.poli.Examen2.adaptadores.salida.archivo;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import co.edu.poli.Examen2.soporte.Fixtures;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExamenRespaldoArchivoAdapterTest {

    @TempDir
    Path carpeta;

    private Path archivo;
    private ExamenRespaldoArchivoAdapter adapter;

    @BeforeEach
    void preparar() {
        archivo = carpeta.resolve("examenes.ser");
        adapter = new ExamenRespaldoArchivoAdapter(archivo);
    }

    @Test
    void guardarYCargarConservaLosDatos() {
        List<ExamenSangre> originales = Arrays.asList(
            Fixtures.examen("EXM-001", "Ana Pérez", "150.50", TipoSangre.AB, FactorRH.NEGATIVO),
            Fixtures.examen("EXM-002", "Luis Gómez", "80", TipoSangre.O, FactorRH.POSITIVO));

        adapter.guardarRespaldo(originales);
        List<ExamenSangre> cargados = adapter.cargarRespaldo();

        assertEquals(2, cargados.size());
        ExamenSangre primero = cargados.get(0);
        assertEquals("EXM-001", primero.codigo().valor());
        assertEquals("Ana Pérez", primero.paciente().valor());
        assertEquals("150.50", primero.costo().monto().toPlainString());
        assertEquals("AB-", primero.grupoSanguineo().simbolo());
        assertEquals("EXM-002", cargados.get(1).codigo().valor());
    }

    @Test
    void respaldoVacioSeCargaComoListaVacia() {
        adapter.guardarRespaldo(Collections.<ExamenSangre>emptyList());
        assertTrue(adapter.cargarRespaldo().isEmpty());
    }

    @Test
    void guardarSobrescribeElRespaldoAnterior() {
        adapter.guardarRespaldo(Arrays.asList(Fixtures.examen("AAA-001"), Fixtures.examen("AAA-002")));
        adapter.guardarRespaldo(Collections.singletonList(Fixtures.examen("BBB-001")));

        List<ExamenSangre> cargados = adapter.cargarRespaldo();
        assertEquals(1, cargados.size());
        assertEquals("BBB-001", cargados.get(0).codigo().valor());
    }

    @Test
    void cargarSinArchivoLanzaIllegalState() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
        assertTrue(ex.getMessage().contains("No existe un respaldo"));
    }

    @Test
    void archivoCorruptoLanzaIllegalState() throws IOException {
        Files.write(archivo, new byte[] {1, 2, 3, 4, 5});
        assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
    }

    @Test
    void elFiltroRechazaClasesNoPermitidas() throws IOException {
        escribirObjeto(new HashMap<String, String>());
        assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
    }

    @Test
    void rechazaListasConElementosInesperados() throws IOException {
        ArrayList<String> intrusos = new ArrayList<>(Arrays.asList("no", "soy", "dto"));
        escribirObjeto(intrusos);

        IllegalStateException ex = assertThrows(IllegalStateException.class, adapter::cargarRespaldo);
        assertTrue(ex.getMessage().contains("inesperados"));
    }

    @Test
    void guardarEnRutaInvalidaLanzaIllegalState() {
        ExamenRespaldoArchivoAdapter malo =
            new ExamenRespaldoArchivoAdapter(carpeta.resolve("no-existe").resolve("x.ser"));
        assertThrows(IllegalStateException.class,
            () -> malo.guardarRespaldo(Collections.singletonList(Fixtures.examen("AAA-001"))));
    }

    @Test
    void rechazaRutaNula() {
        assertThrows(NullPointerException.class, () -> new ExamenRespaldoArchivoAdapter(null));
    }

    private void escribirObjeto(Object objeto) throws IOException {
        try (OutputStream os = Files.newOutputStream(archivo);
             ObjectOutputStream out = new ObjectOutputStream(os)) {
            out.writeObject(objeto);
        }
    }
}
