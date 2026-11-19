package co.edu.poli.Examen2.adaptadores.salida.archivo;

import co.edu.poli.Examen2.dominio.modelo.CodigoExamen;
import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.GrupoSanguineo;
import co.edu.poli.Examen2.dominio.modelo.NombrePaciente;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Objeto de transferencia serializable que representa un {@link ExamenSangre}
 * en el archivo de respaldo. Existe para que el dominio no tenga que implementar
 * {@link Serializable}: la tecnología de persistencia queda confinada al adaptador.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class ExamenSerializableDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String codigo;
    private final String paciente;
    private final BigDecimal costo;
    private final String tipoSangre;
    private final String factorRh;

    private ExamenSerializableDto(String codigo, String paciente, BigDecimal costo,
                                  String tipoSangre, String factorRh) {
        this.codigo = codigo;
        this.paciente = paciente;
        this.costo = costo;
        this.tipoSangre = tipoSangre;
        this.factorRh = factorRh;
    }

    /**
     * Convierte una entidad de dominio en su DTO.
     *
     * @param examen entidad a convertir; no nula
     * @return el DTO equivalente
     */
    public static ExamenSerializableDto desde(ExamenSangre examen) {
        return new ExamenSerializableDto(
            examen.codigo().valor(),
            examen.paciente().valor(),
            examen.costo().monto(),
            examen.grupoSanguineo().tipo().name(),
            examen.grupoSanguineo().factor().name());
    }

    /**
     * Reconstruye la entidad de dominio. Al pasar por las fábricas del dominio,
     * un archivo manipulado con datos inválidos es rechazado.
     *
     * @return la entidad equivalente
     * @throws co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException si los datos incumplen las reglas
     * @throws IllegalArgumentException si el tipo de sangre o el factor no existen
     */
    public ExamenSangre aDominio() {
        return ExamenSangre.registrar(
            CodigoExamen.de(codigo),
            NombrePaciente.de(paciente),
            Costo.de(costo),
            new GrupoSanguineo(TipoSangre.valueOf(tipoSangre), FactorRH.valueOf(factorRh)));
    }
}
