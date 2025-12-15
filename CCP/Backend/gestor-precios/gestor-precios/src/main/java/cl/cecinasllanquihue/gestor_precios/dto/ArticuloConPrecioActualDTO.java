package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloConPrecioActualDTO {
    private String codigo;
    private String nombre;
    private String unidadMedida;

    private Integer listaPrecioId;
    private String listaPrecioNombre;
    private BigDecimal precioActual;

    private Integer sistemaId;
    private String sistemaNombre;
}
