package cl.cecinasllanquihue.gestor_precios.service;

import cl.cecinasllanquihue.gestor_precios.dto.HistorialDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface HistorialService {

    List<HistorialDTO> obtenerHistorialPorArticulo(String codigoArticulo);
    List<HistorialDTO> buscar(String codigoArticulo, Integer listaPrecioId);
    Page<HistorialDTO> obtenerHistorialPorArticuloYLista(String codigoArticulo, Integer listaPrecioId, Pageable pageable);

    Page<HistorialDTO> buscarHistorial(String search, Pageable pageable);

    void eliminar(Integer idHistorial);
}
