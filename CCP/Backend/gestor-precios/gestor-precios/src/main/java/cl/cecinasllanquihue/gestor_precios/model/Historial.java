package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_historial")
@Data
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regh_id")
    private Integer id;

    @Column(name = "regh_fechahora")
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regh_usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regh_articulo_codigo")
    private Articulo articulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regh_lispre_id")
    private ListaPrecio listaPrecio;

    @Column(name = "regh_precioant")
    private BigDecimal precioAnterior;

    @Column(name = "regh_precionue")
    private BigDecimal precioNuevo;

    @Column(name = "regh_observacion")
    private String observacion;
}

