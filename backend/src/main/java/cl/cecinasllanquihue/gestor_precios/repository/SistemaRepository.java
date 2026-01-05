package cl.cecinasllanquihue.gestor_precios.repository;

import cl.cecinasllanquihue.gestor_precios.model.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface SistemaRepository extends JpaRepository<Sistema,Integer> {

    Optional<Sistema> findByNombreIgnoreCase(String nombre);
}
