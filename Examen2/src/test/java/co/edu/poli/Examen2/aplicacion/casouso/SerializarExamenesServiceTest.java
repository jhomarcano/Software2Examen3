package co.edu.poli.Examen2.aplicacion.casouso;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.adaptadores.salida.memoria.ExamenRepositorioMemoria;
import co.edu.poli.Examen2.soporte.Fixtures;
import co.edu.poli.Examen2.soporte.RespaldoFalso;
import org.junit.jupiter.api.Test;

class SerializarExamenesServiceTest {

    @Test
    void entregaTodosLosExamenesAlPuertoYDevuelveLaCantidad() {
        ExamenRepositorioMemoria repo = new ExamenRepositorioMemoria();
        repo.guardar(Fixtures.examen("AAA-001"));
        repo.guardar(Fixtures.examen("AAA-002"));
        RespaldoFalso respaldo = new RespaldoFalso();

        int cantidad = new SerializarExamenesService(repo, respaldo).serializar();

        assertEquals(2, cantidad);
        assertEquals(repo.listarTodos(), respaldo.guardado);
    }

    @Test
    void conRepositorioVacioRespaldaCeroExamenes() {
        RespaldoFalso respaldo = new RespaldoFalso();
        int cantidad = new SerializarExamenesService(new ExamenRepositorioMemoria(), respaldo).serializar();

        assertEquals(0, cantidad);
        assertTrue(respaldo.guardado.isEmpty());
    }

    @Test
    void propagaElFalloDelPuerto() {
        RespaldoFalso respaldo = new RespaldoFalso();
        respaldo.fallo = new IllegalStateException("disco lleno");
        SerializarExamenesService servicio =
            new SerializarExamenesService(new ExamenRepositorioMemoria(), respaldo);

        IllegalStateException ex = assertThrows(IllegalStateException.class, servicio::serializar);
        assertEquals("disco lleno", ex.getMessage());
    }

    @Test
    void rechazaDependenciasNulas() {
        assertThrows(NullPointerException.class,
            () -> new SerializarExamenesService(null, new RespaldoFalso()));
        assertThrows(NullPointerException.class,
            () -> new SerializarExamenesService(new ExamenRepositorioMemoria(), null));
    }
}
