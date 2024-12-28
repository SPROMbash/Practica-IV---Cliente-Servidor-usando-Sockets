package servidor;

import dao.ProyectoDAO;
import dao.UsuarioDAO;
import modelo.Proyecto;
import modelo.Usuario;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ManejoCliente implements Runnable {
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private UsuarioDAO usuarioDAO;
    private ProyectoDAO proyectoDAO;
    private ReentrantLock monitor = new ReentrantLock();


    public ManejoCliente(Socket socket) {
        this.socket = socket;
        try  {
            this.salida = new DataOutputStream(socket.getOutputStream());
            this.entrada = new DataInputStream(socket.getInputStream());
            this.usuarioDAO = new UsuarioDAO();
            this.proyectoDAO = new ProyectoDAO();
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
        } finally {
            desconectar();
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

    private void procesarPeticionProyecto(String peticion) {
        switch (peticion.toLowerCase()) {
            case "crear":
                crearProyecto();
                break;
            case "listar":
                listarProyectos();
                break;
            case "actualizar":
                actualizarProyecto();
                break;
            case "eliminar":
                eliminarProyecto();
                break;
            case "tipo":
                buscarPorTipo();
                break;
            case "consultaTipo":
                consultaTipo();
                break;
        }
    }

    private void procesarPeticionUsuario(String peticion) {
        switch (peticion.toLowerCase()) {
            case "crear":
                crearUsuario();
                break;
            case "listar":
                listarUsuarios();
                break;
            case "actualizar":
                actualizarUsuario();
                break;
            case "eliminar":
                eliminarUsuario();
                break;
            case "nombre":
                buscarPorNombre();
                break;
            case "email":
                buscarPorEmail();
                break;
        }
    }

    private void crearUsuario() {
        monitor.lock();
        try {
            String nombre = entrada.readUTF();
            String apellidos = entrada.readUTF();
            String email = entrada.readUTF();
            Usuario usuario = new Usuario(nombre, apellidos, email);
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
        try {
            ArrayList<Usuario> usuarios = (ArrayList<Usuario>) usuarioDAO.getTodos();
            if (usuarios != null) {
                salida.writeInt(usuarios.size());
                for (Usuario usuario : usuarios) {
                    salida.writeUTF(usuario.serializar());
                }
            } else {
                salida.writeInt(0);
            }
            salida.writeUTF("Usuarios listados correctamente");
        } catch (Exception e) {
            System.err.println("Error al listar los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void actualizarUsuario() {
        monitor.lock();
        try  {
            String nombre = entrada.readUTF();
            String apellidos = entrada.readUTF();
            String email = entrada.readUTF();
            Usuario usuario = new Usuario(nombre, apellidos, email);
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
        try {
            String nombre = entrada.readUTF();
            String apellidos = entrada.readUTF();
            String email = entrada.readUTF();
            Usuario usuario = new Usuario(nombre, apellidos, email);
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

    private void buscarPorNombre() {
        try {
            String nombre = entrada.readUTF();
            ArrayList<Usuario> usuarios = (ArrayList<Usuario>) usuarioDAO.obtenerPorNombre(nombre);
            if (usuarios != null) {
                salida.writeInt(usuarios.size());
                for (Usuario usuario : usuarios) {
                    salida.writeUTF(usuario.serializar());
                }
            } else {
                salida.writeInt(0);
            }
        } catch (Exception e) {
            System.err.println("Error al listar los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void buscarPorEmail() {
        try {
            String nombre = entrada.readUTF();
            ArrayList<Usuario> usuarios = (ArrayList<Usuario>) usuarioDAO.obtenerPorNombre(nombre);
            if (usuarios != null) {
                salida.writeInt(usuarios.size());
                for (Usuario usuario : usuarios) {
                    salida.writeUTF(usuario.serializar());
                }
            } else {
                salida.writeInt(0);
            }
            salida.writeUTF("Usuarios listados correctamente");
        } catch (Exception e) {
            System.err.println("Error al listar los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearProyecto() {
        monitor.lock();
        try {
            String nombre = entrada.readUTF();
            String descripcion = entrada.readUTF();
            String tipo = entrada.readUTF();
            Proyecto proyecto = new Proyecto(nombre, descripcion, tipo);
            System.out.println("Proyecto recibido: " + proyecto);
            proyectoDAO.crear(proyecto);
            salida.writeUTF("Proyecto creado correctamente");
        } catch (Exception e) {
            System.err.println("Error al crear el proyecto: " + e.getMessage());
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    private void listarProyectos() {
        monitor.lock();
        try {
            ArrayList<Proyecto> proyectos = (ArrayList<Proyecto>) proyectoDAO.getTodos();
            salida.writeInt(proyectos.size());
            for (Proyecto proyecto : proyectos) {
                salida.writeUTF(proyecto.serializar());
            }
            salida.writeUTF("Usuarios listados correctamente");
        } catch (Exception e) {
            System.err.println("Error al listar los usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    private void actualizarProyecto() {
        monitor.lock();
        try {
            String idProyecto = entrada.readUTF();
            String nombre = entrada.readUTF();
            String descripcion = entrada.readUTF();
            String tipo = entrada.readUTF();

            Proyecto proyecto = proyectoDAO.obtenerPorId(Integer.parseInt(idProyecto));

            if (!nombre.isEmpty()) {
                proyecto.setNombre(nombre);
            }
            if (!descripcion.isEmpty()) {
                proyecto.setDescripcion(descripcion);
            }
            if (!tipo.isEmpty()) {
                proyecto.setTipo(tipo);
            }

            proyectoDAO.actualizar(proyecto);
            salida.writeUTF("Proyecto actualizado correctamente.");
        } catch (Exception e) {

        } finally {
            monitor.unlock();
        }

    }

    private void eliminarProyecto() {
    monitor.lock();
        try {
            String idProyecto = entrada.readUTF();
            Proyecto proyecto = proyectoDAO.obtenerPorId(Integer.parseInt(idProyecto));
            proyectoDAO.eliminar(proyecto);
            salida.writeUTF("Proyecto eliminado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar el proyecto: " + e.getMessage());
            e.printStackTrace();
        } finally {
            monitor.unlock();
        }
    }

    private void buscarPorTipo() {
        try {
            String tipo = entrada.readUTF();
            ArrayList<Proyecto> proyectos = (ArrayList<Proyecto>) proyectoDAO.obtenerPorTipo(tipo);
            salida.writeInt(proyectos.size());
            for (Proyecto proyecto : proyectos) {
                salida.writeUTF(proyecto.serializar());
            }
            salida.writeUTF("Proyectos listados correctamente");
        } catch (Exception e) {
            System.err.println("Error al listar los proyectos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void consultaTipo() {
        try {
            List<String> resultados = proyectoDAO.obtenerProyectosPorTipo();
            salida.writeInt(resultados.size());
            for (String resultado : resultados) {
                salida.writeUTF(resultado);
            }
            salida.writeUTF("Proyectos listados por tipos correctamente");
        } catch (Exception e) {
            System.err.println("Error al listar los proyectos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
