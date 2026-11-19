// adaptadores/salida/fabrica/RespaldoFactories.java
package co.edu.poli.Examen2.adaptadores.salida.fabrica;

/**
 * Selector de fábricas de respaldo según el formato configurado. Es el único lugar que
 * enumera las fábricas concretas.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class RespaldoFactories {

    private RespaldoFactories() {
    }

    /**
     * Obtiene la fábrica para un formato.
     *
     * @param formato formato deseado; no nulo
     * @return la fábrica correspondiente
     */
    public static RespaldoFactory para(FormatoRespaldo formato) {
        switch (formato) {
            case CSV:
                return new RespaldoCsvFactory();
            case SERIALIZADO:
            default:
                return new RespaldoSerializadoFactory();
        }
    }

    /**
     * Lee la propiedad de sistema {@code respaldo.formato} (por ejemplo
     * {@code -Drespaldo.formato=csv}); por defecto usa {@code SERIALIZADO}.
     *
     * @return la fábrica configurada
     * @throws IllegalArgumentException si el formato configurado no existe
     */
    public static RespaldoFactory desdeConfiguracion() {
        return para(FormatoRespaldo.desde(System.getProperty("respaldo.formato")));
    }
}