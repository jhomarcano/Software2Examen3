package co.edu.poli.Examen2.dominio.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GrupoSanguineoTest {

    @ParameterizedTest
    @CsvSource({"A,POSITIVO,A+", "B,NEGATIVO,B-", "AB,POSITIVO,AB+", "O,NEGATIVO,O-"})
    void simboloCombinaTipoYFactor(TipoSangre tipo, FactorRH factor, String esperado) {
        GrupoSanguineo grupo = new GrupoSanguineo(tipo, factor);
        assertEquals(esperado, grupo.simbolo());
        assertEquals(esperado, grupo.toString());
    }

    @Test
    void exponeTipoYFactor() {
        GrupoSanguineo g = new GrupoSanguineo(TipoSangre.AB, FactorRH.NEGATIVO);
        assertEquals(TipoSangre.AB, g.tipo());
        assertEquals(FactorRH.NEGATIVO, g.factor());
    }

    @Test
    void rechazaNulos() {
        assertThrows(NullPointerException.class, () -> new GrupoSanguineo(null, FactorRH.POSITIVO));
        assertThrows(NullPointerException.class, () -> new GrupoSanguineo(TipoSangre.A, null));
    }

    @Test
    void igualdadPorValor() {
        GrupoSanguineo a = new GrupoSanguineo(TipoSangre.O, FactorRH.POSITIVO);
        GrupoSanguineo b = new GrupoSanguineo(TipoSangre.O, FactorRH.POSITIVO);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new GrupoSanguineo(TipoSangre.O, FactorRH.NEGATIVO));
        assertNotEquals(a, new GrupoSanguineo(TipoSangre.A, FactorRH.POSITIVO));
    }

    @Test
    void factorRhTieneSimbolos() {
        assertEquals("+", FactorRH.POSITIVO.simbolo());
        assertEquals("-", FactorRH.NEGATIVO.simbolo());
    }
}
