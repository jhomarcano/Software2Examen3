package co.edu.poli.Examen2.aplicacion.casouso;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.adaptadores.salida.memoria.ExamenRepositorioMemoria;
import co.edu.poli.Examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import co.edu.poli.Examen2.dominio.tarifa.PoliticaTarifa;
import co.edu.poli.Examen2.dominio.tarifa.TarifaEstandar;
import co.edu.poli.Examen2.dominio.tarifa.TarifaRecargoRhNegativo;
import co.edu.poli.Examen2.puertos.entrada.RegistrarExamenComando;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrarExamenServiceTest {

    private ExamenRepositorioMemoria repositorio;
    private RegistrarExamenService servicio;

    @BeforeEach
    void preparar() {
        repositorio = new ExamenRepositorioMemoria();
        servicio = new RegistrarExamenService(repositorio, new TarifaEstandar());
    }

    private RegistrarExamenComando comando(String codigo, String nombre, String costo,
                                           TipoSangre tipo, FactorRH factor) {
        return new RegistrarExamenComando(codigo, nombre, new BigDecimal(costo), tipo, factor);
    }

    @Test
    void registraYPersisteElExamen() {
        ExamenSangre e = servicio.registrar(
            comando("EXM-001", "Ana Pérez", "150", TipoSangre.A, FactorRH.POSITIVO));

        assertEquals("EXM-001", e.codigo().valor());
        assertEquals(new BigDecimal("150.00"), e.costo().monto());
        assertEquals(1, repositorio.listarTodos().size());
        assertEquals(e, repositorio.listarTodos().get(0));
    }

    @Test
    void rechazaCodigoDuplicado() {
        servicio.registrar(comando("EXM-001", "Ana", "100", TipoSangre.A, FactorRH.POSITIVO));

        ExamenDuplicadoException ex = assertThrows(ExamenDuplicadoException.class,
            () -> servicio.registrar(comando("EXM-001", "Luis", "50", TipoSangre.B, FactorRH.NEGATIVO)));
        assertTrue(ex.getMessage().contains("EXM-001"));
        assertEquals(1, repositorio.listarTodos().size());
    }

    @Test
    void detectaDuplicadoAunqueElCodigoVengaEnMinusculas() {
        servicio.registrar(comando("EXM-001", "Ana", "100", TipoSangre.A, FactorRH.POSITIVO));
        assertThrows(ExamenDuplicadoException.class,
            () -> servicio.registrar(comando("exm-001", "Luis", "50", TipoSangre.B, FactorRH.NEGATIVO)));
    }

    @Test
    void datosInvalidosNoSeGuardan() {
        assertThrows(ReglaNegocioException.class,
            () -> servicio.registrar(comando("MAL", "Ana", "100", TipoSangre.A, FactorRH.POSITIVO)));
        assertThrows(ReglaNegocioException.class,
            () -> servicio.registrar(comando("EXM-002", " ", "100", TipoSangre.A, FactorRH.POSITIVO)));
        assertThrows(ReglaNegocioException.class,
            () -> servicio.registrar(comando("EXM-003", "Ana", "0", TipoSangre.A, FactorRH.POSITIVO)));
        assertTrue(repositorio.listarTodos().isEmpty());
    }

    @Test
    void tipoDeSangreNuloLanzaNullPointer() {
        assertThrows(NullPointerException.class,
            () -> servicio.registrar(comando("EXM-001", "Ana", "100", null, FactorRH.POSITIVO)));
        assertTrue(repositorio.listarTodos().isEmpty());
    }

    @Test
    void delegaElCostoFinalEnLaEstrategia() {
        PoliticaTarifa duplica = (base, grupo) -> base.multiplicadoPor(new BigDecimal("2"));
        RegistrarExamenService conEstrategia = new RegistrarExamenService(repositorio, duplica);

        ExamenSangre e = conEstrategia.registrar(
            comando("EXM-001", "Ana", "100", TipoSangre.A, FactorRH.POSITIVO));

        assertEquals(new BigDecimal("200.00"), e.costo().monto());
    }

    @Test
    void conRecargoRhNegativoElCostoGuardadoYaIncluyeElRecargo() {
        RegistrarExamenService conRecargo =
            new RegistrarExamenService(repositorio, new TarifaRecargoRhNegativo(new BigDecimal("15")));

        ExamenSangre negativo = conRecargo.registrar(
            comando("EXM-001", "Ana", "100", TipoSangre.O, FactorRH.NEGATIVO));
        ExamenSangre positivo = conRecargo.registrar(
            comando("EXM-002", "Luis", "100", TipoSangre.O, FactorRH.POSITIVO));

        assertEquals(new BigDecimal("115.00"), negativo.costo().monto());
        assertEquals(new BigDecimal("100.00"), positivo.costo().monto());
    }

    @Test
    void elConstructorRechazaDependenciasNulas() {
        assertThrows(NullPointerException.class, () -> new RegistrarExamenService(null, new TarifaEstandar()));
        assertThrows(NullPointerException.class, () -> new RegistrarExamenService(repositorio, null));
    }
}
