package co.edu.poli.Examen2.puertos.salida;

import co.edu.poli.Examen2.dominio.modelo.CodigoExamen;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import java.util.List;

/**
 * Puerto de salida (driven port): contrato de persistencia de exámenes.
 * Lo define la aplicación según lo que necesita; lo implementa un adaptador
 * (memoria, archivo, base de datos). Así la aplicación no depende de la
 * tecnología de almacenamiento.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public interface ExamenRepositorio {

    /**
     * Guarda un examen.
     *
     * @param examen examen a guardar; no nulo
     */
    void guardar(ExamenSangre examen);

    /**
     * Indica si existe un examen con el código dado.
     *
     * @param codigo código a consultar
     * @return {@code true} si existe
     */
    boolean existePorCodigo(CodigoExamen codigo);

    /**
     * Obtiene todos los exámenes almacenados.
     *
     * @return lista de solo lectura, en orden de inserción
     */
    List<ExamenSangre> listarTodos();

    /**
     * Reemplaza todo el contenido almacenado.
     *
     * @param examenes nuevo contenido
     */
    void reemplazarTodos(List<ExamenSangre> examenes);
}
