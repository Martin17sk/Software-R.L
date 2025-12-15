package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarCambioPrecioResponseDTO {
    private Integer registroHistorialId;

    private String articuloCodigo;
    private Integer listaPrecioId;

    private BigDecimal precioAnterior;
    private BigDecimal precioNuevo;

    private LocalDateTime fechaHora;

    private Integer usuarioId;

    private String observacion;
}
