package co.edu.poli.Examen2.dominio.modelo;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CostoTest {

    @Test
    void guardaConDosDecimales() {
        assertEquals("10.00", Costo.de(new BigDecimal("10")).monto().toPlainString());
    }

    @Test
    void redondeaHalfUp() {
        assertEquals(new BigDecimal("10.01"), Costo.de(new BigDecimal("10.005")).monto());
        assertEquals(new BigDecimal("10.00"), Costo.de(new BigDecimal("10.004")).monto());
    }

    @Test
    void rechazaNuloCeroYNegativo() {
        assertThrows(ReglaNegocioException.class, () -> Costo.de(null));
        assertThrows(ReglaNegocioException.class, () -> Costo.de(BigDecimal.ZERO));
        assertThrows(ReglaNegocioException.class, () -> Costo.de(new BigDecimal("-1")));
    }

    @Test
    void multiplicadoPorDevuelveNuevoCosto() {
        Costo base = Costo.de(new BigDecimal("100"));
        Costo resultado = base.multiplicadoPor(new BigDecimal("1.15"));
        assertEquals(new BigDecimal("115.00"), resultado.monto());
        assertEquals(new BigDecimal("100.00"), base.monto(), "el original no cambia (inmutable)");
    }

    @Test
    void multiplicadoPorValidaNuloYResultadoNoPositivo() {
        Costo base = Costo.de(new BigDecimal("100"));
        assertThrows(NullPointerException.class, () -> base.multiplicadoPor(null));
        assertThrows(ReglaNegocioException.class, () -> base.multiplicadoPor(BigDecimal.ZERO));
    }

    @Test
    void igualdadIgnoraLaEscalaOriginal() {
        Costo a = Costo.de(new BigDecimal("10"));
        Costo b = Costo.de(new BigDecimal("10.00"));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, Costo.de(new BigDecimal("11")));
    }

    @Test
    void toStringEsTextoPlano() {
        assertEquals("1500000.00", Costo.de(new BigDecimal("1.5E6")).toString());
    }
}
