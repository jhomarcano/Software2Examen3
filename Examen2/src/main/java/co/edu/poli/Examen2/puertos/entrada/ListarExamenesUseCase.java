package co.edu.poli.Examen2.puertos.entrada;

import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import java.util.List;

/**
 * Puerto de entrada para consultar los exámenes registrados.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public interface ListarExamenesUseCase {

    /**
     * Obtiene todos los exámenes registrados.
     *
     * @return lista de solo lectura; vacía si no hay exámenes
     */
    List<ExamenSangre> listar();
}
