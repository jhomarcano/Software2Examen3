package co.edu.poli.Examen2.adaptadores.salida.memoria;

import co.edu.poli.Examen2.dominio.modelo.CodigoExamen;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.puertos.salida.ExamenRepositorio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Adaptador de salida que implementa {@link ExamenRepositorio} en memoria.
 * Mantiene el orden de inserción. Puede sustituirse por un adaptador de base
 * de datos sin modificar la aplicación ni el dominio.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class ExamenRepositorioMemoria implements ExamenRepositorio {

    private final Map<CodigoExamen, ExamenSangre> almacen = new LinkedHashMap<>();

    /** {@inheritDoc} */
    @Override
    public void guardar(ExamenSangre examen) {
        almacen.put(examen.codigo(), examen);
    }

    /** {@inheritDoc} */
    @Override
    public boolean existePorCodigo(CodigoExamen codigo) {
        return almacen.containsKey(codigo);
    }

    /** {@inheritDoc} */
    @Override
    public List<ExamenSangre> listarTodos() {
        return Collections.unmodifiableList(new ArrayList<>(almacen.values()));
    }

    /** {@inheritDoc} */
    @Override
    public void reemplazarTodos(List<ExamenSangre> examenes) {
        almacen.clear();
        examenes.forEach(this::guardar);
    }
}
