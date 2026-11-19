package co.edu.poli.Examen2.dominio.modelo;

import java.util.Objects;

/**
 * Objeto de valor inmutable que combina el tipo de sangre y el factor Rh.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class GrupoSanguineo {

    private final TipoSangre tipo;
    private final FactorRH factor;

    /**
     * Crea un grupo sanguíneo.
     *
     * @param tipo   tipo de sangre; no puede ser {@code null}
     * @param factor factor Rh; no puede ser {@code null}
     * @throws NullPointerException si algún argumento es {@code null}
     */
    public GrupoSanguineo(TipoSangre tipo, FactorRH factor) {
        this.tipo = Objects.requireNonNull(tipo, "El tipo de sangre es obligatorio");
        this.factor = Objects.requireNonNull(factor, "El factor RH es obligatorio");
    }

    /** @return el tipo de sangre */
    public TipoSangre tipo() {
        return tipo;
    }

    /** @return el factor Rh */
    public FactorRH factor() {
        return factor;
    }

    /**
     * Representación clínica del grupo.
     *
     * @return por ejemplo {@code "AB+"} u {@code "O-"}
     */
    public String simbolo() {
        return tipo.name() + factor.simbolo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrupoSanguineo)) return false;
        GrupoSanguineo otro = (GrupoSanguineo) o;
        return tipo == otro.tipo && factor == otro.factor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, factor);
    }

    @Override
    public String toString() {
        return simbolo();
    }
}
