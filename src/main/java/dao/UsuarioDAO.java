package dao;

import Hibernate.HibernateUtil;
import modelo.Usuario;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends GenericDAO<Usuario> {
    public UsuarioDAO() {
        super(Usuario.class);
    }

    public List<Usuario> obtenerPorNombre(String nombre) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Usuario where nombre like :nombre", Usuario.class)
                    .setParameter("nombre", "%" + nombre + "%")
                    .list();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<Usuario> obtenerPorEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Usuario where email like :email", Usuario.class)
                    .setParameter("email", "%" + email + "%")
                    .list();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<Usuario> obtenerUsuariosSinProyectos() {
        List<Usuario> usuarios = new ArrayList<>();
        String hql = """
        SELECT u 
        FROM Usuario u 
        WHERE u.id NOT IN (SELECT up.usuario.id FROM usuario_proyecto up)
    """;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            usuarios = session.createQuery(hql, Usuario.class).list();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return usuarios;
    }
}
