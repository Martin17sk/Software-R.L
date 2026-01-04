package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "registro_historial",
        indexes = {
                @Index(name = "idx_hist_articulo", columnList = "regh_articulo_codigo"),
                @Index(name = "idx_hist_lista", columnList = "regh_lispre_id"),
                @Index(name = "idx_hist_fecha", columnList = "regh_fechahora")
        }
)
@Getter
@Setter
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regh_id")
    private Integer id;

    @Column(name = "regh_fechahora", nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "regh_usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "regh_articulo_codigo", nullable = false)
    private Articulo articulo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "regh_lispre_id", nullable = false)
    private ListaPrecio listaPrecio;

    @Column(name = "regh_precioant")
    private BigDecimal precioAnterior;

    @Column(name = "regh_precionue", nullable = false)
    private BigDecimal precioNuevo;

    @Column(name = "regh_observacion", length = 255)
    private String observacion;
}

