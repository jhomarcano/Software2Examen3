// dominio/tarifa/PoliticaTarifa.java
package co.edu.poli.Examen2.dominio.tarifa;

import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.GrupoSanguineo;

/**
 * <b>Patrón Strategy (Comportamiento): interfaz Strategy.</b>
 * Encapsula una política para calcular el costo final de un examen a partir
 * de su costo base y del grupo sanguíneo. Es una regla de negocio pura, por
 * eso pertenece al dominio y no depende de ninguna tecnología.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public interface PoliticaTarifa {

    /**
     * Calcula el costo final que se cobrará.
     *
     * @param costoBase costo base ingresado; no nulo
     * @param grupo     grupo sanguíneo del paciente; no nulo
     * @return el costo final, siempre válido (mayor que cero)
     */
    Costo calcular(Costo costoBase, GrupoSanguineo grupo);
}