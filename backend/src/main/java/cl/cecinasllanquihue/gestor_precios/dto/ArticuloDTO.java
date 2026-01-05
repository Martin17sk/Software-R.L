package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloDTO {
    private String codigo;
    private String nombre;
    private String unidadMedida;
}
