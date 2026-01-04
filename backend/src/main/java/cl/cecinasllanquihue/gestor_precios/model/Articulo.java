package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "articulo",
        indexes = {
            @Index(name = "idx_articulo_nombre", columnList = "articulo_nombre")
        }
)
@Getter @Setter
public class Articulo {

    @Id
    @Column(name = "articulo_codigo", length = 10, nullable = false)
    private String codigo;

    @Column(name = "articulo_nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "articulo_um", nullable = false, length = 40)
    private String unidadMedida;
}
