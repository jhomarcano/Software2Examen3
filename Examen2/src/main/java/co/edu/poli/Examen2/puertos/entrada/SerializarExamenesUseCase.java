package co.edu.poli.Examen2.puertos.entrada;

/**
 * Puerto de entrada para respaldar los exámenes en un almacenamiento persistente.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public interface SerializarExamenesUseCase {

    /**
     * Respalda todos los exámenes registrados.
     *
     * @return cantidad de exámenes respaldados
     * @throws IllegalStateException si el respaldo no pudo escribirse
     */
    int serializar();
}
