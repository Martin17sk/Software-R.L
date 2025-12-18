package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.LoginRequest;
import cl.cecinasllanquihue.gestor_precios.dto.LoginResponse;
import cl.cecinasllanquihue.gestor_precios.model.Usuario;
import cl.cecinasllanquihue.gestor_precios.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @RequestBody LoginRequest request, 
        HttpServletRequest httpRequest, 
        HttpServletResponse httpResponse
    ) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            httpRequest.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                context
            );

            Usuario usuario = usuarioRepository.findByNombre(request.getUsername()).orElseThrow();

            return ResponseEntity.ok(new LoginResponse(usuario.getId(), usuario.getNombre()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        SecurityContextHolder.clearContext();

        return ResponseEntity.noContent().build();
    }
}
