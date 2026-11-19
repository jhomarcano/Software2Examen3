package co.edu.poli.Examen2.aplicacion.casouso;

import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.puertos.entrada.SerializarExamenesUseCase;
import co.edu.poli.Examen2.puertos.salida.ExamenRepositorio;
import co.edu.poli.Examen2.puertos.salida.ExamenRespaldoPuerto;
import java.util.List;
import java.util.Objects;

/**
 * Implementación del caso de uso «Serializar exámenes»: toma los exámenes del
 * repositorio y los entrega al puerto de respaldo. No sabe cómo se escribe el respaldo.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class SerializarExamenesService implements SerializarExamenesUseCase {

    private final ExamenRepositorio repositorio;
    private final ExamenRespaldoPuerto respaldo;

    /**
     * Crea el servicio con sus dependencias inyectadas.
     *
     * @param repositorio puerto de salida de persistencia; no nulo
     * @param respaldo    puerto de salida de respaldo; no nulo
     * @throws NullPointerException si algún argumento es {@code null}
     */
    public SerializarExamenesService(ExamenRepositorio repositorio, ExamenRespaldoPuerto respaldo) {
        this.repositorio = Objects.requireNonNull(repositorio);
        this.respaldo = Objects.requireNonNull(respaldo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int serializar() {
        List<ExamenSangre> examenes = repositorio.listarTodos();
        respaldo.guardarRespaldo(examenes);
        return examenes.size();
    }
}
