package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PrecioActualId implements Serializable {

    @Getter
    @Column(name = "preact_articulo_codigo")
    private String articuloCodigo;

    @Getter
    @Column(name = "preact_lispre_id")
    private Integer listaPrecioId;

    public PrecioActualId() {}

    public PrecioActualId(String articuloCodigo, Integer listaPrecioId) {
        this.articuloCodigo = articuloCodigo;
        this.listaPrecioId = listaPrecioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrecioActualId)) return false;
        PrecioActualId that = (PrecioActualId) o;
        return Objects.equals(articuloCodigo, that.articuloCodigo)
                && Objects.equals(listaPrecioId, that.listaPrecioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articuloCodigo, listaPrecioId);
    }
}

