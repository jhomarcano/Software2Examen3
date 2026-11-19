// dominio/tarifa/TarifaConDescuento.java
package co.edu.poli.Examen2.dominio.tarifa;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.GrupoSanguineo;
import java.math.BigDecimal;

/**
 * <b>Strategy concreta:</b> descuento porcentual para todos los exámenes
 * (por ejemplo, un convenio con una empresa).
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class TarifaConDescuento implements PoliticaTarifa {

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    private final BigDecimal factor;

    /**
     * Crea la política con un porcentaje de descuento.
     *
     * @param porcentaje descuento en porcentaje, mayor que 0 y menor que 100
     * @throws ReglaNegocioException si es nulo o está fuera de rango
     */
    public TarifaConDescuento(BigDecimal porcentaje) {
        if (porcentaje == null || porcentaje.signum() <= 0 || porcentaje.compareTo(CIEN) >= 0) {
            throw new ReglaNegocioException("El descuento debe estar entre 0 y 100 (exclusivo).");
        }
        this.factor = BigDecimal.ONE.subtract(porcentaje.movePointLeft(2));
    }

    /** {@inheritDoc} */
    @Override
    public Costo calcular(Costo costoBase, GrupoSanguineo grupo) {
        return costoBase.multiplicadoPor(factor);
    }
}