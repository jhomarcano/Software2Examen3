// dominio/tarifa/TarifaRecargoRhNegativo.java
package co.edu.poli.Examen2.dominio.tarifa;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.GrupoSanguineo;
import java.math.BigDecimal;

/**
 * <b>Strategy concreta:</b> aplica un recargo porcentual a los pacientes Rh negativo
 * (regla de negocio supuesta: requieren pruebas de compatibilidad adicionales).
 * Los pacientes Rh positivo pagan el costo base.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class TarifaRecargoRhNegativo implements PoliticaTarifa {

    private final BigDecimal factor;

    /**
     * Crea la política con un porcentaje de recargo.
     *
     * @param porcentaje recargo en porcentaje (por ejemplo {@code 15} para 15 %); no negativo
     * @throws ReglaNegocioException si es nulo o negativo
     */
    public TarifaRecargoRhNegativo(BigDecimal porcentaje) {
        if (porcentaje == null || porcentaje.signum() < 0) {
            throw new ReglaNegocioException("El porcentaje de recargo no puede ser negativo.");
        }
        this.factor = BigDecimal.ONE.add(porcentaje.movePointLeft(2));
    }

    /** {@inheritDoc} */
    @Override
    public Costo calcular(Costo costoBase, GrupoSanguineo grupo) {
        return grupo.factor() == FactorRH.NEGATIVO ? costoBase.multiplicadoPor(factor) : costoBase;
    }
}