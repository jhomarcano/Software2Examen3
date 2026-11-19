// adaptadores/salida/fabrica/FormatoRespaldo.java
package co.edu.poli.Examen2.adaptadores.salida.fabrica;

/**
 * Formatos de respaldo disponibles. Es un detalle de infraestructura, por eso no
 * está en el dominio.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public enum FormatoRespaldo {
    /** Serialización nativa de Java ({@code .ser}). */
    SERIALIZADO("ser"),
    /** Texto separado por punto y coma ({@code .csv}). */
    CSV("csv");

    private final String extension;

    FormatoRespaldo(String extension) {
        this.extension = extension;
    }

    /** @return la extensión del archivo, sin punto */
    public String extension() {
        return extension;
    }

    /**
     * Interpreta un texto de configuración.
     *
     * @param texto nombre del formato; si es nulo o vacío se usa {@link #SERIALIZADO}
     * @return el formato correspondiente
     * @throws IllegalArgumentException si el formato no existe
     */
    public static FormatoRespaldo desde(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return SERIALIZADO;
        }
        try {
            return valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de respaldo no soportado: " + texto, e);
        }
    }
}