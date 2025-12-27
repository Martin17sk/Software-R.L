package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.*;
import cl.cecinasllanquihue.gestor_precios.model.Articulo;
import cl.cecinasllanquihue.gestor_precios.repository.ArticuloRepository;
import cl.cecinasllanquihue.gestor_precios.service.ArticuloService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    private final ArticuloService articuloService;
    private final ArticuloRepository articuloRepository;

    public ArticuloController(ArticuloService articuloService,  ArticuloRepository articuloRepository) {
        this.articuloService = articuloService;
        this.articuloRepository = articuloRepository;
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
    public ResponseEntity<ArticuloDTO> actualizarArticulo(
            @PathVariable String codigo,
            @Valid @RequestBody ArticuloUpdateDTO body
    ) {
        Articulo actualizado = articuloService.actualizarBasico(codigo, body);

        return ResponseEntity.ok(new ArticuloDTO(
                actualizado.getCodigo(),
                actualizado.getNombre(),
                actualizado.getUnidadMedida()
        ));
    }

    @GetMapping("/options")
    public ResponseEntity<List<ArticuloOptionDTO>> listarOptions() {
        return ResponseEntity.ok(articuloService.listarOptions());
    }

    @GetMapping("/{codigo}/basic")
    public ResponseEntity<Articulo> obtenerBasico(@PathVariable String codigo){
        return ResponseEntity.ok(articuloService.obtenerPorCodigo(codigo));
    }

    @GetMapping("/{codigo}/precio-actual")
    public ResponseEntity<ArticuloPrecioActualDTO> obtenerPrecioActual(
            @PathVariable String codigo,
            @RequestParam Integer listaPrecioId
    ) {
        return ResponseEntity.ok(articuloService.obtenerPrecioActual(codigo, listaPrecioId));
    }

    @GetMapping
    public List<ArticuloDTO> listar() {
        return articuloRepository.findAll().stream().map(a -> new ArticuloDTO(a.getCodigo(), a.getNombre(), a.getUnidadMedida())).toList();
    }
}
