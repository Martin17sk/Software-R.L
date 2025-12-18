package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.LoginResponse;
import cl.cecinasllanquihue.gestor_precios.model.Usuario;
import cl.cecinasllanquihue.gestor_precios.service.UsuarioSesionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthMeController {

    private final UsuarioSesionService usuarioSesionService;

    public AuthMeController(UsuarioSesionService usuarioSesionService) {
        this.usuarioSesionService = usuarioSesionService;
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse> me() {
        Usuario usuario = usuarioSesionService.getUsuarioActual();
        if (usuario == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(new LoginResponse(usuario.getId(),usuario.getNombre()));
    }
}
