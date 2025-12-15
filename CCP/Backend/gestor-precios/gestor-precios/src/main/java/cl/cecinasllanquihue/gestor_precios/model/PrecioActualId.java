package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class PrecioActualId implements Serializable {

    @Column(name = "preact_articulo_codigo")
    private String articuloCodigo;

    @Column(name = "preact_lispre_id")
    private Integer listaPrecioId;
}

