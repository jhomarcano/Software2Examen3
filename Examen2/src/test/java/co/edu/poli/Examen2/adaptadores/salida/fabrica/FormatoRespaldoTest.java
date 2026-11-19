package co.edu.poli.Examen2.adaptadores.salida.fabrica;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class FormatoRespaldoTest {

    @Test
    void tieneExtensionPorFormato() {
        assertEquals("ser", FormatoRespaldo.SERIALIZADO.extension());
        assertEquals("csv", FormatoRespaldo.CSV.extension());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void sinConfiguracionUsaSerializado(String texto) {
        assertEquals(FormatoRespaldo.SERIALIZADO, FormatoRespaldo.desde(texto));
    }

    @Test
    void interpretaTextoSinImportarMayusculasNiEspacios() {
        assertEquals(FormatoRespaldo.CSV, FormatoRespaldo.desde(" csv "));
        assertEquals(FormatoRespaldo.CSV, FormatoRespaldo.desde("CSV"));
        assertEquals(FormatoRespaldo.SERIALIZADO, FormatoRespaldo.desde("Serializado"));
    }

    @Test
    void formatoDesconocidoLanzaIllegalArgument() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> FormatoRespaldo.desde("xml"));
        assertTrue(ex.getMessage().contains("xml"));
    }
}
