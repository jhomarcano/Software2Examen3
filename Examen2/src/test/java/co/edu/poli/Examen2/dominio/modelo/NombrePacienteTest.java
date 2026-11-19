package co.edu.poli.Examen2.dominio.modelo;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NombrePacienteTest {

    @Test
    void aceptaNombreYLoLimpia() {
        assertEquals("Ana Pérez", NombrePaciente.de("  Ana Pérez  ").valor());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void rechazaNuloOVacio(String texto) {
        assertThrows(ReglaNegocioException.class, () -> NombrePaciente.de(texto));
    }

    @Test
    void aceptaExactamente100Caracteres() {
        String nombre = "a".repeat(100);
        assertEquals(100, NombrePaciente.de(nombre).valor().length());
    }

    @Test
    void rechazaMasDe100Caracteres() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
            () -> NombrePaciente.de("a".repeat(101)));
        assertTrue(ex.getMessage().contains("100"));
    }

    @Test
    void igualdadPorValor() {
        assertEquals(NombrePaciente.de("Ana"), NombrePaciente.de(" Ana "));
        assertEquals(NombrePaciente.de("Ana").hashCode(), NombrePaciente.de("Ana").hashCode());
        assertNotEquals(NombrePaciente.de("Ana"), NombrePaciente.de("Luis"));
    }
}
