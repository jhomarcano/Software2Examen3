package co.edu.poli.Examen2.adaptadores.entrada.javafx;

//import co.edu.poli.Examen2.adaptadores.salida.archivo.ExamenRespaldoArchivoAdapter;
import co.edu.poli.Examen2.adaptadores.salida.memoria.ExamenRepositorioMemoria;
import co.edu.poli.Examen2.aplicacion.casouso.DeserializarExamenesService;
import co.edu.poli.Examen2.aplicacion.casouso.ListarExamenesService;
import co.edu.poli.Examen2.aplicacion.casouso.RegistrarExamenService;
import co.edu.poli.Examen2.aplicacion.casouso.SerializarExamenesService;
import co.edu.poli.Examen2.puertos.salida.ExamenRepositorio;
import co.edu.poli.Examen2.puertos.salida.ExamenRespaldoPuerto;
import co.edu.poli.Examen2.dominio.tarifa.*;
import co.edu.poli.Examen2.adaptadores.salida.fabrica.*;
import java.io.IOException;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.math.BigDecimal;

/**
 * Punto de entrada de la aplicación JavaFX y <b>raíz de composición</b>: es el único
 * lugar que conoce las clases concretas. Crea los adaptadores de salida, los inyecta
 * en los casos de uso y estos, a su vez, en el adaptador de entrada (el controlador).
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class App extends Application {

    /** Nombre del archivo de respaldo, relativo al directorio de ejecución. */
    private static final String ARCHIVO_RESPALDO = "examenes.ser";

    /**
     * Ensambla las dependencias y muestra la ventana principal.
     *
     * @param stage escenario principal
     * @throws IOException si no puede cargarse el archivo FXML
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Adaptadores de salida (driven)
        ExamenRepositorio repositorio = new ExamenRepositorioMemoria();
        //ExamenRespaldoPuerto respaldo = new ExamenRespaldoArchivoAdapter(Paths.get(ARCHIVO_RESPALDO));

        
        RespaldoFactory fabrica = RespaldoFactories.desdeConfiguracion();           // Factory Method
        ExamenRespaldoPuerto respaldo = fabrica.crear(Paths.get(""));                // devuelve el Adapter elegido

        PoliticaTarifa tarifa = new TarifaRecargoRhNegativo(new BigDecimal("15"));  // Strategy

        
        // Casos de uso (aplicación) con sus puertos de salida inyectados
        PrimaryController controlador = new PrimaryController(
            new RegistrarExamenService(repositorio, tarifa),
            new ListarExamenesService(repositorio),
            new SerializarExamenesService(repositorio, respaldo),
            new DeserializarExamenesService(repositorio, respaldo));

        // Adaptador de entrada (driving)
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/poli/Examen2/primary.fxml"));
        loader.setController(controlador);
        Parent raiz = loader.load();

        stage.setTitle("Exámenes de Sangre");
        stage.setScene(new Scene(raiz, 640, 580));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Lanza la aplicación.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch();
    }
}
