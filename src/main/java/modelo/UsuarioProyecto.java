package modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_proyecto")
public class UsuarioProyecto {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "proyecto_id")
    private Long proyectoId;

    public UsuarioProyecto() {

    }

    public UsuarioProyecto(Long usuarioId, Long proyectoId) {
        this.usuarioId = usuarioId;
        this.proyectoId = proyectoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    @Override
    public String toString() {
        return "Usuario-Proyecto --> id: " + id + "|| usuarioId: " + usuarioId + "|| proyectoId: " + proyectoId;
    }
}
