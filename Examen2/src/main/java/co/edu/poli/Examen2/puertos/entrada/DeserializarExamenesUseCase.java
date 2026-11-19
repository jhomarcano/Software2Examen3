package co.edu.poli.Examen2.puertos.entrada;

/**
 * Puerto de entrada para restaurar los exámenes desde un respaldo.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public interface DeserializarExamenesUseCase {

    /**
     * Restaura los exámenes desde el respaldo, reemplazando los actuales.
     *
     * @return cantidad de exámenes restaurados
     * @throws IllegalStateException si no existe respaldo o no pudo leerse
     */
    int deserializar();
}
