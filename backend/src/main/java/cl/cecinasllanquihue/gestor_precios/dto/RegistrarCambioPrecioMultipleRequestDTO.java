package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegistrarCambioPrecioMultipleRequestDTO {
    private String articuloCodigo;
    private List<CambioEnListaDTO> cambios;
}
