package cl.cecinasllanquihue.gestor_precios.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private Integer usuarioId;
    private String username;

    public LoginResponse(Integer usuarioId, String username) {
        this.usuarioId = usuarioId;
        this.username = username;
    }
}
