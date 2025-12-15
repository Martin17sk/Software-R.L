package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.ActualizarTiempoHistorialRequestDTO;
import cl.cecinasllanquihue.gestor_precios.dto.ConfiguracionGeneralDTO;
import cl.cecinasllanquihue.gestor_precios.service.ParametroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion")
public class ConfiguracionController {
    private final ParametroService parametroService;

    public ConfiguracionController(ParametroService parametroService) {
        this.parametroService = parametroService;
    }

    @GetMapping("/general")
    public ResponseEntity<ConfiguracionGeneralDTO> getConfiguracionGeneral() {
        ConfiguracionGeneralDTO dto = new ConfiguracionGeneralDTO();
        dto.setHorasMaxEliminacionHistorial(parametroService.getHorasMaxEliminacionHistorial());
        return  ResponseEntity.ok(dto);
    }

    @PutMapping("/historial/tiempo")
    public ResponseEntity<Void> actualizarTiempoHistorial(
            @RequestBody ActualizarTiempoHistorialRequestDTO request
    ) {
        parametroService.setHorasMaxEliminacionHistorial(request.getHoras());
        return ResponseEntity.noContent().build();
    }
}
