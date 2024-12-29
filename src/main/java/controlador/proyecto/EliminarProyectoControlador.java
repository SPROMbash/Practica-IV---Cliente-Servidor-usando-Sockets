package controlador.proyecto;

import com.example.pr4sockets.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Proyecto;
import modelo.Usuario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class EliminarProyectoControlador {
    @FXML
    private TableView<Proyecto> tablaProyecto;
    @FXML
    private TableColumn<Usuario, String> tcIdProyecto;
    @FXML
    private TableColumn<Usuario, String> tcNombreProyecto;
    @FXML
    private TableColumn<Usuario, String> tcDescripcionProyecto;
    @FXML
    private TableColumn<Usuario, String> tcTipoProyecto;
    @FXML
    private ComboBox<Long> cbProyectos;

    @FXML
    public void initialize() {
        tcIdProyecto.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombreProyecto.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tcDescripcionProyecto.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
        tcTipoProyecto.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
        cargarProyectos();
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

    public void crearProyecto(ActionEvent actionEvent) {
        try {
            App.setRoot("crearProyecto", "Proyectos - Crear proyecto");
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

    public void abrirUsuarioProyectos(ActionEvent actionEvent) {
        try {
            App.setRoot("usuarioProyectos", "Usuarios - Proyectos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void volver(ActionEvent actionEvent) {
        try {
            App.setRoot("principal", "Gestión de usuarios y proyectos - Principal");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(ActionEvent actionEvent) {
        if (cbProyectos.getValue() == null) {
            return;
        }
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("proyecto:eliminar");
            salida.writeLong(cbProyectos.getValue());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText("Proyecto eliminado");
            alert.setContentText(entrada.readUTF());
            alert.showAndWait();
            volver(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarProyectos() {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("proyecto:listar");
            int tamanio = entrada.readInt();
            if (tamanio > 0) {
                List<Proyecto> proyectos = new ArrayList<>();
                List<Long> idProyectos = new ArrayList<>();
                for (int i = 0; i < tamanio; i++) {
                    proyectos.add(Proyecto.deserializar(entrada.readUTF()));
                    idProyectos.add(proyectos.get(i).getId());
                }
                tablaProyecto.getItems().setAll(proyectos);
                cbProyectos.getItems().setAll(idProyectos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
