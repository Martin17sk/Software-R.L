package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.UsuarioCreateDTO;
import cl.cecinasllanquihue.gestor_precios.dto.UsuarioDTO;
import cl.cecinasllanquihue.gestor_precios.model.Usuario;
import cl.cecinasllanquihue.gestor_precios.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,  PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioDTO(u.getId(), u.getNombre(), u.isActivo()))
                .toList();
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioCreateDTO body) {
        String nombre = body.getNombre().trim();

        if (usuarioRepository.findByNombre(nombre).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Usuario u = Usuario.builder()
                .nombre(nombre)
                .contrasena(passwordEncoder.encode(body.getContrasena()))
                .activo(body.getActivo() != null ? body.getActivo() : true)
                .build();

        Usuario saved = usuarioRepository.save(u);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new  UsuarioDTO(saved.getId(), saved.getNombre(), saved.isActivo()));
    }
}
