package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.ReporteComparacionDTO;
import cl.cecinasllanquihue.gestor_precios.service.ComparacionListasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/comparacion")
public class ComparacionListasController {

    private final ComparacionListasService comparacionListasService;

    public ComparacionListasController(ComparacionListasService comparacionListasService) {
        this.comparacionListasService = comparacionListasService;
    }

    @PostMapping("/listas")
    public ResponseEntity<ReporteComparacionDTO> compararListas(
            @RequestPart("listaA") MultipartFile listaA,
            @RequestPart("listaB") MultipartFile listaB,
            @RequestParam Integer sistemaId
    ) {
        ReporteComparacionDTO reporte = comparacionListasService
                .compararListas(listaA, listaB, sistemaId);
        return ResponseEntity.ok(reporte);
    }
}
