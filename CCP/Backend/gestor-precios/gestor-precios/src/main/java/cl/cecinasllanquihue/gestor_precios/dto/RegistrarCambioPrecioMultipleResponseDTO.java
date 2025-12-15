package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegistrarCambioPrecioMultipleResponseDTO {
    private String articuloCodigo;
    private List<RegistrarCambioPrecioResponseDTO> cambios;
}
