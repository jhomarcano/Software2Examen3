package co.edu.poli.Examen2.aplicacion.casouso;

import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.puertos.entrada.DeserializarExamenesUseCase;
import co.edu.poli.Examen2.puertos.salida.ExamenRepositorio;
import co.edu.poli.Examen2.puertos.salida.ExamenRespaldoPuerto;
import java.util.List;
import java.util.Objects;

/**
 * Implementación del caso de uso «Deserializar exámenes»: lee el respaldo a través
 * del puerto y reemplaza el contenido del repositorio.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class DeserializarExamenesService implements DeserializarExamenesUseCase {

    private final ExamenRepositorio repositorio;
    private final ExamenRespaldoPuerto respaldo;

    /**
     * Crea el servicio con sus dependencias inyectadas.
     *
     * @param repositorio puerto de salida de persistencia; no nulo
     * @param respaldo    puerto de salida de respaldo; no nulo
     * @throws NullPointerException si algún argumento es {@code null}
     */
    public DeserializarExamenesService(ExamenRepositorio repositorio, ExamenRespaldoPuerto respaldo) {
        this.repositorio = Objects.requireNonNull(repositorio);
        this.respaldo = Objects.requireNonNull(respaldo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deserializar() {
        List<ExamenSangre> examenes = respaldo.cargarRespaldo();
        repositorio.reemplazarTodos(examenes);
        return examenes.size();
    }
}
