package cl.cecinasllanquihue.gestor_precios.controller;

import cl.cecinasllanquihue.gestor_precios.dto.RegistrarCambioPrecioMultipleRequestDTO;
import cl.cecinasllanquihue.gestor_precios.dto.RegistrarCambioPrecioMultipleResponseDTO;
import cl.cecinasllanquihue.gestor_precios.dto.RegistrarCambioPrecioRequestDTO;
import cl.cecinasllanquihue.gestor_precios.dto.RegistrarCambioPrecioResponseDTO;
import cl.cecinasllanquihue.gestor_precios.service.CambioPrecioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/precios")
public class CambioPrecioController {

    private final CambioPrecioService cambioPrecioService;

    public CambioPrecioController(CambioPrecioService cambioPrecioService) {
        this.cambioPrecioService = cambioPrecioService;
    }

    @PostMapping("/cambio-precio")
    public ResponseEntity<RegistrarCambioPrecioResponseDTO> registrarCambioPrecio(
            @RequestBody RegistrarCambioPrecioRequestDTO request) {
        return ResponseEntity.ok(cambioPrecioService.registrarCambioPrecio(request));
    }

    @PostMapping("/cambio-precio/multiple")
    public ResponseEntity<RegistrarCambioPrecioMultipleResponseDTO> registrarCambioPrecioMultiple(
            @RequestBody RegistrarCambioPrecioMultipleRequestDTO request
    ) {
        return ResponseEntity.ok(cambioPrecioService.registrarCambioPrecioMultiple(request));
    }
}
