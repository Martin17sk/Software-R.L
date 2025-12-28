package cl.cecinasllanquihue.gestor_precios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioCreateDTO {
    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotBlank
    @Size(min = 6, max = 100)
    private String contrasena;

    private Boolean activo = true;
}
