// adaptadores/salida/csv/ExamenRespaldoCsvAdapter.java
package co.edu.poli.Examen2.adaptadores.salida.csv;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import co.edu.poli.Examen2.dominio.modelo.CodigoExamen;
import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.GrupoSanguineo;
import co.edu.poli.Examen2.dominio.modelo.NombrePaciente;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import co.edu.poli.Examen2.puertos.salida.ExamenRespaldoPuerto;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <b>Patrón Adapter (Estructural): Adapter de objeto.</b>
 * Implementa el puerto ({@link ExamenRespaldoPuerto}, el <i>Target</i>) y traduce sus
 * operaciones a las del {@link ClienteCsv} (el <i>Adaptee</i>), convirtiendo entre
 * {@link ExamenSangre} y filas de texto. Al reconstruir, pasa por las fábricas del dominio,
 * así que un CSV con datos inválidos es rechazado.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class ExamenRespaldoCsvAdapter implements ExamenRespaldoPuerto {

    private static final String[] ENCABEZADO =
        {"codigo", "paciente", "costo", "tipoSangre", "factorRh"};

    private final ClienteCsv cliente;
    private final Path ruta;

    /**
     * Crea el adaptador.
     *
     * @param cliente componente CSV adaptado; no nulo
     * @param ruta    archivo de respaldo; no nulo
     * @throws NullPointerException si algún argumento es {@code null}
     */
    public ExamenRespaldoCsvAdapter(ClienteCsv cliente, Path ruta) {
        this.cliente = Objects.requireNonNull(cliente);
        this.ruta = Objects.requireNonNull(ruta);
    }

    /** {@inheritDoc} */
    @Override
    public void guardarRespaldo(List<ExamenSangre> examenes) {
        List<String[]> filas = new ArrayList<>();
        filas.add(ENCABEZADO);
        for (ExamenSangre e : examenes) {
            filas.add(new String[] {
                e.codigo().valor(),
                e.paciente().valor(),
                e.costo().monto().toPlainString(),
                e.grupoSanguineo().tipo().name(),
                e.grupoSanguineo().factor().name()});
        }
        try {
            cliente.escribir(ruta, filas);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo escribir el respaldo CSV en " + ruta + ".", ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<ExamenSangre> cargarRespaldo() {
        if (!Files.exists(ruta)) {
            throw new IllegalStateException("No existe un respaldo en " + ruta + ". Serialice primero.");
        }
        List<String[]> filas;
        try {
            filas = cliente.leer(ruta);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo leer el respaldo CSV de " + ruta + ".", ex);
        }
        if (filas.isEmpty() || !Arrays.equals(filas.get(0), ENCABEZADO)) {
            throw new IllegalStateException("El archivo CSV no tiene el encabezado esperado.");
        }
        List<ExamenSangre> examenes = new ArrayList<>();
        for (int i = 1; i < filas.size(); i++) {
            String[] f = filas.get(i);
            if (f.length != ENCABEZADO.length) {
                throw new IllegalStateException("Fila " + (i + 1) + " inválida en el respaldo CSV.");
            }
            try {
                examenes.add(ExamenSangre.registrar(
                    CodigoExamen.de(f[0]),
                    NombrePaciente.de(f[1]),
                    Costo.de(new BigDecimal(f[2])),
                    new GrupoSanguineo(TipoSangre.valueOf(f[3]), FactorRH.valueOf(f[4]))));
            } catch (ReglaNegocioException | IllegalArgumentException ex) {
                throw new IllegalStateException(
                    "Datos inválidos en la fila " + (i + 1) + ": " + ex.getMessage(), ex);
            }
        }
        return Collections.unmodifiableList(examenes);
    }
}