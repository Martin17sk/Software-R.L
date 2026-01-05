package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "sistema",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_sistema_nombre",
                        columnNames = "sistema_nombre"
                )
        },
        indexes = {
                @Index(name = "idx_sistema_nombre", columnList = "sistema_nombre")
        }
)
@Getter @Setter
public class Sistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sistema_id")
    private Integer id;

    @Column(name = "sistema_nombre", nullable = false, length = 50)
    private String nombre;
}
