package controlador;

import com.example.pr4sockets.App;
import javafx.event.ActionEvent;
import modelo.Usuario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class UsuariosControlador {

    public void crearUsuario(ActionEvent actionEvent) {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF("usuario:crear");
            String respuesta = entrada.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void actualizarUsuario(ActionEvent actionEvent) {
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             ObjectOutputStream objetoSalida = new ObjectOutputStream(socket.getOutputStream())) {
            objetoSalida.writeUTF("usuario:actualizar");
            objetoSalida.writeObject(new Usuario());
            String respuesta = entrada.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
