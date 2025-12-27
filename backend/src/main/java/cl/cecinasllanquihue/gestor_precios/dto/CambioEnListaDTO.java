package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CambioEnListaDTO {
    private Integer listaPrecioId;
    private BigDecimal precioNuevo;
    private String observacion;
}
