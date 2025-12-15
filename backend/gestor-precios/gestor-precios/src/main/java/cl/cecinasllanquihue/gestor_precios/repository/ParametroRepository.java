package cl.cecinasllanquihue.gestor_precios.repository;

import cl.cecinasllanquihue.gestor_precios.model.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParametroRepository extends JpaRepository<Parametro, String> {
}
