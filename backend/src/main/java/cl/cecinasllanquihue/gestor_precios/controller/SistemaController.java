package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.SistemaDTO;
import cl.cecinasllanquihue.gestor_precios.repository.SistemaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sistemas")
public class SistemaController {

    private final SistemaRepository sistemaRepository;

    public SistemaController(SistemaRepository sistemaRepository) {
        this.sistemaRepository = sistemaRepository;
    }

    @GetMapping
    public List<SistemaDTO> listar() {
        return sistemaRepository.findAll().stream().map(s -> new SistemaDTO(s.getId(), s.getNombre())).toList();
    }
}
