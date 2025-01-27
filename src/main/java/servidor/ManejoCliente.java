package servidor;

import dao.ProyectoDAO;
import dao.UsuarioDAO;
import dao.UsuarioProyectoDAO;
import modelo.Proyecto;
import modelo.Usuario;
import modelo.UsuarioProyecto;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ManejoCliente implements Runnable {
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private UsuarioDAO usuarioDAO;
    private ProyectoDAO proyectoDAO;
    private UsuarioProyectoDAO usuarioProyectoDAO;
    private ReentrantLock monitor = new ReentrantLock();


    public ManejoCliente(Socket socket) {
        this.socket = socket;
        try  {
            this.salida = new DataOutputStream(socket.getOutputStream());
            this.entrada = new DataInputStream(socket.getInputStream());
            this.usuarioDAO = new UsuarioDAO();
            this.proyectoDAO = new ProyectoDAO();
            this.usuarioProyectoDAO = new UsuarioProyectoDAO();
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
            } else if (peticion.startsWith("up")) {
                procesarPeticionUsuarioProyecto(peticion.split(":")[1]);
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

    private void procesarPeticionUsuarioProyecto(String peticion) {
        switch (peticion) {
            case "listar":
                listarUsuariosProyectos();
                break;
            case "proyectos":
                listarProyectosAsignadosAUsuario();
                break;
            case "!proyectos":
                listarProyectosNoAsignadosAUsuario();
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
            List<Usuario> usuarios = usuarioDAO.getTodos();
            if (usuarios != null) {
                salida.writeInt(usuarios.size());
                for (Usuario usuario : usuarios) {
                    salida.writeUTF(usuario.toString());
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
            Long idUsuario = entrada.readLong();
            String nombre = entrada.readUTF();
            String apellidos = entrada.readUTF();
            String email = entrada.readUTF();

            Usuario usuario = usuarioDAO.obtenerPorId(idUsuario);

            if (!nombre.isEmpty()) {
                usuario.setNombre(nombre);
            }
            if (!apellidos.isEmpty()) {
                usuario.setApellidos(apellidos);
            }
            if (!email.isEmpty()) {
                usuario.setEmail(email);
            }

            usuarioDAO.actualizar(usuario);
            salida.writeUTF("Proyecto actualizado correctamente.");
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
            Long id = entrada.readLong();
            Usuario usuario = usuarioDAO.obtenerPorId(id);
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
            List<Usuario> usuarios = usuarioDAO.obtenerPorNombre(nombre);
            if (usuarios != null) {
                salida.writeInt(usuarios.size());
                for (Usuario usuario : usuarios) {
                    salida.writeUTF(usuario.toString());
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
            List<Usuario> usuarios = usuarioDAO.obtenerPorNombre(nombre);
            if (usuarios != null) {
                salida.writeInt(usuarios.size());
                for (Usuario usuario : usuarios) {
                    salida.writeUTF(usuario.toString());
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
        try {
            List<Proyecto> proyectos = proyectoDAO.getTodos();
            salida.writeInt(proyectos.size());
            for (Proyecto proyecto : proyectos) {
                salida.writeUTF(proyecto.toString());
            }
            salida.writeUTF("Usuarios listados correctamente");
        } catch (Exception e) {
            System.err.println("Error al listar los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void actualizarProyecto() {
        monitor.lock();
        try {
            Long idProyecto = entrada.readLong();
            String nombre = entrada.readUTF();
            String descripcion = entrada.readUTF();
            String tipo = entrada.readUTF();

            Proyecto proyecto = proyectoDAO.obtenerPorId(idProyecto);

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
            Long idProyecto = entrada.readLong();
            Proyecto proyecto = proyectoDAO.obtenerPorId(idProyecto);
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
            List<Proyecto> proyectos = proyectoDAO.obtenerPorTipo(tipo);
            salida.writeInt(proyectos.size());
            for (Proyecto proyecto : proyectos) {
                salida.writeUTF(proyecto.toString());
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

    private void listarUsuariosProyectos() {
        try {
            List<UsuarioProyecto> usuarioProyectos = usuarioProyectoDAO.getTodos();
            if (usuarioProyectos != null) {
                salida.writeInt(usuarioProyectos.size()); // Enviar tamaño de la lista
                for (UsuarioProyecto usuarioProyecto : usuarioProyectos) {
                    salida.writeUTF(usuarioProyecto.toString()); // Enviar cada elemento serializado
                }
            } else {
                salida.writeInt(0); // Indicar que no hay datos
            }
        } catch (Exception e) {
            System.err.println("Error al listar los usuarios-proyectos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listarProyectosAsignadosAUsuario() {
        try {
            Long idUsuario = entrada.readLong();
            List<Proyecto> proyectos = usuarioProyectoDAO.obtenerProyectosAsignadosAUsuario(idUsuario);
            if (proyectos == null) {
                salida.writeInt(0);
                salida.writeUTF("No hay proyectos asignados al usuario");
                return;
            }
            salida.writeInt(proyectos.size());
            for (Proyecto proyecto : proyectos) {
                salida.writeUTF(proyecto.toString());
            }
            salida.writeUTF("Proyectos listados correctamente");
        } catch (Exception e) {
            System.err.println("Error al listar los proyectos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listarProyectosNoAsignadosAUsuario() {
        try {
            Long idUsuario = entrada.readLong();
           List<Proyecto> proyectos = usuarioProyectoDAO.obtenerProyectosNoAsignadosAUsuario(idUsuario);
            salida.writeInt(proyectos.size());
            for (Proyecto proyecto : proyectos) {
                salida.writeUTF(proyecto.toString());
            }
            salida.writeUTF("Proyectos listados correctamente");
        } catch (Exception e) {
            System.err.println("Error al listar los proyectos: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
