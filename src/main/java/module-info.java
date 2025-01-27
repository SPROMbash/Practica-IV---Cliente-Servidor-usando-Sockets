module com.example.pr4sockets {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    opens com.example.pr4sockets to javafx.fxml;
    exports com.example.pr4sockets;
    exports controlador;
    opens controlador to javafx.fxml;
    exports dao;
    opens dao to org.hibernate.orm.core;
    exports modelo;
    opens modelo to org.hibernate.orm.core;
    exports controlador.usuario;
    opens controlador.usuario to javafx.fxml;
    exports controlador.proyecto;
    opens controlador.proyecto to javafx.fxml;
    exports controlador.relaciones;
    opens controlador.relaciones to javafx.fxml;
}