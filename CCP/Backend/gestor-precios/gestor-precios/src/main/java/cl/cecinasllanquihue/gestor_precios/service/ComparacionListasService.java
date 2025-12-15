package cl.cecinasllanquihue.gestor_precios.service;

import cl.cecinasllanquihue.gestor_precios.dto.ReporteComparacionDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ComparacionListasService {
    ReporteComparacionDTO compararListas(
            MultipartFile archivoListaA,
            MultipartFile archivoListaB,
            Integer sistemaId
    );
}
