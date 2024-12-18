package servidor;

import dao.GenericDAO;
import modelo.Proyecto;
import modelo.Usuario;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class ManejoCliente implements Runnable {
    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private GenericDAO<Usuario> usuarioDAO;
    private GenericDAO<Proyecto> proyectoDAO;
    private ReentrantLock monitor = new ReentrantLock();


    public ManejoCliente(Socket socket) {
        this.socket = socket;
        try (DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            this.entrada = entrada;
            this.salida = salida;
            this.usuarioDAO = new GenericDAO<>(Usuario.class);
            this.proyectoDAO = new GenericDAO<>(Proyecto.class);
        } catch (Exception e) {
            System.err.println("Error al crear los flujos de entrada y salida: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String peticion = entrada.readUTF();
            System.out.println("Petición recibida: " + peticion);
            if (peticion.startsWith("usuario")) {
                procesarPeticionUsuario(peticion.split(":")[1]);
            } else if (peticion.startsWith("proyecto")) {
                procesarPeticionProyecto(peticion.split(":")[1]);
            }
        } catch (Exception e) {
            System.err.println("Error al procesar la petición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void desconectar() {
        try {
            socket.close();
        } catch (Exception e) {
            System.err.println("Error al desconectar el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void procesarPeticionProyecto(String s) {
    }

    private void procesarPeticionUsuario(String peticion) {
        if (peticion.equalsIgnoreCase("crear")) {
            crearUsuario();
        } else if (peticion.equalsIgnoreCase("listar")) {
            listarUsuarios();
        } else if (peticion.equalsIgnoreCase("actualizar")) {
            actualizarUsuario();
        } else if (peticion.equalsIgnoreCase("eliminar")) {
            eliminarUsuario();
        }
    }

    private void crearUsuario() {
        monitor.lock();
        try (ObjectInputStream entradaObjeto = new ObjectInputStream(entrada);) {
            Usuario usuario = (Usuario) entradaObjeto.readObject();
            System.out.println("Usuario recibido: " + usuario);
            usuarioDAO.crear(usuario);
            salida.writeUTF("Usuario creado correctamente");

        } catch (Exception e) {
            System.err.println("Error al crear el usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    private void listarUsuarios() {
        monitor.lock();
        try (ObjectOutputStream salidaObjeto = new ObjectOutputStream(salida);) {
            salidaObjeto.writeObject(usuarioDAO.getTodos());
        } catch (Exception e) {
            System.err.println("Error al listar los usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    private void actualizarUsuario() {
        monitor.lock();
        try (ObjectInputStream entradaObjeto = new ObjectInputStream(entrada);) {
            Usuario usuario = (Usuario) entradaObjeto.readObject();
            System.out.println("Usuario recibido: " + usuario);
            usuarioDAO.actualizar(usuario);
            salida.writeUTF("Usuario actualizado correctamente");
        } catch (Exception e) {
            System.err.println("Error al actualizar el usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    private void eliminarUsuario() {
        monitor.lock();
        try (ObjectInputStream entradaObjeto = new ObjectInputStream(entrada);) {
            Usuario usuario = (Usuario) entradaObjeto.readObject();
            System.out.println("Usuario recibido: " + usuario);
            usuarioDAO.eliminar(usuario);
            salida.writeUTF("Usuario eliminado correctamente");
        } catch (Exception e) {
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }
}
