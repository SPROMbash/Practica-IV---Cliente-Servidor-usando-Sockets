package controlador;

import com.example.pr4sockets.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Proyecto;
import modelo.Usuario;
import modelo.UsuarioProyecto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PrincipalControlador {
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<String> cbTipo;

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
    private TableView<UsuarioProyecto> tablaUsuarioProyecto;
    @FXML
    private TableColumn<UsuarioProyecto, String> tcIdUsuarioProyecto;
    @FXML
    private TableColumn<UsuarioProyecto, String> tcIdUsuarioUsuarioProyecto;
    @FXML
    private TableColumn<UsuarioProyecto, String> tcIdProyectoUsuarioProyecto;

    @FXML
    public void initialize() {
        tcIdUsuario.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tcApellidosUsuario.setCellValueFactory(new PropertyValueFactory<>("Apellidos"));
        tcEmailUsuario.setCellValueFactory(new PropertyValueFactory<>("Email"));
        listarUsuarios();

        tcIdProyecto.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombreProyecto.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tcDescripcionProyecto.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
        tcTipoProyecto.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
        listarProyectos();

        tcIdUsuarioProyecto.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcIdUsuarioUsuarioProyecto.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        tcIdProyectoUsuarioProyecto.setCellValueFactory(new PropertyValueFactory<>("proyecto"));
        listarUsuarioProyecto();

        cbTipo.getItems().addAll("Desarrollo", "Investigación", "Formación", "Mejora", "Empresarial");

        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> filtrarNombre());
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> filtrarEmail());
        cbTipo.valueProperty().addListener((observable, oldValue, newValue) -> filtrarTipo());
    }

    public void crearUsuario(ActionEvent actionEvent) {
        try {
            App.setRoot("crearUsuario", "Usuarios - Crear usuario");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarUsuarios() {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("usuario:listar");
            int tamanio = entrada.readInt();
            List<Usuario> usuarios = new ArrayList<>();
            if (tamanio != 0) {
                for (int i = 0; i < tamanio; i++) {
                    usuarios.add(Usuario.deserializar(entrada.readUTF()));
                }
                tablaUsuarios.getItems().setAll(usuarios);
            }
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

    public void listarProyectos() {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("proyecto:listar");
            int tamanio = entrada.readInt();
            List<Proyecto> proyectos = new ArrayList<>();

            for (int i = 0; i < tamanio; i++) {
                proyectos.add(Proyecto.deserializar(entrada.readUTF()));
            }
            tablaProyecto.getItems().setAll(proyectos);
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

    public void salir(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void filtrarNombre() {
        String nombre = txtNombre.getText();
        if (!nombre.isEmpty()) {
            try (Socket socket = new Socket("localhost", 12345);
                 DataInputStream entrada = new DataInputStream(socket.getInputStream());
                 DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
                salida.writeUTF("usuario:nombre");
                salida.writeUTF(nombre);
                int tamanio = entrada.readInt();
                if (tamanio != 0) {
                    List<Usuario> usuarios = new ArrayList<>();

                    for (int i = 0; i < tamanio; i++) {
                        usuarios.add(Usuario.deserializar(entrada.readUTF()));
                    }
                    tablaUsuarios.getItems().setAll(usuarios);
                } else {
                    tablaUsuarios.getItems().clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listarUsuarios();
        }
    }

    public void filtrarEmail() {
        String email = txtEmail.getText();
        if (!email.isEmpty()) {
            try (Socket socket = new Socket("localhost", 12345);
                 DataInputStream entrada = new DataInputStream(socket.getInputStream());
                 DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
                salida.writeUTF("usuario:email");
                salida.writeUTF(email);
                int tamanio = entrada.readInt();
                if (tamanio != 0) {
                    List<Usuario> usuarios = new ArrayList<>();

                    for (int i = 0; i < tamanio; i++) {
                        usuarios.add(Usuario.deserializar(entrada.readUTF()));
                    }
                    tablaUsuarios.getItems().setAll(usuarios);
                } else {
                    tablaUsuarios.getItems().clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listarUsuarios();
        }
    }

    public void filtrarTipo() {
        String tipo = cbTipo.getValue();
        if ((tipo != null && !tipo.isEmpty())) {
            try (Socket socket = new Socket("localhost", 12345);
                 DataInputStream entrada = new DataInputStream(socket.getInputStream());
                 DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
                salida.writeUTF("proyecto:tipo");
                salida.writeUTF(tipo);
                int tamanio = entrada.readInt();
                if (tamanio != 0) {
                    List<Proyecto> proyectos = new ArrayList<>();

                    for (int i = 0; i < tamanio; i++) {
                        proyectos.add(Proyecto.deserializar(entrada.readUTF()));
                    }
                    tablaProyecto.getItems().setAll(proyectos);
                } else {
                    tablaProyecto.getItems().clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listarProyectos();
        }
    }

    public void listarUsuarioProyecto() {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("up:listar");
            int tamanio = entrada.readInt();
            if (tamanio > 0) { // Solo procesar si hay datos
                List<UsuarioProyecto> usuarioProyecto = new ArrayList<>();
                for (int i = 0; i < tamanio; i++) {
                    usuarioProyecto.add(UsuarioProyecto.deserializar(entrada.readUTF())); // Leer cada elemento
                }
                tablaUsuarioProyecto.getItems().setAll(usuarioProyecto); // Mostrar en la tabla
            } else {
                tablaUsuarioProyecto.getItems().clear(); // No hay datos, limpiar tabla
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limpiarFiltroProyecto(ActionEvent actionEvent) {
        cbTipo.getSelectionModel().clearSelection();
        listarProyectos();
    }

    public void limpiarFiltroUsuario(ActionEvent actionEvent) {
        txtNombre.clear();
        txtEmail.clear();
    }

    public void abrirUsuarioProyectos(ActionEvent actionEvent) {
        try {
            App.setRoot("usuarioProyectos", "Gestión de la relación de un usuario con sus proyectos.");
        } catch (Exception e) {
            e.printStackTrace();
            }
    }

}