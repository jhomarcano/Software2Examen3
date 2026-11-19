// aplicacion/casouso/RegistrarExamenService.java
package co.edu.poli.Examen2.aplicacion.casouso;

import co.edu.poli.Examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.Examen2.dominio.modelo.CodigoExamen;
import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.dominio.modelo.GrupoSanguineo;
import co.edu.poli.Examen2.dominio.modelo.NombrePaciente;
import co.edu.poli.Examen2.dominio.tarifa.PoliticaTarifa;
import co.edu.poli.Examen2.puertos.entrada.RegistrarExamenComando;
import co.edu.poli.Examen2.puertos.entrada.RegistrarExamenUseCase;
import co.edu.poli.Examen2.puertos.salida.ExamenRepositorio;
import java.util.Objects;

/**
 * Caso de uso «Registrar examen». Actúa como <b>Context</b> del patrón Strategy:
 * delega el cálculo del costo final en una {@link PoliticaTarifa} sin conocer su
 * implementación concreta.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 2.0
 */
public class RegistrarExamenService implements RegistrarExamenUseCase {

    private final ExamenRepositorio repositorio;
    private final PoliticaTarifa politicaTarifa;

    /**
     * Inyecta las dependencias por constructor, tipadas por interfaz.
     *
     * @param repositorio    puerto de salida de persistencia; no nulo
     * @param politicaTarifa estrategia de cálculo del costo final; no nula
     * @throws NullPointerException si algún argumento es {@code null}
     */
    public RegistrarExamenService(ExamenRepositorio repositorio, PoliticaTarifa politicaTarifa) {
        this.repositorio = Objects.requireNonNull(repositorio);
        this.politicaTarifa = Objects.requireNonNull(politicaTarifa);
    }

    /** {@inheritDoc} */
    @Override
    public ExamenSangre registrar(RegistrarExamenComando comando) {
        CodigoExamen codigo = CodigoExamen.de(comando.codigo());
        NombrePaciente paciente = NombrePaciente.de(comando.nombrePaciente());
        Costo costoBase = Costo.de(comando.costo());
        GrupoSanguineo grupo = new GrupoSanguineo(comando.tipoSangre(), comando.factorRH());

        if (repositorio.existePorCodigo(codigo)) {
            throw new ExamenDuplicadoException(codigo.valor());
        }

        Costo costoFinal = politicaTarifa.calcular(costoBase, grupo);   // <- Strategy
        ExamenSangre examen = ExamenSangre.registrar(codigo, paciente, costoFinal, grupo);
        repositorio.guardar(examen);
        return examen;
    }
}