package cl.cecinasllanquihue.gestor_precios.service.impl;

import cl.cecinasllanquihue.gestor_precios.model.Usuario;
import cl.cecinasllanquihue.gestor_precios.repository.UsuarioRepository;
import cl.cecinasllanquihue.gestor_precios.service.UsuarioSesionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioSesionServiceImpl implements UsuarioSesionService {

    private static final String ATTR_USUARIO_ID = "USUARIO_ID";

    private final UsuarioRepository usuarioRepository;

    public UsuarioSesionServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario getUsuarioActual(){

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("No hay request activa");
        }

        HttpSession session = attributes.getRequest().getSession(false);
        if (session == null) {
            throw new IllegalStateException("No hay sesión activa. Usuario no autenticado.");
        }

        Integer usuarioId = (Integer) session.getAttribute(ATTR_USUARIO_ID);
        if (usuarioId == null) {
            throw new IllegalStateException("No se encontró USUARIO_ID en la sesión. Usuario no autenticado.");
        }

        return usuarioRepository.findById(usuarioId)
                .orElseThrow(()-> new IllegalStateException("Usuario de sesión no existe en BD"));
    }
}
