package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "articulo")
@Data
public class Articulo {

    @Id
    @Column(name = "articulo_codigo", length = 10)
    private String codigo;

    @Column(name = "articulo_nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "articulo_um", nullable = false, length = 40)
    private String unidadMedida;
}
