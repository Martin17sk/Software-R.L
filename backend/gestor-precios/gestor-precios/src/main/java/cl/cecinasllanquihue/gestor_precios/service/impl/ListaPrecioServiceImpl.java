package cl.cecinasllanquihue.gestor_precios.service.impl;

import cl.cecinasllanquihue.gestor_precios.dto.ListaPrecioDTO;
import cl.cecinasllanquihue.gestor_precios.model.ListaPrecio;
import cl.cecinasllanquihue.gestor_precios.model.Sistema;
import cl.cecinasllanquihue.gestor_precios.repository.ListaPrecioRepository;
import cl.cecinasllanquihue.gestor_precios.repository.SistemaRepository;
import cl.cecinasllanquihue.gestor_precios.service.ListaPrecioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListaPrecioServiceImpl implements ListaPrecioService {

    private final ListaPrecioRepository listaPrecioRepository;
    private final SistemaRepository sistemaRepository;

    public ListaPrecioServiceImpl(ListaPrecioRepository listaPrecioRepository,
                                  SistemaRepository sistemaRepository) {
        this.listaPrecioRepository = listaPrecioRepository;
        this.sistemaRepository = sistemaRepository;
    }


    @Override
    public List<ListaPrecioDTO> obtenerListasPorSistema(Integer sistemaId) {
        List<ListaPrecio> listas = listaPrecioRepository.findBySistema_Id(sistemaId);
        return listas.stream().map(this::toDTO).toList();
    }

    @Override
    public ListaPrecioDTO crearLista(ListaPrecioDTO dto) {
        Sistema sistema = sistemaRepository.findById(dto.getSistemaId())
                .orElseThrow(() -> new EntityNotFoundException("Sistema no encontrado"));

        boolean existe = listaPrecioRepository
                .existsBySistema_IdAndNombreIgnoreCase(sistema.getId(), dto.getNombre());
        if (existe) {
            throw new IllegalStateException("Ya existe una lista con ese nombre para este sistema.");
        }

        ListaPrecio entidad = new ListaPrecio();
        entidad.setNombre(dto.getNombre());
        entidad.setSistema(sistema);

        ListaPrecio guardada = listaPrecioRepository.save(entidad);
        return toDTO(guardada);
    }

    private ListaPrecioDTO toDTO(ListaPrecio lp) {
        ListaPrecioDTO dto = new ListaPrecioDTO();
        dto.setId(lp.getId());
        dto.setNombre(lp.getNombre());
        dto.setSistemaId(lp.getSistema().getId());
        dto.setSistemaNombre(lp.getSistema().getNombre());
        return dto;
    }
}
