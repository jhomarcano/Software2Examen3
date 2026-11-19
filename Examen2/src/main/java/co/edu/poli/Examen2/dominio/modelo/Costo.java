package co.edu.poli.Examen2.dominio.modelo;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Valor monetario de un examen. Regla de negocio: estrictamente mayor que cero.
 * Se usa {@link BigDecimal} para evitar errores de redondeo de {@code double}.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class Costo {

    private final BigDecimal monto;

    private Costo(BigDecimal monto) {
        this.monto = monto;
    }
    
    /**
     * Devuelve un nuevo costo resultado de multiplicar este monto por un factor.
     * El resultado vuelve a validarse (debe seguir siendo mayor que cero).
     *
     * @param factor factor multiplicador; no nulo
     * @return el nuevo costo, con dos decimales
     * @throws NullPointerException  si {@code factor} es {@code null}
     * @throws ReglaNegocioException si el resultado es menor o igual a cero
     */
    public Costo multiplicadoPor(BigDecimal factor) {
        Objects.requireNonNull(factor, "El factor es obligatorio");
        return Costo.de(monto.multiply(factor));
    }

    /**
     * Crea un costo validado, con dos decimales.
     *
     * @param monto valor monetario
     * @return el costo válido
     * @throws ReglaNegocioException si es nulo o menor o igual a cero
     */
    public static Costo de(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new ReglaNegocioException("El costo debe ser mayor que cero.");
        }
        return new Costo(monto.setScale(2, RoundingMode.HALF_UP));
    }

    /** @return el monto con dos decimales */
    public BigDecimal monto() {
        return monto;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Costo && monto.compareTo(((Costo) o).monto) == 0);
    }

    @Override
    public int hashCode() {
        return monto.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString() {
        return monto.toPlainString();
    }
}
