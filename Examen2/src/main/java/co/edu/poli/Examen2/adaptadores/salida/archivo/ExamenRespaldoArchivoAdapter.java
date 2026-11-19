package co.edu.poli.Examen2.adaptadores.salida.archivo;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.puertos.salida.ExamenRespaldoPuerto;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Adaptador de salida que implementa {@link ExamenRespaldoPuerto} mediante
 * serialización nativa de Java sobre un archivo. Convierte las entidades a
 * {@link ExamenSerializableDto} antes de escribir, y al leer restringe las clases
 * permitidas con un filtro de deserialización.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class ExamenRespaldoArchivoAdapter implements ExamenRespaldoPuerto {

    /**
     * Lista blanca de clases que se aceptan al deserializar. Se incluye
     * {@code java.lang.Object} porque {@code ArrayList} serializa su arreglo
     * interno como {@code Object[]}; cualquier otra clase se rechaza ({@code !*}).
     */
    private static final String FILTRO =
        "java.util.ArrayList;java.lang.Object;java.math.BigDecimal;java.math.BigInteger;"
        + "java.lang.Number;java.lang.String;" + ExamenSerializableDto.class.getName() + ";!*";

    private final Path ruta;

    /**
     * Crea el adaptador apuntando a un archivo de respaldo.
     *
     * @param ruta ubicación del archivo; no nula
     * @throws NullPointerException si {@code ruta} es {@code null}
     */
    public ExamenRespaldoArchivoAdapter(Path ruta) {
        this.ruta = Objects.requireNonNull(ruta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void guardarRespaldo(List<ExamenSangre> examenes) {
        ArrayList<ExamenSerializableDto> dtos = new ArrayList<>();
        for (ExamenSangre examen : examenes) {
            dtos.add(ExamenSerializableDto.desde(examen));
        }
        try (OutputStream salida = Files.newOutputStream(ruta);
             ObjectOutputStream out = new ObjectOutputStream(salida)) {
            out.writeObject(dtos);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo escribir el respaldo en " + ruta + ".", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExamenSangre> cargarRespaldo() {
        if (!Files.exists(ruta)) {
            throw new IllegalStateException(
                "No existe un respaldo en " + ruta + ". Serialice primero.");
        }
        try (InputStream entrada = Files.newInputStream(ruta);
             ObjectInputStream in = new ObjectInputStream(entrada)) {
            in.setObjectInputFilter(ObjectInputFilter.Config.createFilter(FILTRO));
            Object leido = in.readObject();
            if (!(leido instanceof List<?>)) {
                throw new IllegalStateException("El archivo de respaldo no tiene el formato esperado.");
            }
            List<ExamenSangre> examenes = new ArrayList<>();
            for (Object elemento : (List<?>) leido) {
                if (!(elemento instanceof ExamenSerializableDto)) {
                    throw new IllegalStateException("El archivo de respaldo contiene datos inesperados.");
                }
                examenes.add(((ExamenSerializableDto) elemento).aDominio());
            }
            return Collections.unmodifiableList(examenes);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("No se pudo leer el respaldo de " + ruta + ".", e);
        } catch (ReglaNegocioException | IllegalArgumentException e) {
            throw new IllegalStateException("El respaldo contiene datos inválidos: " + e.getMessage(), e);
        }
    }
}
