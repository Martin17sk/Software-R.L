package cl.cecinasllanquihue.gestor_precios.repository;

import cl.cecinasllanquihue.gestor_precios.model.ListaPrecio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListaPrecioRepository extends JpaRepository<ListaPrecio, Integer> {

    List<ListaPrecio> findBySistema_Id(Integer sistemaId);

    boolean existsBySistema_IdAndNombreIgnoreCase(Integer sistemaId, String nombre);
}
