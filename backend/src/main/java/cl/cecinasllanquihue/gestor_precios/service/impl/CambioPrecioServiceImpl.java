package cl.cecinasllanquihue.gestor_precios.service.impl;

import cl.cecinasllanquihue.gestor_precios.dto.RegistrarCambioPrecioMultipleRequestDTO;
import cl.cecinasllanquihue.gestor_precios.dto.RegistrarCambioPrecioMultipleResponseDTO;
import cl.cecinasllanquihue.gestor_precios.dto.RegistrarCambioPrecioRequestDTO;
import cl.cecinasllanquihue.gestor_precios.dto.RegistrarCambioPrecioResponseDTO;
import cl.cecinasllanquihue.gestor_precios.model.*;
import cl.cecinasllanquihue.gestor_precios.repository.*;
import cl.cecinasllanquihue.gestor_precios.security.AuthenticatedUserProvider;
import cl.cecinasllanquihue.gestor_precios.service.CambioPrecioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CambioPrecioServiceImpl implements CambioPrecioService {

    private final ArticuloRepository articuloRepository;
    private final ListaPrecioRepository listaPrecioRepository;
    private final PrecioActualRepository precioActualRepository;
    private final HistorialRepository historialRepository;
    private final AuthenticatedUserProvider authUser;

    public CambioPrecioServiceImpl(ArticuloRepository articuloRepository, ListaPrecioRepository listaPrecioRepository, PrecioActualRepository precioActualRepository, HistorialRepository historialRepository, AuthenticatedUserProvider authUser) {
        this.articuloRepository = articuloRepository;
        this.listaPrecioRepository = listaPrecioRepository;
        this.precioActualRepository = precioActualRepository;
        this.historialRepository = historialRepository;
        this.authUser = authUser;
    }

    @Transactional
    @Override
    public RegistrarCambioPrecioResponseDTO registrarCambioPrecio(RegistrarCambioPrecioRequestDTO request){

        validarRequest(request);

        Articulo articulo = articuloRepository.findById(request.getArticuloCodigo())
                .orElseThrow(() -> new EntityNotFoundException("PRE_E001: Artículo no existe"));

        ListaPrecio listaPrecio = listaPrecioRepository.findById(request.getListaPrecioId())
                .orElseThrow(() -> new EntityNotFoundException("PRE_E002: Lista de precio no existe"));

        Usuario usuario = authUser.getUsuarioActual();

        // Obtener precio actual
        Optional<PrecioActual> precioActualOpt =
                precioActualRepository.findByArticulo_CodigoAndListaPrecio_Id(
                        articulo.getCodigo(),
                        listaPrecio.getId()
                );

        BigDecimal precioAnterior = precioActualOpt
                .map(PrecioActual::getPrecio)
                .orElse(BigDecimal.ZERO);

        BigDecimal precioNuevo = request.getPrecioNuevo();

        if (precioActualOpt.isPresent() && precioAnterior.compareTo(precioNuevo) == 0) {
            throw new IllegalStateException("PRE_E005: Precio nuevo igual al anterior");
        }

        // Registrar en historial
        Historial historial = new Historial();
        historial.setFechaHora(LocalDateTime.now());
        historial.setUsuario(usuario);
        historial.setArticulo(articulo);
        historial.setListaPrecio(listaPrecio);
        historial.setPrecioAnterior(precioAnterior);
        historial.setPrecioNuevo(request.getPrecioNuevo());
        historial.setObservacion(request.getObservacion());

        historial = historialRepository.save(historial);

        //Actualizar precio_actual (upsert)
        PrecioActualId id = new PrecioActualId(articulo.getCodigo(), listaPrecio.getId());

        PrecioActual precioActual = precioActualRepository.findById(id)
                        .orElseGet(() -> {
                            PrecioActual nuevo = new PrecioActual();
                            nuevo.setId(id);
                            return nuevo;
                        });

        precioActual.setArticulo(articulo);
        precioActual.setListaPrecio(listaPrecio);
        precioActual.setPrecio(precioNuevo);

        precioActualRepository.save(precioActual);

        //Respuesta
        RegistrarCambioPrecioResponseDTO response = new RegistrarCambioPrecioResponseDTO();
        response.setRegistroHistorialId(historial.getId());
        response.setArticuloCodigo(articulo.getCodigo());
        response.setListaPrecioId(listaPrecio.getId());
        response.setPrecioAnterior(precioAnterior);
        response.setPrecioNuevo(precioNuevo);
        response.setFechaHora(historial.getFechaHora());
        response.setUsuarioId(usuario.getId());
        response.setObservacion(historial.getObservacion());

        return response;
    }

    @Transactional
    @Override
    public RegistrarCambioPrecioMultipleResponseDTO registrarCambioPrecioMultiple(
            RegistrarCambioPrecioMultipleRequestDTO request
    ) {
        if (request.getArticuloCodigo() == null || request.getArticuloCodigo().isBlank()) {
            throw new IllegalArgumentException("PRE_MULTI_E001: articuloCodigo es obligatorio");
        }

        if (request.getCambios() == null || request.getCambios().isEmpty()) {
            throw new IllegalArgumentException("PRE_MULTI_E002: se requiere al menos un cambio");
        }

        RegistrarCambioPrecioMultipleResponseDTO resultado = new RegistrarCambioPrecioMultipleResponseDTO();
        resultado.setArticuloCodigo(request.getArticuloCodigo());

        List<RegistrarCambioPrecioResponseDTO> respuestas = new java.util.ArrayList<>();

        for (var cambio : request.getCambios()) {
            if (cambio.getListaPrecioId() == null) {
                throw new IllegalArgumentException("PRE_MULTI_E003: listaPrecioId es obligatorio");
            }
            if (cambio.getPrecioNuevo() == null || cambio.getPrecioNuevo().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("PRE_MULTI_E004: precioNuevo debe ser mayor a 0");
            }
            if (cambio.getObservacion() != null && cambio.getObservacion().length() > 255) {
                throw new IllegalArgumentException("PRE_MULTI_E005: observación excede 255 caracteres");
            }

            RegistrarCambioPrecioRequestDTO single = new RegistrarCambioPrecioRequestDTO();
            single.setArticuloCodigo(request.getArticuloCodigo());
            single.setListaPrecioId(cambio.getListaPrecioId());
            single.setPrecioNuevo(cambio.getPrecioNuevo());
            single.setObservacion(cambio.getObservacion());

            RegistrarCambioPrecioResponseDTO resp = registrarCambioPrecio(single);
            respuestas.add(resp);
        }

        resultado.setCambios(respuestas);
        return resultado;
    }


    private void validarRequest(RegistrarCambioPrecioRequestDTO request) {

        if (request.getArticuloCodigo() == null || request.getArticuloCodigo().isBlank()) {
            throw new IllegalArgumentException("PRE_E003: código de artículo obligatorio");
        }

        if (request.getListaPrecioId() == null) {
            throw new IllegalArgumentException("PRE_E003: listaPrecioId obligatorio");
        }

        if (request.getPrecioNuevo() == null || request.getPrecioNuevo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("PRE_E003: precioNuevo debe ser mayor a 0");
        }

        if (request.getObservacion() != null && request.getObservacion().length() > 255) {
            throw new IllegalArgumentException("PRE_E004: observación excede 255 caracteres");
        }
    }
}
