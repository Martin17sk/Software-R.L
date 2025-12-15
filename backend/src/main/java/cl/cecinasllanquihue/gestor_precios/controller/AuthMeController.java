package cl.cecinasllanquihue.gestor_precios.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.cecinasllanquihue.gestor_precios.model.Usuario;
import cl.cecinasllanquihue.gestor_precios.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthMeController {

    private final UsuarioRepository usuarioRepository;

    public AuthMeController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String username = principal .getName();
        Usuario usuario = usuarioRepository.findByNombre(username)
            .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(
            Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre()
            )
        );
    }
}
