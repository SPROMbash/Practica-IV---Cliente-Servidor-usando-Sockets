package controlador;

import com.example.pr4sockets.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Usuario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PrincipalControlador {
    private static int contadorUsuarios = 0;
    private static int contadorProyectos = 0;
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
    public void initialize() {
        tcIdUsuario.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tcApellidosUsuario.setCellValueFactory(new PropertyValueFactory<>("Apellidos"));
        tcEmailUsuario.setCellValueFactory(new PropertyValueFactory<>("Email"));
        listarUsuarios();
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

            for (int i = 0; i < tamanio; i++) {
                usuarios.add(deserializarUsuario(entrada.readUTF()));
            }
            tablaUsuarios.getItems().addAll(usuarios);
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

    public void salir(ActionEvent actionEvent) {
        System.exit(0);
    }

    private Usuario deserializarUsuario(String s) {
        String[] campos = s.split(",");
        return new Usuario(Long.parseLong(campos[0]), campos[1], campos[3], campos[2]);
    }
}