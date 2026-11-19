package co.edu.poli.Examen2.soporte;

import co.edu.poli.Examen2.dominio.modelo.CodigoExamen;
import co.edu.poli.Examen2.dominio.modelo.Costo;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.GrupoSanguineo;
import co.edu.poli.Examen2.dominio.modelo.NombrePaciente;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import java.math.BigDecimal;

/** Utilidades para construir objetos de dominio válidos en las pruebas. */
public final class Fixtures {

    private Fixtures() {
    }

    public static ExamenSangre examen(String codigo, String paciente, String costo,
                                      TipoSangre tipo, FactorRH factor) {
        return ExamenSangre.registrar(
            CodigoExamen.de(codigo),
            NombrePaciente.de(paciente),
            Costo.de(new BigDecimal(costo)),
            new GrupoSanguineo(tipo, factor));
    }

    public static ExamenSangre examen(String codigo) {
        return examen(codigo, "Ana Pérez", "100.00", TipoSangre.O, FactorRH.POSITIVO);
    }

    public static GrupoSanguineo grupo(TipoSangre tipo, FactorRH factor) {
        return new GrupoSanguineo(tipo, factor);
    }
}
