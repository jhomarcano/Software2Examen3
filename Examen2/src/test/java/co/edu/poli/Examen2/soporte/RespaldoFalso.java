package co.edu.poli.Examen2.soporte;

import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.puertos.salida.ExamenRespaldoPuerto;
import java.util.ArrayList;
import java.util.List;

/** Doble de prueba del puerto de respaldo: guarda en memoria y puede simular fallos. */
public class RespaldoFalso implements ExamenRespaldoPuerto {

    public List<ExamenSangre> guardado;
    public List<ExamenSangre> aCargar = new ArrayList<>();
    public RuntimeException fallo;

    @Override
    public void guardarRespaldo(List<ExamenSangre> examenes) {
        if (fallo != null) {
            throw fallo;
        }
        guardado = new ArrayList<>(examenes);
    }

    @Override
    public List<ExamenSangre> cargarRespaldo() {
        if (fallo != null) {
            throw fallo;
        }
        return aCargar;
    }
}
