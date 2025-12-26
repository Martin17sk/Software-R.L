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
    List<Historial> findByListaPrecio_IdOrderByFechaHoraDesc(Integer listaPrecioId);

    // Historial paginado por artículo
    Page<Historial> findByArticulo_CodigoAndListaPrecio_Id(
            String codigoArticulo,
            Integer listaPrecioId,
            Pageable pageable
    );

    @Query("""
    SELECT h
    FROM Historial h
    JOIN h.articulo a
    JOIN h.listaPrecio lp
    JOIN lp.sistema s
    JOIN h.usuario u
    WHERE (:search IS NULL OR :search = ''
       OR LOWER(a.codigo) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(lp.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(s.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
    )
    """)
    Page<Historial> buscarConFiltro(
            @Param("search") String search,
            Pageable pageable
    );
}
