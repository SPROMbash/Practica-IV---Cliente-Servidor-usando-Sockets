package controlador.relaciones;

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

public class UsuarioProyectosControlador {
    // Combo Boxes
    @FXML
    private ComboBox<Long> cbUsuarios;
    @FXML
    private ComboBox<Long> cbProyectosAnadir;
    @FXML
    private ComboBox<Long> cbProyectosEliminar;

    // Botones
    @FXML
    private Button buttonAnadir;
    @FXML
    private Button buttonEliminar;
    @FXML
    private Button buttonLimpiar;

    // Radio Buttons
    @FXML
    private RadioButton rbAsignados;
    @FXML
    private RadioButton rbNoAsignados;

    // Tabla de proyectos
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
    public void initialize() {
        cargarUsuarios();
        tcIdProyecto.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombreProyecto.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tcDescripcionProyecto.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
        tcTipoProyecto.setCellValueFactory(new PropertyValueFactory<>("Tipo"));

        cbUsuarios.setOnAction(event -> {
            listarProyectos();
            cargarProyectosAnadir();
            cargarProyectosEliminar();
        });
    }

    public void listarProyectos() {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            if (rbAsignados.isSelected()) {
                salida.writeUTF("up:proyectos");
            } else {
                salida.writeUTF("up:!proyectos");
            }
            Long id = 1L;
            if (cbUsuarios.getValue() != null) {
                id = cbUsuarios.getValue();
            }
            salida.writeLong(id);
            int tamanio = entrada.readInt();
            if (tamanio > 0) {
                List<Proyecto> proyectos = new ArrayList<>();

                for (int i = 0; i < tamanio; i++) {
                    proyectos.add(Proyecto.deserializar(entrada.readUTF()));
                }
                tablaProyecto.getItems().setAll(proyectos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearUsuario(ActionEvent actionEvent) {
        try {
            App.setRoot("crearUsuario", "Usuarios - Crear Usuario");
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

    public void eliminarProyecto(ActionEvent actionEvent) {
        try {
            App.setRoot("eliminarProyecto", "Proyectos - Eliminar proyecto");
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

    public void anadirProyecto(ActionEvent actionEvent) {
    }

    public void limpiar(ActionEvent actionEvent) {
        cbUsuarios.setValue(null);
        cbProyectosAnadir.getItems().clear();
        cbProyectosEliminar.getItems().clear();
        tablaProyecto.getItems().clear();
    }

    public void cargarUsuarios() {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("usuario:listar");
            int tamanio = entrada.readInt();
            List<Usuario> usuarios = new ArrayList<>();
            List<Long> idUsuarios = new ArrayList<>();
            if (tamanio != 0) {
                for (int i = 0; i < tamanio; i++) {
                    usuarios.add(Usuario.deserializar(entrada.readUTF()));
                    idUsuarios.add(usuarios.get(i).getId());
                }
                cbUsuarios.getItems().setAll(idUsuarios);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarProyectosAnadir() {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("up:!proyectos");
            Long id = 1L;
            if (cbUsuarios.getValue() != null) {
                id = cbUsuarios.getValue();
            }
            salida.writeLong(id);
            int tamanio = entrada.readInt();
            if (tamanio > 0) {
                List<Proyecto> proyectos = new ArrayList<>();
                List<Long> idProyectos = new ArrayList<>();

                for (int i = 0; i < tamanio; i++) {
                    proyectos.add(Proyecto.deserializar(entrada.readUTF()));
                    idProyectos.add(proyectos.get(i).getId());
                }
                cbProyectosAnadir.getItems().setAll(idProyectos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarProyectosEliminar() {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("up:proyectos");
            Long id = 1L;
            if (cbUsuarios.getValue() != null) {
                id = cbUsuarios.getValue();
            }
            salida.writeLong(id);
            int tamanio = entrada.readInt();
            if (tamanio > 0) {
                List<Proyecto> proyectos = new ArrayList<>();
                List<Long> idProyectos = new ArrayList<>();

                for (int i = 0; i < tamanio; i++) {
                    proyectos.add(Proyecto.deserializar(entrada.readUTF()));
                    idProyectos.add(proyectos.get(i).getId());
                }
                cbProyectosEliminar.getItems().setAll(idProyectos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
