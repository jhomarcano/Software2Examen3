/**
 * Módulo de la aplicación de exámenes de sangre, organizado en arquitectura hexagonal.
 *
 * @author Jhomar Alejandra Cano Ramirez
 */
module co.edu.poli.Examen2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.poli.Examen2.adaptadores.entrada.javafx to javafx.fxml;

    exports co.edu.poli.Examen2.adaptadores.entrada.javafx;
}
