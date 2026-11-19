package co.edu.poli.Examen2.adaptadores.salida.memoria;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.poli.Examen2.dominio.modelo.CodigoExamen;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import co.edu.poli.Examen2.soporte.Fixtures;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExamenRepositorioMemoriaTest {

    private ExamenRepositorioMemoria repo;

    @BeforeEach
    void preparar() {
        repo = new ExamenRepositorioMemoria();
    }

    @Test
    void empiezaVacio() {
        assertTrue(repo.listarTodos().isEmpty());
        assertFalse(repo.existePorCodigo(CodigoExamen.de("EXM-001")));
    }

    @Test
    void guardaYConsultaPorCodigo() {
        repo.guardar(Fixtures.examen("EXM-001"));
        assertTrue(repo.existePorCodigo(CodigoExamen.de("EXM-001")));
        assertFalse(repo.existePorCodigo(CodigoExamen.de("EXM-002")));
    }

    @Test
    void mantieneElOrdenDeInsercion() {
        repo.guardar(Fixtures.examen("CCC-003"));
        repo.guardar(Fixtures.examen("AAA-001"));
        repo.guardar(Fixtures.examen("BBB-002"));

        assertEquals("CCC-003", repo.listarTodos().get(0).codigo().valor());
        assertEquals("AAA-001", repo.listarTodos().get(1).codigo().valor());
        assertEquals("BBB-002", repo.listarTodos().get(2).codigo().valor());
    }

    @Test
    void guardarConElMismoCodigoSobrescribe() {
        repo.guardar(Fixtures.examen("EXM-001", "Ana", "100", TipoSangre.O, FactorRH.POSITIVO));
        repo.guardar(Fixtures.examen("EXM-001", "Luis", "200", TipoSangre.A, FactorRH.NEGATIVO));

        assertEquals(1, repo.listarTodos().size());
        assertEquals("Luis", repo.listarTodos().get(0).paciente().valor());
    }

    @Test
    void laListaDevueltaEsDeSoloLectura() {
        repo.guardar(Fixtures.examen("EXM-001"));
        assertThrows(UnsupportedOperationException.class,
            () -> repo.listarTodos().add(Fixtures.examen("EXM-002")));
    }

    @Test
    void reemplazarTodosDescartaLoAnterior() {
        repo.guardar(Fixtures.examen("OLD-001"));

        repo.reemplazarTodos(Arrays.asList(Fixtures.examen("NEW-001"), Fixtures.examen("NEW-002")));

        assertEquals(2, repo.listarTodos().size());
        assertFalse(repo.existePorCodigo(CodigoExamen.de("OLD-001")));
    }

    @Test
    void reemplazarConListaVaciaLimpiaElRepositorio() {
        repo.guardar(Fixtures.examen("OLD-001"));
        repo.reemplazarTodos(Collections.<ExamenSangre>emptyList());
        assertTrue(repo.listarTodos().isEmpty());
    }
}
