package cl.cecinasllanquihue.gestor_precios.service.impl;

import cl.cecinasllanquihue.gestor_precios.dto.ListaPrecioDTO;
import cl.cecinasllanquihue.gestor_precios.model.ListaPrecio;
import cl.cecinasllanquihue.gestor_precios.model.Sistema;
import cl.cecinasllanquihue.gestor_precios.repository.ListaPrecioRepository;
import cl.cecinasllanquihue.gestor_precios.repository.SistemaRepository;
import cl.cecinasllanquihue.gestor_precios.service.ListaPrecioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListaPrecioServiceImpl implements ListaPrecioService {

    private final ListaPrecioRepository listaPrecioRepository;
    private final SistemaRepository sistemaRepository;

    @Override
    public List<ListaPrecioDTO> obtenerTodas() {
        return listaPrecioRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<ListaPrecioDTO> obtenerListasPorSistema(Integer sistemaId) {
        return null;
    }

    @Override
    @Transactional
    public ListaPrecioDTO crearLista(ListaPrecioDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body vacío");
        }

        String nombre = dto.getNombre() == null ? null : dto.getNombre().trim();
        if (nombre == null || nombre.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre requerido");
        }

        Integer sistemaId = dto.getSistemaId();
        if (sistemaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sistema requerido");
        }

        Sistema sistema = sistemaRepository.findById(dto.getSistemaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sistema no encontrada"));

        ListaPrecio entity = new ListaPrecio();
        entity.setNombre(dto.getNombre().trim());
        entity.setSistema(sistema);

        ListaPrecio saved = listaPrecioRepository.save(entity);

        return new ListaPrecioDTO(
                saved.getId(),
                saved.getNombre(),
                saved.getSistema().getId(),
                saved.getSistema().getNombre()
        );
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
