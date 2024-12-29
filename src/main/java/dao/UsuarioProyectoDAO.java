package dao;

import Hibernate.HibernateUtil;
import modelo.Proyecto;
import modelo.Usuario;
import modelo.UsuarioProyecto;
import org.hibernate.Session;
import java.util.List;

public class UsuarioProyectoDAO extends GenericDAO<UsuarioProyecto> {
    public UsuarioProyectoDAO() {
        super(UsuarioProyecto.class);
    }

    public List<Proyecto> obtenerProyectosAsignadosAUsuario(Long idUsuario) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT p FROM Proyecto p WHERE p.id IN (SELECT up.proyecto.id FROM UsuarioProyecto up WHERE up.usuario.id = :idUsuario)";
            return session.createQuery(hql, Proyecto.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Proyecto> obtenerProyectosNoAsignadosAUsuario(Long idUsuario) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT p FROM Proyecto p WHERE p.id NOT IN (SELECT up.proyecto.id FROM UsuarioProyecto up WHERE up.usuario.id = :idUsuario)";
            return session.createQuery(hql, Proyecto.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void anadirProyectoAUsuario(Long idUsuario, Long idProyecto) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Usuario usuario = session.get(Usuario.class, idUsuario); // Conseguir el usuario.
            Proyecto proyecto = session.get(Proyecto.class, idProyecto); // Conseguir el proyecto.
            usuario.addProyecto(proyecto); // Añadir el proyecto al usuario.
            proyecto.addUsuario(usuario); // Añadir el usuario al proyecto.
            UsuarioProyecto usuarioProyecto = new UsuarioProyecto(usuario, proyecto);

            session.beginTransaction();
            session.merge(usuario);
            session.merge(proyecto);
            session.persist(usuarioProyecto);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarProyectoAUsuario(Long idUsuario, Long idProyecto) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Usuario usuario = session.get(Usuario.class, idUsuario); // Conseguir el usuario.
            Proyecto proyecto = session.get(Proyecto.class, idProyecto); // Conseguir el proyecto.
            usuario.removeProyecto(proyecto); // Eliminar el proyecto del usuario.
            proyecto.removeUsuario(usuario); // Eliminar el usuario del proyecto.
            UsuarioProyecto usuarioProyecto = new UsuarioProyecto(usuario, proyecto);

            session.beginTransaction();
            session.merge(usuario);
            session.merge(proyecto);
            session.remove(usuarioProyecto);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
