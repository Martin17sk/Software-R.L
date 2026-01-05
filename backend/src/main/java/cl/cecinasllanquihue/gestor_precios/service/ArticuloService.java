package cl.cecinasllanquihue.gestor_precios.service;

import cl.cecinasllanquihue.gestor_precios.dto.ArticuloConPrecioActualDTO;
import cl.cecinasllanquihue.gestor_precios.dto.ArticuloOptionDTO;
import cl.cecinasllanquihue.gestor_precios.dto.ArticuloPrecioActualDTO;
import cl.cecinasllanquihue.gestor_precios.dto.ArticuloUpdateDTO;
import cl.cecinasllanquihue.gestor_precios.model.Articulo;

import java.util.List;

public interface ArticuloService {

    List<ArticuloConPrecioActualDTO> buscarPorNombre(String query, Integer listaPrecioId);

    ArticuloConPrecioActualDTO obtenerPorCodigoYLista(String codigo, Integer listaPrecioId);

    Articulo crear(Articulo articulo);
    Articulo actualizar(String codigo, Articulo datos);

    List<ArticuloOptionDTO> listarOptions();

    Articulo obtenerPorCodigo(String codigo);

    ArticuloPrecioActualDTO obtenerPrecioActual(String codigo, Integer listaPrecioId);

    Articulo actualizarBasico(String codigo, ArticuloUpdateDTO body);
}
