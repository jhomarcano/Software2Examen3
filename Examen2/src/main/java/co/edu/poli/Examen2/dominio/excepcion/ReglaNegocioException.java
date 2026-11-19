package co.edu.poli.Examen2.dominio.excepcion;

/**
 * Se lanza cuando se viola una regla de negocio del dominio.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class ReglaNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con un mensaje legible.
     *
     * @param mensaje descripción de la regla violada
     */
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
