package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.HistorialDTO;
import cl.cecinasllanquihue.gestor_precios.service.HistorialService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<HistorialDTO>> buscar(
            @RequestParam(required = false) String codigoArticulo,
            @RequestParam(required = false) Integer listaPrecioId
    ) {
        var resultados = historialService.buscar(codigoArticulo, listaPrecioId);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/articulo/{codigo}")
    public ResponseEntity<List<HistorialDTO>> obtenerPorArticulo(
            @PathVariable String codigo
    ) {
        return ResponseEntity.ok(historialService.obtenerHistorialPorArticulo(codigo));
    }

    @GetMapping("/articulo/{codigo}/lista/{listaId}")
    public ResponseEntity<Page<HistorialDTO>> obtenerPorArticuloYLista(
            @PathVariable String codigo,
            @PathVariable Integer listaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                historialService.obtenerHistorialPorArticuloYLista(codigo, listaId, pageable)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegistro(@PathVariable Integer id) {
        historialService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<HistorialDTO>> listarHistorial(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "fechaHora") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Sort.Direction direction = sortDir.equals("ASC")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        String sortProperty = mapSortProperty(sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));

        return ResponseEntity.ok(
                historialService.buscarHistorial(search, pageable)
        );
    }

    private String mapSortProperty(String sortBy) {
        return switch (sortBy) {
            case "codigo" -> "articulo.codigo";
            case "nombre" -> "articulo.nombre";
            case "lista" -> "listaPrecio.nombre";
            case "sistema" -> "listaPrecio.sistema.nombre";
            case "usuario" -> "usuario.nombre";
            case "fechaHora" -> "fechaHora";
            default -> "fechaHora";
        };
    }
}
