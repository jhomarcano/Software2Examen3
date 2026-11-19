package co.edu.poli.Examen2.dominio.modelo;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.soporte.Fixtures;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ExamenSangreTest {

    private final CodigoExamen codigo = CodigoExamen.de("EXM-001");
    private final NombrePaciente paciente = NombrePaciente.de("Ana Pérez");
    private final Costo costo = Costo.de(new BigDecimal("100"));
    private final GrupoSanguineo grupo = Fixtures.grupo(TipoSangre.O, FactorRH.POSITIVO);

    @Test
    void registraConDatosValidos() {
        ExamenSangre e = ExamenSangre.registrar(codigo, paciente, costo, grupo);
        assertEquals(codigo, e.codigo());
        assertEquals(paciente, e.paciente());
        assertEquals(costo, e.costo());
        assertEquals(grupo, e.grupoSanguineo());
    }

    @Test
    void rechazaArgumentosNulos() {
        assertThrows(NullPointerException.class, () -> ExamenSangre.registrar(null, paciente, costo, grupo));
        assertThrows(NullPointerException.class, () -> ExamenSangre.registrar(codigo, null, costo, grupo));
        assertThrows(NullPointerException.class, () -> ExamenSangre.registrar(codigo, paciente, null, grupo));
        assertThrows(NullPointerException.class, () -> ExamenSangre.registrar(codigo, paciente, costo, null));
    }

    @Test
    void laIdentidadEsElCodigo() {
        ExamenSangre a = Fixtures.examen("EXM-001", "Ana", "100", TipoSangre.O, FactorRH.POSITIVO);
        ExamenSangre mismoCodigo = Fixtures.examen("EXM-001", "Luis", "999", TipoSangre.A, FactorRH.NEGATIVO);
        ExamenSangre otroCodigo = Fixtures.examen("EXM-002", "Ana", "100", TipoSangre.O, FactorRH.POSITIVO);

        assertEquals(a, mismoCodigo);
        assertEquals(a.hashCode(), mismoCodigo.hashCode());
        assertNotEquals(a, otroCodigo);
    }

    @Test
    void toStringResumeElExamen() {
        ExamenSangre e = ExamenSangre.registrar(codigo, paciente, costo, grupo);
        assertEquals("EXM-001 | Ana Pérez | O+ | $100.00", e.toString());
    }
}
