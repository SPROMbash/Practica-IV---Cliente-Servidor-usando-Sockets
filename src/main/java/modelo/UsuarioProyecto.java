package modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_proyecto")
public class UsuarioProyecto {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    public UsuarioProyecto() {

    }

    public UsuarioProyecto(Long id) {
        this.id = id;
    }

    public UsuarioProyecto(Long id, Usuario usuario, Proyecto proyecto) {
        this.id = id;
        this.usuario = usuario;
        this.proyecto = proyecto;
    }

    public UsuarioProyecto(Usuario usuario, Proyecto proyecto) {
        this.usuario = usuario;
        this.proyecto = proyecto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public static UsuarioProyecto deserializar(String linea) {
        String[] campos = linea.split(",");
        Usuario usuario = new Usuario(Long.parseLong(campos[1]), campos[2], campos[3], campos[4]);
        Proyecto proyecto = new Proyecto(Long.parseLong(campos[5]), campos[6], campos[7], campos[8]);
        return new UsuarioProyecto(Long.parseLong(campos[0]), usuario, proyecto);
    }

    @Override
    public String toString() {
        return id + "," + usuario+ "," + proyecto;
    }
}
