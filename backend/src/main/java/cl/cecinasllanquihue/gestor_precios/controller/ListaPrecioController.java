package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.ListaPrecioDTO;
import cl.cecinasllanquihue.gestor_precios.service.ListaPrecioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas-precio")
public class ListaPrecioController {

    private final ListaPrecioService listaPrecioService;

    public ListaPrecioController(ListaPrecioService listaPrecioService) {
        this.listaPrecioService = listaPrecioService;
    }

    @GetMapping
    public ResponseEntity<List<ListaPrecioDTO>> obtenerTodas() {
        return ResponseEntity.ok(listaPrecioService.obtenerTodas());
    }

    @GetMapping("/sistema/{sistemaId}")
    public ResponseEntity<List<ListaPrecioDTO>> obtenerPorSistema(@PathVariable Integer sistemaId) {
        return ResponseEntity.ok(listaPrecioService.obtenerListasPorSistema(sistemaId));
    }

    @PostMapping
    public ResponseEntity<ListaPrecioDTO> crear(@RequestBody ListaPrecioDTO dto) {
        ListaPrecioDTO creada = listaPrecioService.crearLista(dto);
        if (creada == null) throw new IllegalArgumentException("Servicio devolvio null");
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }
}
