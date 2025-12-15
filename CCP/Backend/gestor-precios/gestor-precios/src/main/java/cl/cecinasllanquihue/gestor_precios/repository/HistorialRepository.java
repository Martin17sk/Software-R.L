package cl.cecinasllanquihue.gestor_precios.repository;

import cl.cecinasllanquihue.gestor_precios.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistorialRepository extends JpaRepository<Historial, Integer> {

    // Historial por artículo, ordenado de más nuevo a más antiguo
    List<Historial> findByArticulo_CodigoOrderByFechaHoraDesc(String codigoArticulo);

    // Para filtrar por lista
    List<Historial> findByListaPrecioIdOrderByFechaHoraDesc(Integer listaPrecioId);

    // Historial paginado por artículo + lista
    Page<Historial> findByArticulo_CodigoAndListaPrecio_Id(
            String codigoArticulo,
            Integer listaPrecioId,
            Pageable pageable
    );

    List<Historial> findByArticulo_CodigoAndListaPrecio_IdOrderByFechaHoraDesc(
            String articuloCodigo,
            Integer listaPrecioId
    );

    @Query("""
            SELECT h
            FROM Historial h
            WHERE (:search IS NULL OR :search = '' 
               OR LOWER(h.articulo.codigo) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(h.articulo.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(h.listaPrecio.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(h.listaPrecio.sistema.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(h.usuario.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            """)
    Page<Historial> buscarConFiltro(
            @Param("search") String search,
            Pageable pageable
    );
}
