package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sistema")
@Data
public class Sistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sistema_id")
    private Integer id;

    @Column(name = "sistema_nombre", nullable = false, length = 50)
    private String nombre;
}
