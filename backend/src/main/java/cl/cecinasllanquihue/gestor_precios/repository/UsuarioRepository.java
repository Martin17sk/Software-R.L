package cl.cecinasllanquihue.gestor_precios.repository;

import cl.cecinasllanquihue.gestor_precios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

    Optional<Usuario> findByNombre(String nombre);
}
