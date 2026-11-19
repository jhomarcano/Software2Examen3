package co.edu.poli.Examen2.aplicacion.casouso;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.adaptadores.salida.memoria.ExamenRepositorioMemoria;
import co.edu.poli.Examen2.dominio.modelo.CodigoExamen;
import co.edu.poli.Examen2.soporte.Fixtures;
import co.edu.poli.Examen2.soporte.RespaldoFalso;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class DeserializarExamenesServiceTest {

    @Test
    void reemplazaElContenidoDelRepositorio() {
        ExamenRepositorioMemoria repo = new ExamenRepositorioMemoria();
        repo.guardar(Fixtures.examen("OLD-001"));
        RespaldoFalso respaldo = new RespaldoFalso();
        respaldo.aCargar = Arrays.asList(Fixtures.examen("NEW-001"), Fixtures.examen("NEW-002"));

        int cantidad = new DeserializarExamenesService(repo, respaldo).deserializar();

        assertEquals(2, cantidad);
        assertEquals(2, repo.listarTodos().size());
        assertFalse(repo.existePorCodigo(CodigoExamen.de("OLD-001")));
        assertTrue(repo.existePorCodigo(CodigoExamen.de("NEW-001")));
    }

    @Test
    void siFallaLaLecturaElRepositorioQuedaIntacto() {
        ExamenRepositorioMemoria repo = new ExamenRepositorioMemoria();
        repo.guardar(Fixtures.examen("OLD-001"));
        RespaldoFalso respaldo = new RespaldoFalso();
        respaldo.fallo = new IllegalStateException("No existe un respaldo");

        DeserializarExamenesService servicio = new DeserializarExamenesService(repo, respaldo);

        assertThrows(IllegalStateException.class, servicio::deserializar);
        assertTrue(repo.existePorCodigo(CodigoExamen.de("OLD-001")));
    }

    @Test
    void rechazaDependenciasNulas() {
        assertThrows(NullPointerException.class,
            () -> new DeserializarExamenesService(null, new RespaldoFalso()));
        assertThrows(NullPointerException.class,
            () -> new DeserializarExamenesService(new ExamenRepositorioMemoria(), null));
    }
}
