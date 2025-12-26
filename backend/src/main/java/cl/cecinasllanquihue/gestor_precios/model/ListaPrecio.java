package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lista_precio",
        uniqueConstraints = {
            @UniqueConstraint(name = "uq_lista_nombre_sistema", columnNames = {"lispre_sistema_id", "lispre_nombre"})
        },
        indexes = {
                @Index(name = "idx_lispre_sistema", columnList = "lispre_sistema_id"),
                @Index(name = "idx_lispre_nombre", columnList = "lispre_nombre")
        }
)
@Getter @Setter
public class ListaPrecio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lispre_id")
    private Integer id;

    @Column(name = "lispre_nombre", nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lispre_sistema_id", nullable = false)
    private Sistema sistema;
}
