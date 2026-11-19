package co.edu.poli.Examen2.aplicacion.casouso;

import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.puertos.entrada.ListarExamenesUseCase;
import co.edu.poli.Examen2.puertos.salida.ExamenRepositorio;
import java.util.List;
import java.util.Objects;

/**
 * Implementación del caso de uso «Listar exámenes»: consulta el repositorio
 * a través del puerto de salida.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class ListarExamenesService implements ListarExamenesUseCase {

    private final ExamenRepositorio repositorio;

    /**
     * Crea el servicio con su dependencia inyectada.
     *
     * @param repositorio puerto de salida de persistencia; no nulo
     * @throws NullPointerException si {@code repositorio} es {@code null}
     */
    public ListarExamenesService(ExamenRepositorio repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExamenSangre> listar() {
        return repositorio.listarTodos();
    }
}
