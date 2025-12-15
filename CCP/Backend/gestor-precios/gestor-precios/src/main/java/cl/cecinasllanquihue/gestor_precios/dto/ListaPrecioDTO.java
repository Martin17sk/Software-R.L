package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaPrecioDTO {
    private Integer id;
    private String nombre;

    private Integer sistemaId;
    private String sistemaNombre;
}
