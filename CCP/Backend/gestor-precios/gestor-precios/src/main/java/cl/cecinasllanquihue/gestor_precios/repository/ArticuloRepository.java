package cl.cecinasllanquihue.gestor_precios.repository;

import cl.cecinasllanquihue.gestor_precios.model.Articulo;
import cl.cecinasllanquihue.gestor_precios.dto.ArticuloConPrecioActualDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticuloRepository extends JpaRepository<Articulo, String> {

    Optional<Articulo> findByCodigo(String codigo);

    @Query("SELECT a FROM Articulo a " +
            "WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Articulo> findByNombre(@Param("filtro") String filtro);

    boolean existsByCodigo(String codigo);

    List<Articulo> findByNombreContainingIgnoreCase(String nombre);

    //Busca artículos por nombre/código y devuelve el precio actual en una lista de precios específica.
    @Query("""
        SELECT new cl.cecinasllanquihue.gestor_precios.dto.ArticuloConPrecioActualDTO(
            a.codigo,
            a.nombre,
            a.unidadMedida,
            lp.id,
            lp.nombre,
            pa.precio,
            s.id,
            s.nombre
        )
        FROM PrecioActual pa
        JOIN pa.articulo a
        JOIN pa.listaPrecio lp
        JOIN lp.sistema s
        WHERE lp.id = :listaPrecioId
          AND (
                LOWER(a.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))
             OR LOWER(a.codigo) LIKE LOWER(CONCAT('%', :filtro, '%'))
          )
        ORDER BY a.nombre ASC
        """)
    List<ArticuloConPrecioActualDTO> buscarConPrecioPorNombre(
            @Param("filtro") String filtro,
            @Param("listaPrecioId") Integer listaPrecioId
    );

    //Obtiene un artículo por código con su precio actual en una lista de precios específica.

    @Query("""
        SELECT new cl.cecinasllanquihue.gestor_precios.dto.ArticuloConPrecioActualDTO(
            a.codigo,
            a.nombre,
            a.unidadMedida,
            lp.id,
            lp.nombre,
            pa.precio,
            s.id,
            s.nombre
        )
        FROM PrecioActual pa
        JOIN pa.articulo a
        JOIN pa.listaPrecio lp
        JOIN lp.sistema s
        WHERE lp.id = :listaPrecioId
          AND a.codigo = :codigo
        """)
    List<ArticuloConPrecioActualDTO> buscarConPrecioPorCodigo(
            @Param("codigo") String codigo,
            @Param("listaPrecioId") Integer listaPrecioId
    );

    @Query("SELECT a.nombre FROM Articulo a")
    List<String> findAllNames();
}

