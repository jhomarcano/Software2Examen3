// adaptadores/salida/fabrica/RespaldoFactory.java
package co.edu.poli.Examen2.adaptadores.salida.fabrica;

import co.edu.poli.Examen2.puertos.salida.ExamenRespaldoPuerto;
import java.nio.file.Path;

/**
 * <b>Patrón Factory Method (Creacional): Creator.</b> Declara el método de fábrica que
 * crea el producto ({@link ExamenRespaldoPuerto}). Cada fábrica concreta decide qué
 * adaptador instanciar, así quien la usa no conoce las clases concretas.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public interface RespaldoFactory {

    /** @return el formato que produce esta fábrica */
    FormatoRespaldo formato();

    /**
     * <b>Método de fábrica.</b> Crea el adaptador de respaldo.
     *
     * @param carpeta carpeta donde se guardará el archivo {@code examenes.<extensión>}; no nula
     * @return un adaptador listo para usar
     */
    ExamenRespaldoPuerto crear(Path carpeta);
}