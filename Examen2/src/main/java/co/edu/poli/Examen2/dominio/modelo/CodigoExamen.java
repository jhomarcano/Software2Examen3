package co.edu.poli.Examen2.dominio.modelo;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import java.util.regex.Pattern;

/**
 * Identificador de un examen. Regla de negocio: tres letras mayúsculas,
 * un guion y tres dígitos (ej. {@code EXM-001}).
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class CodigoExamen {

    private static final Pattern FORMATO = Pattern.compile("^[A-Z]{3}-\\d{3}$");

    private final String valor;

    private CodigoExamen(String valor) {
        this.valor = valor;
    }

    /**
     * Construye un código validando su formato.
     *
     * @param texto texto ingresado; se normaliza con {@code trim} y mayúsculas
     * @return el código válido
     * @throws ReglaNegocioException si el texto es nulo, vacío o no cumple el formato
     */
    public static CodigoExamen de(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ReglaNegocioException("El código del examen es obligatorio.");
        }
        String normalizado = texto.trim().toUpperCase();
        if (!FORMATO.matcher(normalizado).matches()) {
            throw new ReglaNegocioException(
                "El código debe tener el formato XXX-000 (ej. EXM-001).");
        }
        return new CodigoExamen(normalizado);
    }

    /** @return el valor textual del código */
    public String valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof CodigoExamen && valor.equals(((CodigoExamen) o).valor));
    }

    @Override
    public int hashCode() {
        return valor.hashCode();
    }

    @Override
    public String toString() {
        return valor;
    }
}
