package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HistorialDTO {

    private Integer id;
    private LocalDateTime fechaHora;

    private Integer usuarioId;
    private String usuarioNombre;

    private String articuloCodigo;
    private String unidadMedida;

    private Integer listaPrecioId;
    private String listaPrecioNombre;

    private BigDecimal precioAnterior;
    private BigDecimal precioNuevo;

    private String observacion;
}

