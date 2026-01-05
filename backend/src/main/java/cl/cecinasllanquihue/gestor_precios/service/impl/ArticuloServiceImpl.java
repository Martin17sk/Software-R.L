package cl.cecinasllanquihue.gestor_precios.service.impl;

import cl.cecinasllanquihue.gestor_precios.dto.ArticuloConPrecioActualDTO;
import cl.cecinasllanquihue.gestor_precios.dto.ArticuloOptionDTO;
import cl.cecinasllanquihue.gestor_precios.dto.ArticuloPrecioActualDTO;
import cl.cecinasllanquihue.gestor_precios.dto.ArticuloUpdateDTO;
import cl.cecinasllanquihue.gestor_precios.model.Articulo;
import cl.cecinasllanquihue.gestor_precios.model.PrecioActual;
import cl.cecinasllanquihue.gestor_precios.model.PrecioActualId;
import cl.cecinasllanquihue.gestor_precios.repository.ArticuloRepository;
import cl.cecinasllanquihue.gestor_precios.repository.ListaPrecioRepository;
import cl.cecinasllanquihue.gestor_precios.repository.PrecioActualRepository;
import cl.cecinasllanquihue.gestor_precios.service.ArticuloService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final ListaPrecioRepository  listaPrecioRepository;
    private final PrecioActualRepository precioActualRepository;

    public ArticuloServiceImpl(ArticuloRepository articuloRepository,  ListaPrecioRepository listaPrecioRepository,  PrecioActualRepository precioActualRepository) {
        this.articuloRepository = articuloRepository;
        this.listaPrecioRepository = listaPrecioRepository;
        this.precioActualRepository = precioActualRepository;
    }

    @Override
    public List<ArticuloConPrecioActualDTO> buscarPorNombre(String query, Integer listaPrecioId) {

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("ART_E001: parámetro 'q' obligatorio");
        }

        if (listaPrecioId == null) {
            throw new IllegalArgumentException("ART_E002: listaPrecioId obligatorio");
        }

        return articuloRepository.buscarConPrecioPorNombre(query, listaPrecioId);
    }

    @Override
    public ArticuloConPrecioActualDTO obtenerPorCodigoYLista(String codigo, Integer listaPrecioId) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("ART_E003: código de artículo obligatorio");
        }

        if (listaPrecioId == null) {
            throw new IllegalArgumentException("ART_E004: listaPrecioId obligatorio");
        }

        return articuloRepository.buscarConPrecioPorCodigo(codigo, listaPrecioId)
                .orElseThrow(() -> new EntityNotFoundException("ART_E005: artículo no encontrado en esa lista"));
    }

    @Override
    public Articulo crear(Articulo articulo) {
        if (articulo.getCodigo() == null || articulo.getCodigo().isBlank()) {
            throw new IllegalArgumentException("ART_E010: código obligatorio");
        }
        if (articulo.getNombre() == null || articulo.getNombre().isBlank()) {
            throw new IllegalArgumentException("ART_E011: nombre obligatorio");
        }
        if (articulo.getUnidadMedida() == null || articulo.getUnidadMedida().isBlank()) {
            throw new IllegalArgumentException("ART_E012: unidad de medida obligatoria");
        }

        if (articuloRepository.existsById(articulo.getCodigo())) {
            throw new IllegalStateException("ART_E013: ya existe un artículo con ese código");
        }

        return articuloRepository.save(articulo);
    }

    @Override
    public Articulo actualizar(String codigo, Articulo datos) {
        Articulo existente = articuloRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("ART_E005: artículo no encontrado"));

        if (datos.getNombre() != null && !datos.getNombre().isBlank()) {
            existente.setNombre(datos.getNombre());
        }
        if (datos.getUnidadMedida() != null && !datos.getUnidadMedida().isBlank()) {
            existente.setUnidadMedida(datos.getUnidadMedida());
        }

        return articuloRepository.save(existente);
    }

    @Override
    public List<ArticuloOptionDTO> listarOptions() {
        return articuloRepository.findAllOptions();
    }

    @Override
    public Articulo obtenerPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("ART_E003: código de artículo obligatorio");
        }

        return articuloRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("ART_E005: artículo no encontrado"));
    }

    @Override
    public ArticuloPrecioActualDTO obtenerPrecioActual(String codigo, Integer listaPrecioId) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("ART_E003: código de artículo obligatorio");
        }
        if (listaPrecioId == null) {
            throw new IllegalArgumentException("ART_E004: listaPrecioId obligatorio");
        }
        if (!listaPrecioRepository.existsById(listaPrecioId)) {
            throw new IllegalStateException("ART_E006: lista de precio inválida");
        }

        Articulo articulo = articuloRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("ART_E005: artículo no encontrado"));

        var precioOpt = precioActualRepository.findById(new PrecioActualId(codigo, listaPrecioId));

        ArticuloPrecioActualDTO dto = new ArticuloPrecioActualDTO();
        dto.setCodigo(articulo.getCodigo());
        dto.setNombre(articulo.getNombre());
        dto.setListaPrecioId(listaPrecioId);
        dto.setPrecioActual(precioOpt.map(PrecioActual::getPrecio).orElse(null));
        return dto;
    }

    @Override
    public Articulo actualizarBasico(String codigo, ArticuloUpdateDTO body) {
        Articulo a = articuloRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado: " + codigo));

        a.setNombre(body.getNombre().trim());
        a.setUnidadMedida(body.getUnidadMedida().trim());

        return articuloRepository.save(a);
    }
}
