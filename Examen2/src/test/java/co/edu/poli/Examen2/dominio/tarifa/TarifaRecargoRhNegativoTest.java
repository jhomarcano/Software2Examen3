package co.edu.poli.Examen2.dominio.tarifa;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import co.edu.poli.Examen2.soporte.Fixtures;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TarifaRecargoRhNegativoTest {

    private final PoliticaTarifa tarifa = new TarifaRecargoRhNegativo(new BigDecimal("15"));
    private final Costo base = Costo.de(new BigDecimal("100"));

    @Test
    void recargaAPacientesRhNegativo() {
        Costo final_ = tarifa.calcular(base, Fixtures.grupo(TipoSangre.O, FactorRH.NEGATIVO));
        assertEquals(new BigDecimal("115.00"), final_.monto());
    }

    @Test
    void noRecargaAPacientesRhPositivo() {
        Costo final_ = tarifa.calcular(base, Fixtures.grupo(TipoSangre.O, FactorRH.POSITIVO));
        assertEquals(base, final_);
    }

    @Test
    void recargoCeroEsValidoYNoCambiaElCosto() {
        PoliticaTarifa sinRecargo = new TarifaRecargoRhNegativo(BigDecimal.ZERO);
        assertEquals(base, sinRecargo.calcular(base, Fixtures.grupo(TipoSangre.A, FactorRH.NEGATIVO)));
    }

    @Test
    void rechazaPorcentajeNuloONegativo() {
        assertThrows(ReglaNegocioException.class, () -> new TarifaRecargoRhNegativo(null));
        assertThrows(ReglaNegocioException.class, () -> new TarifaRecargoRhNegativo(new BigDecimal("-1")));
    }
}
