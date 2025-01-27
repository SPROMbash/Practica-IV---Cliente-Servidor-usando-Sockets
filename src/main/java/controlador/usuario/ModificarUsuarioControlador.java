package controlador.usuario;

import com.example.pr4sockets.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Proyecto;
import modelo.Usuario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ModificarUsuarioControlador {
    // Tabla de usuarios
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

    // Formulario de modificación
    @FXML
    private ComboBox<Long> cbUsuarios;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtEmail;

    @FXML
    public void initialize() {
        tcIdUsuario.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tcApellidosUsuario.setCellValueFactory(new PropertyValueFactory<>("Apellidos"));
        tcEmailUsuario.setCellValueFactory(new PropertyValueFactory<>("Email"));
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

    public void eliminarProyecto(ActionEvent actionEvent) {
        try {
            App.setRoot("eliminarProyecto", "Proyectos - Eliminar proyecto.");
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

    public void modificar(ActionEvent actionEvent) {
        if (comprobarId()) {
            try (Socket socket = new Socket("localhost", 12345);
                 DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
                 DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
                if (comprobarCampos() && comprobarEmail()) {
                    Long idUsuario = cbUsuarios.getValue();
                    String nombre = txtNombre.getText();
                    String apellidos = txtApellidos.getText();
                    String email = txtEmail.getText();

                    salida.writeUTF("usuario:actualizar");
                    salida.writeLong(idUsuario);

                    if (nombre != null && !nombre.isEmpty()) {
                        salida.writeUTF(nombre);
                    } else {
                        salida.writeUTF("");
                    }
                    if (apellidos != null && !apellidos.isEmpty()) {
                        salida.writeUTF(apellidos);
                    } else {
                        salida.writeUTF("");
                    }
                    if (email != null && !email.isEmpty()) {
                        salida.writeUTF(email);
                    } else {
                        salida.writeUTF("");
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setHeaderText("Usuario actualizado");
                    alert.setContentText(entrada.readUTF());
                    alert.showAndWait();
                }
                volver(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private boolean comprobarId() {
        if (cbUsuarios.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Usuario no seleccionado");
            alert.setContentText("Por favor, selecciona un usuario.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean comprobarCampos() {
        if (txtNombre.getText().isEmpty() && txtApellidos.getText().isEmpty() && txtEmail.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Por favor, al menos, un campo que quieras modificar.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean comprobarEmail() {
        if (!txtEmail.getText().isEmpty() && !txtEmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Email incorrecto");
            alert.setContentText("Por favor, introduce un email válido.");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
