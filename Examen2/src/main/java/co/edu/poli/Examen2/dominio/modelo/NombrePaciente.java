package co.edu.poli.Examen2.dominio.modelo;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;

/**
 * Nombre completo del paciente. Regla de negocio: no vacío y máximo 100 caracteres.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class NombrePaciente {

    private static final int LONGITUD_MAXIMA = 100;

    private final String valor;

    private NombrePaciente(String valor) {
        this.valor = valor;
    }

    /**
     * Crea un nombre validado.
     *
     * @param texto nombre ingresado
     * @return el nombre válido, sin espacios sobrantes
     * @throws ReglaNegocioException si es nulo, vacío o excede la longitud máxima
     */
    public static NombrePaciente de(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del paciente es obligatorio.");
        }
        String limpio = texto.trim();
        if (limpio.length() > LONGITUD_MAXIMA) {
            throw new ReglaNegocioException(
                "El nombre no puede superar " + LONGITUD_MAXIMA + " caracteres.");
        }
        return new NombrePaciente(limpio);
    }

    /** @return el nombre como texto */
    public String valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof NombrePaciente && valor.equals(((NombrePaciente) o).valor));
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
