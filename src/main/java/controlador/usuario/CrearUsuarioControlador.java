package controlador.usuario;

import com.example.pr4sockets.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class CrearUsuarioControlador {
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtEmail;

    public void crear(ActionEvent actionEvent) {
        try (Socket socket = new Socket("localhost", 12345);
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
             DataInputStream entrada = new DataInputStream(socket.getInputStream());) {
            if (comprobarEmail() && comprobarCampos()) {
                salida.writeUTF("usuario:crear");
                salida.writeUTF(txtNombre.getText());
                salida.writeUTF(txtApellido.getText());
                salida.writeUTF(txtEmail.getText());
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

    public void volver(ActionEvent actionEvent) {
        try {
            App.setRoot("principal", "Principal");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean comprobarCampos() {
        if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Debes rellenar todos los campos");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    private boolean comprobarEmail() {
        if (txtEmail.getText().matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.([a-zA-Z]{2,4})+$")) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Email incorrecto");
            alert.setContentText("El email introducido no es correcto");
            alert.showAndWait();
            return false;
        }
    }
}
