package co.edu.poli.Examen2.puertos.entrada;

import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import java.math.BigDecimal;

/**
 * Datos de entrada del caso de uso {@link RegistrarExamenUseCase}.
 * Contiene datos crudos: la validación es responsabilidad del dominio.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class RegistrarExamenComando {

    private final String codigo;
    private final String nombrePaciente;
    private final BigDecimal costo;
    private final TipoSangre tipoSangre;
    private final FactorRH factorRH;

    /**
     * Crea el comando con los datos ingresados por el usuario.
     *
     * @param codigo         código ingresado por el usuario
     * @param nombrePaciente nombre ingresado
     * @param costo          costo ya convertido a número
     * @param tipoSangre     tipo de sangre elegido
     * @param factorRH       factor Rh elegido
     */
    public RegistrarExamenComando(String codigo, String nombrePaciente, BigDecimal costo,
                                  TipoSangre tipoSangre, FactorRH factorRH) {
        this.codigo = codigo;
        this.nombrePaciente = nombrePaciente;
        this.costo = costo;
        this.tipoSangre = tipoSangre;
        this.factorRH = factorRH;
    }

    /** @return código en texto */
    public String codigo() { return codigo; }

    /** @return nombre del paciente en texto */
    public String nombrePaciente() { return nombrePaciente; }

    /** @return costo numérico */
    public BigDecimal costo() { return costo; }

    /** @return tipo de sangre */
    public TipoSangre tipoSangre() { return tipoSangre; }

    /** @return factor Rh */
    public FactorRH factorRH() { return factorRH; }
}
