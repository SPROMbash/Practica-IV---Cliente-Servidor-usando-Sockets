package dao;

import Hibernate.HibernateUtil;
import modelo.Proyecto;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO extends GenericDAO<Proyecto>{
    public ProyectoDAO() {
        super(Proyecto.class);
    }

    public List<Proyecto> obtenerPorTipo(String tipo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Proyecto where tipo = :tipo", Proyecto.class)
                    .setParameter("tipo", tipo)
                    .list();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<String> obtenerProyectosPorTipo() {
        List<String> resultados = new ArrayList<>();
        String hql = "SELECT p.tipo, COUNT(p) FROM Proyecto p GROUP BY p.tipo";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> lista = session.createQuery(hql).list();
            for (Object[] fila : lista) {
                String tipo = (String) fila[0];
                Long cantidad = (Long) fila[1];
                resultados.add(tipo + ":" + cantidad);
            }
            return resultados;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
