package co.edu.poli.Examen2.dominio.tarifa;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import co.edu.poli.Examen2.soporte.Fixtures;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TarifaConDescuentoTest {

    @Test
    void aplicaElDescuentoATodosLosGrupos() {
        PoliticaTarifa tarifa = new TarifaConDescuento(new BigDecimal("10"));
        Costo base = Costo.de(new BigDecimal("200"));

        assertEquals(new BigDecimal("180.00"),
            tarifa.calcular(base, Fixtures.grupo(TipoSangre.A, FactorRH.POSITIVO)).monto());
        assertEquals(new BigDecimal("180.00"),
            tarifa.calcular(base, Fixtures.grupo(TipoSangre.O, FactorRH.NEGATIVO)).monto());
    }

    @Test
    void rechazaPorcentajesFueraDeRango() {
        assertThrows(ReglaNegocioException.class, () -> new TarifaConDescuento(null));
        assertThrows(ReglaNegocioException.class, () -> new TarifaConDescuento(BigDecimal.ZERO));
        assertThrows(ReglaNegocioException.class, () -> new TarifaConDescuento(new BigDecimal("-5")));
        assertThrows(ReglaNegocioException.class, () -> new TarifaConDescuento(new BigDecimal("100")));
        assertThrows(ReglaNegocioException.class, () -> new TarifaConDescuento(new BigDecimal("150")));
    }

    @Test
    void aceptaLosLimitesInternos() {
        assertDoesNotThrow(() -> new TarifaConDescuento(new BigDecimal("0.01")));
        assertDoesNotThrow(() -> new TarifaConDescuento(new BigDecimal("99.99")));
    }
}
