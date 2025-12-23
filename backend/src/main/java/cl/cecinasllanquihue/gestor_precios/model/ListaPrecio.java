package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lista_precio")
@Data
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
