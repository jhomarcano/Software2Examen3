// adaptadores/salida/fabrica/RespaldoCsvFactory.java
package co.edu.poli.Examen2.adaptadores.salida.fabrica;

import co.edu.poli.Examen2.adaptadores.salida.csv.ClienteCsv;
import co.edu.poli.Examen2.adaptadores.salida.csv.ExamenRespaldoCsvAdapter;
import co.edu.poli.Examen2.puertos.salida.ExamenRespaldoPuerto;
import java.nio.file.Path;

/**
 * <b>Concrete Creator:</b> produce el adaptador CSV, ensamblando el componente
 * adaptado ({@link ClienteCsv}) con su {@link ExamenRespaldoCsvAdapter}.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public final class RespaldoCsvFactory implements RespaldoFactory {

    /** {@inheritDoc} */
    @Override
    public FormatoRespaldo formato() {
        return FormatoRespaldo.CSV;
    }

    /** {@inheritDoc} */
    @Override
    public ExamenRespaldoPuerto crear(Path carpeta) {
        return new ExamenRespaldoCsvAdapter(new ClienteCsv(),
            carpeta.resolve("examenes." + formato().extension()));
    }
}