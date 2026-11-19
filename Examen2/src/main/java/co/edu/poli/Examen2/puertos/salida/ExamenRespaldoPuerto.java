package co.edu.poli.Examen2.puertos.salida;

import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import java.util.List;

/**
 * Puerto de salida para guardar y recuperar respaldos de exámenes.
 * Abstrae el mecanismo (serialización Java, JSON, nube).
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public interface ExamenRespaldoPuerto {

    /**
     * Escribe el respaldo, sobrescribiendo el anterior.
     *
     * @param examenes exámenes a respaldar
     * @throws IllegalStateException si el respaldo no puede escribirse
     */
    void guardarRespaldo(List<ExamenSangre> examenes);

    /**
     * Lee el último respaldo.
     *
     * @return los exámenes respaldados
     * @throws IllegalStateException si no existe respaldo o no puede leerse
     */
    List<ExamenSangre> cargarRespaldo();
}
