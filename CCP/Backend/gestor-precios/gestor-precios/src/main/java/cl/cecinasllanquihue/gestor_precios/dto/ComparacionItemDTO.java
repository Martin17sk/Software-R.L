package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComparacionItemDTO {

    private String codigoArticulo;
    private String nombreArticulo;

    private Integer listaAId;
    private String listaANombre;
    private BigDecimal precioA;

    private Integer listaBId;
    private String listaBNombre;
    private BigDecimal precioB;

    private BigDecimal diferencia;
    private String tipoDiscrepancia;
    // por ejemplo: "PRECIO_DISTINTO", "NO_EN_A", "NO_EN_B", "STRUCT_ERROR"
}

