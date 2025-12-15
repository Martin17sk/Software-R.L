package cl.cecinasllanquihue.gestor_precios.service.impl;

import cl.cecinasllanquihue.gestor_precios.dto.SistemaDTO;
import cl.cecinasllanquihue.gestor_precios.model.Sistema;
import cl.cecinasllanquihue.gestor_precios.repository.SistemaRepository;
import cl.cecinasllanquihue.gestor_precios.service.SistemaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SistemaServiceImpl implements SistemaService {

    private final SistemaRepository sistemaRepository;

    public SistemaServiceImpl(SistemaRepository sistemaRepository) {
        this.sistemaRepository = sistemaRepository;
    }

    @Override
    public List<SistemaDTO> listarTodos() {
        return sistemaRepository.findAll().stream().map(this::toDto).toList();
    }

    private SistemaDTO toDto(Sistema sistema) {
        return new SistemaDTO(sistema.getId(), sistema.getNombre());
    }
}
