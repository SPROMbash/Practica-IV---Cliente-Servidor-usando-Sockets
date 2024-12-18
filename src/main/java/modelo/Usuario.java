package modelo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "email")
    private String email;

    @ManyToMany (cascade = CascadeType.ALL)
    @JoinTable(
        name = "usuario_proyecto",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "proyecto_id")
    )
    private List<Proyecto> proyectos;

    public Usuario() {

    }

    public Usuario(String nombre, String email, String apellidos) {
        this.nombre = nombre;
        this.email = email;
        this.apellidos = apellidos;
    }

    public Usuario(String nombre, String apellidos, String email, List<Proyecto> proyectos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.proyectos = proyectos;
    }

    public Usuario(Long id, String nombre, String apellidos, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
    }

    public Usuario(Long id, String nombre, String apellidos, String email, List<Proyecto> proyectos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.proyectos = proyectos;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public String serializar() {
        return id + "," + nombre + "," + apellidos + "," + email;
    }

    @Override
    public String toString() {
        return "Usuario -> id: " + id + " || nombre: " + nombre + " || apellidos: " + apellidos + " || email: " + email + " || proyectos: " + proyectos;
    }
}
