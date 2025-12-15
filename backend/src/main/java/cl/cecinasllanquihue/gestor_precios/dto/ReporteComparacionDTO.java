package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReporteComparacionDTO {

    private List<ComparacionItemDTO> discrepancias;
    private Integer totalProcesados;
    private Integer totalSinDiscrepancia;
    private Integer totalConDiscrepancia;
    private Integer totalErroresEstructura;
}

