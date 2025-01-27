package controlador.usuario;

import com.example.pr4sockets.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Usuario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class EliminarUsuarioControlador {
    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, String> tcIdUsuario;
    @FXML
    private TableColumn<Usuario, String> tcNombreUsuario;
    @FXML
    private TableColumn<Usuario, String> tcApellidosUsuario;
    @FXML
    private TableColumn<Usuario, String> tcEmailUsuario;
    @FXML
    private ComboBox<Long> cbUsuarios;

    @FXML
    public void initialize() {
        tcIdUsuario.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcApellidosUsuario.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        tcEmailUsuario.setCellValueFactory(new PropertyValueFactory<>("email"));
        cargarUsuarios();
    }
    public void crearUsuario(ActionEvent actionEvent) {
        try {
            App.setRoot("crearUsuario", "Usuarios - Crear usuario.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarUsuario(ActionEvent actionEvent) {
        try {
            App.setRoot("modificarUsuario", "Usuarios - Modificar usuario.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarUsuario(ActionEvent actionEvent) {
        try {
            App.setRoot("eliminarUsuario", "Usuarios - Eliminar usuario.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearProyecto(ActionEvent actionEvent) {
        try {
            App.setRoot("crearProyecto", "Proyectos - Crear proyecto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarProyecto(ActionEvent actionEvent) {
        try {
            App.setRoot("modificarProyecto", "Proyectos - Modificar proyecto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirUsuarioProyectos(ActionEvent actionEvent) {
        try {
            App.setRoot("usuarioProyectos", "Usuarios - Proyectos.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void volver(ActionEvent actionEvent) {
        try {
            App.setRoot("principal", "Gestión de usuarios y proyectos - Principal.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(ActionEvent actionEvent) {
        if (cbUsuarios.getValue() == null) {
            return;
        }
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("usuario:eliminar");
            salida.writeLong(cbUsuarios.getValue());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText("Usuario eliminado");
            alert.setContentText(entrada.readUTF());
            alert.showAndWait();
            volver(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarUsuarios() {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("usuario:listar");
            int tamanio = entrada.readInt();
            List<Usuario> usuarios = new ArrayList<>();
            List<Long> idUsuarios = new ArrayList<>();
            if (tamanio > 0) {
                for (int i = 0; i < tamanio; i++) {
                    usuarios.add(Usuario.deserializar(entrada.readUTF()));
                    idUsuarios.add(usuarios.get(i).getId());
                }
                tablaUsuarios.getItems().setAll(usuarios);
                cbUsuarios.getItems().setAll(idUsuarios);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
