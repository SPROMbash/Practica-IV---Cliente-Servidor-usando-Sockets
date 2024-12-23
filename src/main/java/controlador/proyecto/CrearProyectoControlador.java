package controlador.proyecto;

import com.example.pr4sockets.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class CrearProyectoControlador {
    @FXML
    private TextField txtNombre;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private ComboBox cbTipo;

    @FXML
    public void initialize() {
        cbTipo.getItems().addAll("Desarrollo", "Investigación", "Formación", "Mejora", "Empresarial");
    }

    public void crear(ActionEvent actionEvent) {
        try (Socket socket = new Socket("localhost", 12345);
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
             DataInputStream entrada = new DataInputStream(socket.getInputStream());) {
            if (comprobarCampos()) {
                salida.writeUTF("proyecto:crear");
                salida.writeUTF(txtNombre.getText());
                salida.writeUTF(txtDescripcion.getText());
                salida.writeUTF(cbTipo.getValue().toString());
                String respuesta = entrada.readUTF();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText("Usuario creado");
                alert.setContentText(respuesta);
                alert.showAndWait();
            }
            volver(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarProyecto(ActionEvent actionEvent) {
        try {
            App.setRoot("modificarProyecto", "Proyectos - Modificar proyecto");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarProyecto(ActionEvent actionEvent) {
        try {
            App.setRoot("eliminarProyecto", "Proyectos - Eliminar proyecto");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void volver(ActionEvent actionEvent) {
        try {
            App.setRoot("principal", "Principal");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearUsuario(ActionEvent actionEvent) {
        try {
            App.setRoot("crearUsuario", "Usuarios - Crear usuario");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarUsuario(ActionEvent actionEvent) {
        try {
            App.setRoot("modificarUsuario", "Usuarios - Modificar usuario");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarUsuario(ActionEvent actionEvent) {
        try {
            App.setRoot("eliminarUsuario", "Usuarios - Eliminar usuario");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean comprobarCampos() {
        return !txtNombre.getText().isEmpty() && !txtDescripcion.getText().isEmpty() && cbTipo.getValue() != null;
    }
}
