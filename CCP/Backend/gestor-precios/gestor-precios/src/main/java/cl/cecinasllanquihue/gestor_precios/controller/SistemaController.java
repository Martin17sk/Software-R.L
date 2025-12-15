package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.SistemaDTO;
import cl.cecinasllanquihue.gestor_precios.service.SistemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sistemas")
public class SistemaController {

    private final SistemaService sistemaService;

    public SistemaController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    @GetMapping
    public ResponseEntity<List<SistemaDTO>> listar() {
        List<SistemaDTO> sistemas = sistemaService.listarTodos();
        return ResponseEntity.ok(sistemas);
    }
}
