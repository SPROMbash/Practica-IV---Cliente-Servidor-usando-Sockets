package controlador.proyecto;

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

public class ModificarProyectoControlador {
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
    private ComboBox cbIdsProyecto;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private ComboBox cbTipo;

    @FXML
    public void initialize() {
        tcIdProyecto.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombreProyecto.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tcDescripcionProyecto.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
        tcTipoProyecto.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
        int tamanio = listarProyectos();
        for (int i = 1; i <= tamanio; i++) {
            cbIdsProyecto.getItems().add(i);
        }
        cbTipo.getItems().addAll("Desarrollo", "Investigación", "Formación", "Mejora", "Empresarial");
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

    public void crearProyecto(ActionEvent actionEvent) {
        try {
            App.setRoot("crearProyecto", "Proyectos - Crear proyecto");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificar(ActionEvent actionEvent) {
        try (Socket socket = new Socket("localhost", 12345);
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
             DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
            if (comprobarCampos()) {
                String idProyecto = cbIdsProyecto.getValue().toString();
                String nombre = txtNombre.getText();
                String descripcion = txtDescripcion.getText();
                String tipo = cbTipo.getValue() != null ? cbTipo.getValue().toString() : null;

                salida.writeUTF("proyecto:actualizar");
                salida.writeUTF(idProyecto);

                if (nombre != null && !nombre.isEmpty()) {
                    salida.writeUTF(nombre);
                } else {
                    salida.writeUTF("");
                }
                if (descripcion != null && !descripcion.isEmpty()) {
                    salida.writeUTF(descripcion);
                } else {
                    salida.writeUTF("");
                }
                if (tipo != null && !tipo.isEmpty()) {
                    salida.writeUTF(tipo);
                } else {
                    salida.writeUTF("");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText("Proyecto actualizado");
                alert.setContentText("Proyecto actualizado correctamente.");
                alert.showAndWait();
            }
            volver(null);
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

    public int listarProyectos() {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("proyecto:listar");
            int tamanio = entrada.readInt();
            List<Proyecto> proyectos = new ArrayList<>();

            for (int i = 0; i < tamanio; i++) {
                proyectos.add(deserializarProyecto(entrada.readUTF()));
            }
            tablaProyecto.getItems().addAll(proyectos);
            return tamanio;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Proyecto deserializarProyecto(String s) {
        String[] campos = s.split(",");
        return new Proyecto(Long.parseLong(campos[0]), campos[1], campos[2], campos[3]);
    }

    private boolean comprobarCampos() {
        if (txtNombre.getText().isEmpty() && txtDescripcion.getText().isEmpty() && cbTipo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Por favor, al menos, un campo que quieras modificar.");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
