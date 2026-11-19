package co.edu.poli.Examen2.aplicacion.casouso;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.adaptadores.salida.memoria.ExamenRepositorioMemoria;
import co.edu.poli.Examen2.soporte.Fixtures;
import org.junit.jupiter.api.Test;

class ListarExamenesServiceTest {

    @Test
    void devuelveListaVaciaSiNoHayExamenes() {
        ListarExamenesService servicio = new ListarExamenesService(new ExamenRepositorioMemoria());
        assertTrue(servicio.listar().isEmpty());
    }

    @Test
    void devuelveLosExamenesEnOrdenDeInsercion() {
        ExamenRepositorioMemoria repo = new ExamenRepositorioMemoria();
        repo.guardar(Fixtures.examen("BBB-002"));
        repo.guardar(Fixtures.examen("AAA-001"));

        ListarExamenesService servicio = new ListarExamenesService(repo);

        assertEquals(2, servicio.listar().size());
        assertEquals("BBB-002", servicio.listar().get(0).codigo().valor());
        assertEquals("AAA-001", servicio.listar().get(1).codigo().valor());
    }

    @Test
    void rechazaRepositorioNulo() {
        assertThrows(NullPointerException.class, () -> new ListarExamenesService(null));
    }
}
