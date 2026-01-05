package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private Integer id;
    private String nombre;
    private boolean activo;
}
