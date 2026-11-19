package co.edu.poli.Examen2.dominio.modelo;

/**
 * Factor Rh de la sangre.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public enum FactorRH {
    /** Rh positivo. */
    POSITIVO("+"),
    /** Rh negativo. */
    NEGATIVO("-");

    private final String simbolo;

    FactorRH(String simbolo) {
        this.simbolo = simbolo;
    }

    /**
     * Obtiene el símbolo clínico del factor.
     *
     * @return {@code "+"} o {@code "-"}
     */
    public String simbolo() {
        return simbolo;
    }
}
