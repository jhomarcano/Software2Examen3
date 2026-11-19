package co.edu.poli.Examen2.dominio.modelo;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CodigoExamenTest {

    @Test
    void aceptaFormatoValido() {
        assertEquals("EXM-001", CodigoExamen.de("EXM-001").valor());
    }

    @Test
    void normalizaEspaciosYMayusculas() {
        assertEquals("EXM-001", CodigoExamen.de("  exm-001 ").valor());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void rechazaNuloOVacio(String texto) {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> CodigoExamen.de(texto));
        assertTrue(ex.getMessage().contains("obligatorio"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"EXM001", "EX-001", "EXMA-001", "EXM-01", "EXM-0012", "123-ABC", "EXM_001", "EXM-00A"})
    void rechazaFormatoInvalido(String texto) {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> CodigoExamen.de(texto));
        assertTrue(ex.getMessage().contains("XXX-000"));
    }

    @Test
    void igualdadPorValor() {
        CodigoExamen a = CodigoExamen.de("EXM-001");
        CodigoExamen b = CodigoExamen.de("exm-001");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, CodigoExamen.de("EXM-002"));
        assertNotEquals(a, "EXM-001");
    }

    @Test
    void toStringDevuelveElValor() {
        assertEquals("EXM-001", CodigoExamen.de("EXM-001").toString());
    }
}
