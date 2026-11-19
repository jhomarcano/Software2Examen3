package co.edu.poli.Examen2.dominio.tarifa;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import co.edu.poli.Examen2.soporte.Fixtures;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TarifaEstandarTest {

    private final PoliticaTarifa tarifa = new TarifaEstandar();

    @Test
    void devuelveElCostoBaseSinCambios() {
        Costo base = Costo.de(new BigDecimal("80"));
        assertEquals(base, tarifa.calcular(base, Fixtures.grupo(TipoSangre.A, FactorRH.NEGATIVO)));
        assertEquals(base, tarifa.calcular(base, Fixtures.grupo(TipoSangre.O, FactorRH.POSITIVO)));
    }
}
