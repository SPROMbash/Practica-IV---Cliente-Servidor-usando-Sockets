package controlador;

import com.example.pr4sockets.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PrincipalControlador {

    public void abrirUsuario(ActionEvent actionEvent) {
        try {
            App.setRoot("usuarios", "Gestión de Usuarios");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirProyectos(ActionEvent actionEvent) {
        try {
            App.setRoot("proyectos", "Gestión de Proyectos");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void salir(ActionEvent actionEvent) {
        System.exit(0);
    }
}