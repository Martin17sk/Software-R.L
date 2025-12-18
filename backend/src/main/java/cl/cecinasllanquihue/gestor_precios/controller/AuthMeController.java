package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.LoginResponse;
import cl.cecinasllanquihue.gestor_precios.model.Usuario;
import cl.cecinasllanquihue.gestor_precios.repository.UsuarioRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthMeController {

    private final UsuarioRepository usuarioRepository;

    public AuthMeController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Usuario usuario = usuarioRepository.findByNombre(authentication.getName())
                .orElse(null);

        if (usuario == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(new LoginResponse(usuario.getId(), usuario.getNombre()));
    }

}
