package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(
        name = "precio_actual",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "preact_articulo_codigo",
                        "preact_lispre_id"
                })
        })
@Data
public class PrecioActual {

    @EmbeddedId
    private PrecioActualId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "preact_articulo_codigo",
            insertable = false,
            updatable = false
    )
    private Articulo articulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "preact_lispre_id",
            insertable = false,
            updatable = false
    )
    private ListaPrecio listaPrecio;

    @Column(name = "preact_precio", nullable = false)
    private BigDecimal precio;
}
