package co.edu.poli.Examen2.puertos.entrada;

import co.edu.poli.Examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;

/**
 * Puerto de entrada (driving port): contrato del caso de uso
 * «Registrar un examen de sangre». Lo invocan los adaptadores de entrada
 * (JavaFX, consola, REST) y lo implementa la capa de aplicación.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public interface RegistrarExamenUseCase {

    /**
     * Valida y registra un nuevo examen.
     *
     * @param comando datos del examen a registrar
     * @return el examen registrado
     * @throws ReglaNegocioException    si algún dato incumple las reglas del dominio
     * @throws ExamenDuplicadoException si ya existe un examen con el mismo código
     */
    ExamenSangre registrar(RegistrarExamenComando comando);
}
