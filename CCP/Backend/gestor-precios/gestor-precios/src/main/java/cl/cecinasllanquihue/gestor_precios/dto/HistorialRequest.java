package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HistorialRequest {
    private Integer usuarioId;
    private String articuloCodigo;
    private Integer lispreId;
    private BigDecimal precioAnterior;
    private BigDecimal precioNuevo;
    private String observacion;
}
