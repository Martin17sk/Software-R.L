package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.LoginRequest;
import cl.cecinasllanquihue.gestor_precios.dto.LoginResponse;
import cl.cecinasllanquihue.gestor_precios.model.Usuario;
import cl.cecinasllanquihue.gestor_precios.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContext;


@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        var token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication auth = authenticationManager.authenticate(token);

        // Crear contexto y setear auth
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // Forzar creación de sesión y persistir SecurityContext en la sesión
        httpRequest.getSession(true);
        securityContextRepository.saveContext(context, httpRequest, httpResponse);

        Usuario usuario = usuarioRepository.findByNombre(request.getUsername()).orElseThrow();
        return ResponseEntity.ok(new LoginResponse(usuario.getId(), usuario.getNombre()));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();


        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
