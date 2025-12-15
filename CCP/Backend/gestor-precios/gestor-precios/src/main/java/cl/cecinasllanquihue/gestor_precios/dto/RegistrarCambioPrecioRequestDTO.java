package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RegistrarCambioPrecioRequestDTO {

    private String articuloCodigo;   // escribes código y autocompletas
    private Integer listaPrecioId;   // lista seleccionada
    private BigDecimal precioNuevo;  // PRE-03 / PRE-04
    private String observacion;      // PRE-05, opcional (<=255)
}
