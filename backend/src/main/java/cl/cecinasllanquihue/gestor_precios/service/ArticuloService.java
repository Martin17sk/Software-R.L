package cl.cecinasllanquihue.gestor_precios.service;

import cl.cecinasllanquihue.gestor_precios.dto.ArticuloConPrecioActualDTO;
import cl.cecinasllanquihue.gestor_precios.model.Articulo;

import java.util.List;

public interface ArticuloService {

    List<String> listarNombres();

    List<ArticuloConPrecioActualDTO> buscarPorNombre(String query, Integer listaPrecioId);

    ArticuloConPrecioActualDTO obtenerPorCodigoYLista(String codigo, Integer listaPrecioId);

    Articulo crear(Articulo articulo);
    Articulo actualizar(String codigo, Articulo datos);
}
