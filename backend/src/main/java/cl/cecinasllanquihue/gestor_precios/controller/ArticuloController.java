package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.ArticuloConPrecioActualDTO;
import cl.cecinasllanquihue.gestor_precios.dto.ArticuloOptionDTO;
import cl.cecinasllanquihue.gestor_precios.model.Articulo;
import cl.cecinasllanquihue.gestor_precios.service.ArticuloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    private final ArticuloService articuloService;

    public ArticuloController(ArticuloService articuloService) {
        this.articuloService = articuloService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ArticuloConPrecioActualDTO>> buscar(
            @RequestParam String q,
            @RequestParam Integer listaPrecioId
    ) {
        return ResponseEntity.ok(
                articuloService.buscarPorNombre(q, listaPrecioId)
        );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ArticuloConPrecioActualDTO> obtenerPorCodigo(
            @PathVariable String codigo,
            @RequestParam Integer listaPrecioId
    ) {
        ArticuloConPrecioActualDTO dto = articuloService.obtenerPorCodigoYLista(codigo, listaPrecioId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Articulo> crearArticulo(@RequestBody Articulo articulo){
        Articulo creado = articuloService.crear(articulo);
        return ResponseEntity.ok(creado);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Articulo> actualizarArticulo(
            @PathVariable String codigo,
            @RequestBody Articulo articulo
    ) {
        Articulo actualizado = articuloService.actualizar(codigo, articulo);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/options")
    public ResponseEntity<List<ArticuloOptionDTO>> listarOptions() {
        return ResponseEntity.ok(articuloService.listarOptions());
    }
}
