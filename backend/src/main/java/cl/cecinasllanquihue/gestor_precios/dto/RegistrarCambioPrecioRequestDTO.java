package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RegistrarCambioPrecioRequestDTO {

    private String articuloCodigo;
    private Integer listaPrecioId;
    private BigDecimal precioNuevo;
    private String observacion;
}
