package co.edu.poli.Examen2.adaptadores.salida.fabrica;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.adaptadores.salida.archivo.ExamenRespaldoArchivoAdapter;
import co.edu.poli.Examen2.adaptadores.salida.csv.ExamenRespaldoCsvAdapter;
import co.edu.poli.Examen2.puertos.salida.ExamenRespaldoPuerto;
import co.edu.poli.Examen2.soporte.Fixtures;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RespaldoFactoriesTest {

    private static final String PROPIEDAD = "respaldo.formato";

    @TempDir
    Path carpeta;

    private String valorOriginal;

    @BeforeEach
    void guardarPropiedad() {
        valorOriginal = System.getProperty(PROPIEDAD);
    }

    @AfterEach
    void restaurarPropiedad() {
        if (valorOriginal == null) {
            System.clearProperty(PROPIEDAD);
        } else {
            System.setProperty(PROPIEDAD, valorOriginal);
        }
    }

    @Test
    void paraDevuelveLaFabricaDelFormato() {
        assertTrue(RespaldoFactories.para(FormatoRespaldo.CSV) instanceof RespaldoCsvFactory);
        assertTrue(RespaldoFactories.para(FormatoRespaldo.SERIALIZADO) instanceof RespaldoSerializadoFactory);
    }

    @Test
    void cadaFabricaInformaSuFormato() {
        assertEquals(FormatoRespaldo.CSV, new RespaldoCsvFactory().formato());
        assertEquals(FormatoRespaldo.SERIALIZADO, new RespaldoSerializadoFactory().formato());
    }

    @Test
    void fabricaCsvCreaElAdaptadorCsv() {
        ExamenRespaldoPuerto puerto = new RespaldoCsvFactory().crear(carpeta);
        assertTrue(puerto instanceof ExamenRespaldoCsvAdapter);

        puerto.guardarRespaldo(Collections.singletonList(Fixtures.examen("AAA-001")));
        assertTrue(Files.exists(carpeta.resolve("examenes.csv")));
    }

    @Test
    void fabricaSerializadaCreaElAdaptadorDeArchivo() {
        ExamenRespaldoPuerto puerto = new RespaldoSerializadoFactory().crear(carpeta);
        assertTrue(puerto instanceof ExamenRespaldoArchivoAdapter);

        puerto.guardarRespaldo(Collections.singletonList(Fixtures.examen("AAA-001")));
        assertTrue(Files.exists(carpeta.resolve("examenes.ser")));
    }

    @Test
    void sinPropiedadUsaSerializado() {
        System.clearProperty(PROPIEDAD);
        assertEquals(FormatoRespaldo.SERIALIZADO, RespaldoFactories.desdeConfiguracion().formato());
    }

    @Test
    void laPropiedadDeSistemaElijeElFormato() {
        System.setProperty(PROPIEDAD, "csv");
        assertEquals(FormatoRespaldo.CSV, RespaldoFactories.desdeConfiguracion().formato());
    }

    @Test
    void propiedadInvalidaLanzaIllegalArgument() {
        System.setProperty(PROPIEDAD, "xml");
        assertThrows(IllegalArgumentException.class, RespaldoFactories::desdeConfiguracion);
    }
}
