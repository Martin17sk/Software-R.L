package cl.cecinasllanquihue.gestor_precios.service.impl;

import cl.cecinasllanquihue.gestor_precios.dto.HistorialDTO;
import cl.cecinasllanquihue.gestor_precios.model.Historial;
import cl.cecinasllanquihue.gestor_precios.repository.HistorialRepository;
import cl.cecinasllanquihue.gestor_precios.service.HistorialService;
import cl.cecinasllanquihue.gestor_precios.service.ParametroService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialServiceImpl implements HistorialService {
    private final HistorialRepository historialRepository;
    private final ParametroService parametroService;

    public HistorialServiceImpl(HistorialRepository historialRepository, ParametroService parametroService) {
        this.historialRepository = historialRepository;
        this.parametroService = parametroService;
    }

    @Override
    public List<HistorialDTO> obtenerHistorialPorArticulo(String articuloCodigo) {
        List<Historial> lista =
                historialRepository.findByArticulo_CodigoOrderByFechaHoraDesc(articuloCodigo);

        return lista.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<HistorialDTO> buscar(String codigoArticulo, Integer listaPrecioId) {
        var pageable = PageRequest.of(0, 200, Sort.by(Sort.Direction.DESC, "fechaHora"));

        Page<Historial> page = historialRepository
                .findByArticulo_CodigoAndListaPrecio_Id(codigoArticulo, listaPrecioId, pageable);

        return page.getContent().stream().map(this::toDTO).toList();
    }

    @Override
    public Page<HistorialDTO> obtenerHistorialPorArticuloYLista(
            String articuloCodigo,
            Integer listaPrecioId,
            Pageable pageable) {

        Page<Historial> page = historialRepository
                .findByArticulo_CodigoAndListaPrecio_Id(articuloCodigo, listaPrecioId, pageable);

        return page.map(this::toDTO);
    }

    @Override
    public Page<HistorialDTO> buscarHistorial(String search, Pageable pageable) {
        Page<Historial> page = historialRepository.buscarConFiltro(search, pageable);
        return page.map(this::toDTO);
    }

    @Override
    public void eliminar(Integer idHistorial) {
        Historial historial = historialRepository.findById(idHistorial)
                .orElseThrow(() -> new EntityNotFoundException("HIST_E001: historial no encontrado"));

        int horas = parametroService.getHorasMaxEliminacionHistorial();
        LocalDateTime limite = historial.getFechaHora().plusHours(horas);

        if (LocalDateTime.now().isAfter(limite)) {
            throw new IllegalStateException("HIST_E002: historial fuera de plazo para eliminación");
        }

        historialRepository.delete(historial);
    }

    private HistorialDTO toDTO(Historial h) {
        HistorialDTO dto = new HistorialDTO();
        dto.setId(h.getId());
        dto.setArticuloCodigo(h.getArticulo().getCodigo());
        dto.setListaPrecioId(h.getListaPrecio().getId());
        dto.setListaPrecioNombre(h.getListaPrecio().getNombre());
        dto.setPrecioAnterior(h.getPrecioAnterior());
        dto.setPrecioNuevo(h.getPrecioNuevo());
        dto.setFechaHora(h.getFechaHora());
        dto.setUsuarioId(h.getUsuario().getId());
        dto.setUsuarioNombre(h.getUsuario().getNombre());
        dto.setObservacion(h.getObservacion());
        return dto;
    }
}
