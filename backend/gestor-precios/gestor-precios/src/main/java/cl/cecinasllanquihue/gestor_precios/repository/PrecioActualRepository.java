package cl.cecinasllanquihue.gestor_precios.repository;

import cl.cecinasllanquihue.gestor_precios.model.PrecioActual;
import cl.cecinasllanquihue.gestor_precios.model.PrecioActualId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrecioActualRepository extends JpaRepository<PrecioActual, PrecioActualId> {

    // Precio actual de un artículo en una lista
    Optional<PrecioActual> findByArticulo_CodigoAndListaPrecio_Id(String codigoArticulo, Integer listaPrecioId);

    // Todos los precios de una lista
    List<PrecioActual> findByListaPrecio_Id(Integer listaPrecioId);

    // Todos los precios de un conjunto de artículos en una lista (para comparación masiva)
    List<PrecioActual> findByListaPrecio_IdAndArticulo_CodigoIn(Integer listaPrecioId, List<String> codigos);
}

