package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "precio_actual")
@Data
public class PrecioActual {

    @EmbeddedId
    private PrecioActualId id;

    @MapsId("articuloCodigo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preact_articulo_codigo")
    private Articulo articulo;

    @MapsId("listaPrecioId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preact_lispre_id")
    private ListaPrecio listaPrecio;

    @Column(name = "preact_precio")
    private BigDecimal precio;
}
