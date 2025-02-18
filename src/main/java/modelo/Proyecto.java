package modelo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "proyecto")
public class Proyecto implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo")
    private String tipo;

    @ManyToMany (mappedBy = "proyectos", cascade = CascadeType.ALL)
    private List<Usuario> usuarios;

    public Proyecto() {

    }

    public Proyecto(Long id) {
        this.id = id;
    }

    public Proyecto(String nombre, String descripcion, String tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public Proyecto(String nombre, String descripcion, String tipo, List<Usuario> usuarios) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.usuarios = usuarios;
    }

    public Proyecto(Long id, String nombre, String descripcion, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public Proyecto(Long id, String nombre, String descripcion, String tipo, List<Usuario> usuarios) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.usuarios = usuarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public static Proyecto deserializar(String linea) {
        String[] campos = linea.split(",");
        return new Proyecto(Long.parseLong(campos[0]), campos[1], campos[2], campos[3]);
    }

    public void addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public void removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
    }

     @Override
    public String toString() {
         return id + "," + nombre + "," + descripcion + "," + tipo;
    }
}
