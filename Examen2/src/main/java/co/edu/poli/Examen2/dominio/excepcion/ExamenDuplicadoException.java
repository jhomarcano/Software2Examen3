package co.edu.poli.Examen2.dominio.excepcion;

/**
 * Se lanza al intentar registrar un examen cuyo código ya existe.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class ExamenDuplicadoException extends ReglaNegocioException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción indicando el código repetido.
     *
     * @param codigo código que ya está registrado
     */
    public ExamenDuplicadoException(String codigo) {
        super("Ya existe un examen con el código " + codigo + ".");
    }
}
