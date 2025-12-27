package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ArticuloPrecioActualDTO {
    private String codigo;
    private String nombre;
    private Integer listaPrecioId;
    private BigDecimal precioActual;    // puede ser null
}
