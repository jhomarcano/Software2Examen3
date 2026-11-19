package co.edu.poli.Examen2.adaptadores.entrada.javafx;

import co.edu.poli.Examen2.dominio.excepcion.ReglaNegocioException;
import co.edu.poli.Examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.Examen2.dominio.modelo.FactorRH;
import co.edu.poli.Examen2.dominio.modelo.TipoSangre;
import co.edu.poli.Examen2.puertos.entrada.DeserializarExamenesUseCase;
import co.edu.poli.Examen2.puertos.entrada.ListarExamenesUseCase;
import co.edu.poli.Examen2.puertos.entrada.RegistrarExamenComando;
import co.edu.poli.Examen2.puertos.entrada.RegistrarExamenUseCase;
import co.edu.poli.Examen2.puertos.entrada.SerializarExamenesUseCase;
import java.math.BigDecimal;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Adaptador de entrada JavaFX. Solo traduce la interacción de la interfaz a
 * llamadas a los puertos de entrada y presenta el resultado. No contiene reglas
 * de negocio ni acceso a archivos.
 *
 * @author Jhomar Alejandra Cano Ramirez
 * @version 1.0
 */
public class PrimaryController {

    private final RegistrarExamenUseCase registrar;
    private final ListarExamenesUseCase listar;
    private final SerializarExamenesUseCase serializar;
    private final DeserializarExamenesUseCase deserializar;

    @FXML private TextField txtCodigoExamen;
    @FXML private TextField txtNombrePaciente;
    @FXML private TextField txtCosto;
    @FXML private ComboBox<String> cmbTipoSangre;
    @FXML private RadioButton rbPositivo;
    @FXML private TextArea txtAreaMensajes;

    /**
     * Recibe los puertos de entrada por constructor (inyectados por {@link App}).
     *
     * @param registrar    caso de uso de registro
     * @param listar       caso de uso de listado
     * @param serializar   caso de uso de respaldo
     * @param deserializar caso de uso de restauración
     */
    public PrimaryController(RegistrarExamenUseCase registrar, ListarExamenesUseCase listar,
                             SerializarExamenesUseCase serializar,
                             DeserializarExamenesUseCase deserializar) {
        this.registrar = registrar;
        this.listar = listar;
        this.serializar = serializar;
        this.deserializar = deserializar;
    }

    /**
     * Maneja el clic en «Guardar»: arma el comando, invoca el caso de uso y muestra
     * el resultado. Si el registro es exitoso, limpia el formulario.
     */
    @FXML
    private void handleGuardar() {
        try {
            BigDecimal costo = leerCosto();
            TipoSangre tipo = cmbTipoSangre.getValue() == null
                ? null : TipoSangre.valueOf(cmbTipoSangre.getValue());
            FactorRH factor = rbPositivo.isSelected() ? FactorRH.POSITIVO : FactorRH.NEGATIVO;

            ExamenSangre examen = registrar.registrar(new RegistrarExamenComando(
                txtCodigoExamen.getText(), txtNombrePaciente.getText(), costo, tipo, factor));
            mostrar("✔ Registrado: " + examen);
            limpiarFormulario();
        } catch (NumberFormatException e) {
            mostrar("✘ El costo debe ser un número válido (ej. 150.00).");
        } catch (ReglaNegocioException | NullPointerException e) {
            mostrar("✘ " + e.getMessage());
        }
    }

    /** Maneja el clic en «Listar»: muestra todos los exámenes registrados. */
    @FXML
    private void handleListar() {
        List<ExamenSangre> examenes = listar.listar();
        if (examenes.isEmpty()) {
            mostrar("No hay exámenes registrados.");
            return;
        }
        mostrar("— Exámenes registrados (" + examenes.size() + ") —");
        examenes.forEach(e -> mostrar(e.toString()));
    }

    /** Maneja el clic en «Serializar»: respalda los exámenes en el almacenamiento. */
    @FXML
    private void handleSerializar() {
        try {
            int cantidad = serializar.serializar();
            mostrar("📦 Respaldo guardado: " + cantidad + " examen(es).");
        } catch (IllegalStateException e) {
            mostrar("✘ " + e.getMessage());
        }
    }

    /** Maneja el clic en «Deserializar»: restaura los exámenes desde el respaldo. */
    @FXML
    private void handleDeserializar() {
        try {
            int cantidad = deserializar.deserializar();
            mostrar("📂 Respaldo restaurado: " + cantidad + " examen(es).");
        } catch (IllegalStateException e) {
            mostrar("✘ " + e.getMessage());
        }
    }

    /**
     * Convierte el texto del campo costo a número.
     *
     * @return el costo ingresado
     * @throws NumberFormatException si el texto no es numérico
     */
    private BigDecimal leerCosto() {
        return new BigDecimal(txtCosto.getText().trim().replace(',', '.'));
    }

    /** Deja los campos del formulario en su estado inicial. */
    private void limpiarFormulario() {
        txtCodigoExamen.clear();
        txtNombrePaciente.clear();
        txtCosto.clear();
        cmbTipoSangre.setValue(null);
        rbPositivo.setSelected(true);
    }

    /**
     * Agrega una línea al área de mensajes.
     *
     * @param mensaje texto a mostrar
     */
    private void mostrar(String mensaje) {
        txtAreaMensajes.appendText(mensaje + System.lineSeparator());
    }
}
