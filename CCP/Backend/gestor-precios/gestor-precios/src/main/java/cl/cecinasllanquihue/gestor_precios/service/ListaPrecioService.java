package cl.cecinasllanquihue.gestor_precios.service;

import cl.cecinasllanquihue.gestor_precios.dto.ListaPrecioDTO;

import java.util.List;

public interface ListaPrecioService {
    List<ListaPrecioDTO> obtenerListasPorSistema(Integer sistemaId);

    ListaPrecioDTO crearLista(ListaPrecioDTO dto);
}
