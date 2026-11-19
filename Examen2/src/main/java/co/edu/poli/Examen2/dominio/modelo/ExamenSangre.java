package co.edu.poli.Examen2.dominio.modelo;

import java.util.Objects;

/**
 * Entidad raíz del dominio: un examen de sangre solicitado para un paciente.
 * Su identidad es el {@link CodigoExamen}: dos exámenes con el mismo código
 * son el mismo examen. La clase es inmutable y no depende de ninguna
 * tecnología (sin JavaFX, sin {@code java.io}).
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class ExamenSangre {

    private final CodigoExamen codigo;
    private final NombrePaciente paciente;
    private final Costo costo;
    private final GrupoSanguineo grupoSanguineo;

    private ExamenSangre(CodigoExamen codigo, NombrePaciente paciente,
                         Costo costo, GrupoSanguineo grupoSanguineo) {
        this.codigo = codigo;
        this.paciente = paciente;
        this.costo = costo;
        this.grupoSanguineo = grupoSanguineo;
    }

    /**
     * Fábrica que garantiza que el examen se crea siempre en un estado válido.
     * Las reglas de cada campo las validan sus objetos de valor.
     *
     * @param codigo         código único del examen
     * @param paciente       nombre del paciente
     * @param costo          costo del examen
     * @param grupoSanguineo grupo sanguíneo del paciente
     * @return un examen válido
     * @throws NullPointerException si algún argumento es {@code null}
     */
    public static ExamenSangre registrar(CodigoExamen codigo, NombrePaciente paciente,
                                         Costo costo, GrupoSanguineo grupoSanguineo) {
        return new ExamenSangre(
            Objects.requireNonNull(codigo, "El código es obligatorio"),
            Objects.requireNonNull(paciente, "El paciente es obligatorio"),
            Objects.requireNonNull(costo, "El costo es obligatorio"),
            Objects.requireNonNull(grupoSanguineo, "El grupo sanguíneo es obligatorio"));
    }

    /** @return el código (identidad) del examen */
    public CodigoExamen codigo() {
        return codigo;
    }

    /** @return el nombre del paciente */
    public NombrePaciente paciente() {
        return paciente;
    }

    /** @return el costo del examen */
    public Costo costo() {
        return costo;
    }

    /** @return el grupo sanguíneo del paciente */
    public GrupoSanguineo grupoSanguineo() {
        return grupoSanguineo;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof ExamenSangre && codigo.equals(((ExamenSangre) o).codigo));
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return codigo + " | " + paciente + " | " + grupoSanguineo + " | $" + costo;
    }
}
