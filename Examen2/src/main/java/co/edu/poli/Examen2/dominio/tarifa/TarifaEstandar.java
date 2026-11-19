// dominio/tarifa/TarifaEstandar.java
package co.edu.poli.Examen2.dominio.tarifa;

import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.GrupoSanguineo;

/**
 * <b>Strategy concreta:</b> sin ajustes, el costo final es el costo base.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class TarifaEstandar implements PoliticaTarifa {

    /** {@inheritDoc} */
    @Override
    public Costo calcular(Costo costoBase, GrupoSanguineo grupo) {
        return costoBase;
    }
}