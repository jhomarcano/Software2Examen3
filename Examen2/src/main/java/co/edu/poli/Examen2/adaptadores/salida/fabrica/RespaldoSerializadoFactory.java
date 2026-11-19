// adaptadores/salida/fabrica/RespaldoSerializadoFactory.java
package co.edu.poli.Examen2.adaptadores.salida.fabrica;

import co.edu.poli.Examen2.adaptadores.salida.archivo.ExamenRespaldoArchivoAdapter;
import co.edu.poli.Examen2.puertos.salida.ExamenRespaldoPuerto;
import java.nio.file.Path;

/**
 * <b>Concrete Creator:</b> produce el adaptador de serialización nativa de Java.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class RespaldoSerializadoFactory implements RespaldoFactory {

    /** {@inheritDoc} */
    @Override
    public FormatoRespaldo formato() {
        return FormatoRespaldo.SERIALIZADO;
    }

    /** {@inheritDoc} */
    @Override
    public ExamenRespaldoPuerto crear(Path carpeta) {
        return new ExamenRespaldoArchivoAdapter(carpeta.resolve("examenes." + formato().extension()));
    }
}