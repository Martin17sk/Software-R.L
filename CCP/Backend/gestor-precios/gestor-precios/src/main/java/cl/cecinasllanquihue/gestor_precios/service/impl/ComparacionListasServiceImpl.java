package cl.cecinasllanquihue.gestor_precios.service.impl;

import cl.cecinasllanquihue.gestor_precios.dto.ComparacionItemDTO;
import cl.cecinasllanquihue.gestor_precios.dto.ReporteComparacionDTO;
import cl.cecinasllanquihue.gestor_precios.service.ComparacionListasService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ComparacionListasServiceImpl implements ComparacionListasService {

    @Override
    public ReporteComparacionDTO compararListas(MultipartFile archivoListaA,
                                                MultipartFile archivoListaB,
                                                Integer sistemaId) {

        //1. Leer ambas listas desde excel
        ResultadoLectura lecturaA = leerArchivoExcel(archivoListaA);    // Archivo 1: Referencia
        ResultadoLectura lecturaB = leerArchivoExcel(archivoListaB);    // Archivo 2: Sistema

        Map<String, RegistroLista> mapaA = lecturaA.registrosPorCodigo();
        Map<String, RegistroLista> mapaB = lecturaB.registrosPorCodigo();

        //2.

        // Códigos que están en Comercial, pero NO en Sistema → esto SÍ es discrepancia
        Set<String> soloEnA = new HashSet<>(mapaA.keySet());
        soloEnA.removeAll(mapaB.keySet());

        // Códigos que están en ambos → SOLO estos se comparan
        Set<String> enAmbas = new HashSet<>(mapaA.keySet());
        enAmbas.retainAll(mapaB.keySet());

        List<ComparacionItemDTO> discrepancias = new ArrayList<>();
        int erroresEstructura = lecturaA.erroresEstructura() +  lecturaB.erroresEstructura();
        int totalSinDiscrepancia = 0;

        // 3. Artículos que están en Comercial pero NO en Sistema (soloEnA)
        for (String codigo : soloEnA) {

            RegistroLista regA = mapaA.get(codigo);

            if (regA == null) {
                continue;
            }
            discrepancias.add(crearNoEnB(codigo, regA));
        }

        // 4. SOLO los códigos que están en ambos archivos (enAmbas)
        for (String codigo : enAmbas) {

            RegistroLista regA = mapaA.get(codigo); // Comercial
            RegistroLista regB = mapaB.get(codigo); // Sistema

            if (regA == null || regB == null) {
                // No debería pasar por cómo se armó enAmbas, pero por seguridad se ignora
                continue;
            }

            // 4.1. Error de estructura: precio vacío en cualquiera
            if (regA.precio() == null || regB.precio() == null) {

                erroresEstructura++;

                discrepancias.add(crearStructError(codigo, regA, regB));
                continue;
            }

            // 4.2. Precios distintos → discrepancia
            if (regA.precio().compareTo(regB.precio()) != 0) {
                discrepancias.add(crearDiferencia(codigo, regA, regB));
                continue;
            }

            // 4.3. Artículo procesado correctamente, sin discrepancias
            totalSinDiscrepancia++;
        }

        // 5. Armar reporte
        ReporteComparacionDTO reporte = new ReporteComparacionDTO();
        // Procesados = todos los artículos de la lista Comercial (referencia)
        reporte.setTotalProcesados(mapaA.size());
        reporte.setTotalConDiscrepancia(discrepancias.size());
        reporte.setTotalSinDiscrepancia(totalSinDiscrepancia);
        reporte.setTotalErroresEstructura(erroresEstructura);
        reporte.setDiscrepancias(discrepancias);

        return reporte;
    }

    // ─────────────────────────────────────────────────────────────
    // Helpers para construir items de discrepancia
    // ─────────────────────────────────────────────────────────────

    // Artículo que está en Comercial pero NO en Sistema.
    private ComparacionItemDTO crearNoEnB(String codigo, RegistroLista regA) {
        ComparacionItemDTO item = new ComparacionItemDTO();
        item.setCodigoArticulo(codigo);
        item.setNombreArticulo(regA.nombre());

        // Lista A = Comercial
        item.setListaAId(regA.lista());
        item.setListaANombre("Lista A");
        item.setPrecioA(regA.precio());

        // Lista B = Sistema (no existe el artículo)
        item.setListaBId(null);
        item.setListaBNombre("Lista B");
        item.setPrecioB(null);

        item.setDiferencia(null);
        item.setTipoDiscrepancia("NO_EN_B");

        return item;
    }

    //Error de estructura: precio vacío o inválido en alguno de los archivos.
    private ComparacionItemDTO crearStructError(String codigo, RegistroLista regA, RegistroLista regB) {
        ComparacionItemDTO item = new ComparacionItemDTO();
        item.setCodigoArticulo(codigo);
        item.setNombreArticulo(
                regA.nombre() != null ? regA.nombre() : regB.nombre()
        );

        item.setListaAId(regA.lista());
        item.setListaANombre("Lista A");
        item.setPrecioA(regA.precio());

        item.setListaBId(regB.lista());
        item.setListaBNombre("Lista B");
        item.setPrecioB(regB.precio());

        item.setDiferencia(null);
        item.setTipoDiscrepancia("STRUCT_ERROR");

        return item;
    }

    //Discrepancia de precio entre Comercial y Sistema.
    private ComparacionItemDTO crearDiferencia(String codigo, RegistroLista regA, RegistroLista regB) {
        ComparacionItemDTO item = new ComparacionItemDTO();
        item.setCodigoArticulo(codigo);
        item.setNombreArticulo(
                regA.nombre() != null ? regA.nombre() : regB.nombre()
        );

        item.setListaAId(regA.lista());
        item.setListaANombre("Lista A");
        item.setPrecioA(regA.precio());

        item.setListaBId(regB.lista());
        item.setListaBNombre("Lista B");
        item.setPrecioB(regB.precio());

        // diferencia = B (Sistema) - A (Comercial)
        item.setDiferencia(regB.precio().subtract(regA.precio()));
        item.setTipoDiscrepancia("PRECIO_DISTINTO");

        return item;
    }

    // ─────────────────────────────────────────────────────────────
    // Clases internas de apoyo
    // ─────────────────────────────────────────────────────────────

    //Resultado de leer un Excel: mapa por código + contador de errores.
    private record ResultadoLectura(
            Map<String, RegistroLista> registrosPorCodigo,
            int erroresEstructura
    ) {}
    //Registro simple de una fila de la lista.
    private record RegistroLista(
            String codigo,
            String nombre,
            int lista,
            BigDecimal precio
    ) {}

    // ─────────────────────────────────────────────────────────────
    // Lectura de Excel
    // ─────────────────────────────────────────────────────────────

    private ResultadoLectura leerArchivoExcel(MultipartFile archivo) {

        Map<String, RegistroLista> mapa = new HashMap<>();
        int errores = 0;

        try (InputStream is = archivo.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            boolean esPrimeraFila = true;
            for (Row row : sheet) {

                //saltar cabecera
                if (esPrimeraFila) {
                    esPrimeraFila = false;
                    continue;
                }

                if (row == null) {
                    continue;
                }

                //Columna 0: código
                Cell codigoCell = row.getCell(0);
                String codigo = formatter.formatCellValue(codigoCell).trim();

                if(codigo.isEmpty()){
                    continue;
                }

                //Columna 1: nombre
                Cell nombreCell = row.getCell(1);
                String nombre = formatter.formatCellValue(nombreCell).trim();

                //Columna 2: lista
                Cell listaCell = row.getCell(2);
                int lista = Integer.parseInt(formatter.formatCellValue(listaCell));

                //Columna 3: precio
                Cell precioCell = row.getCell(3);
                BigDecimal precio = null;

                if (precioCell != null && precioCell.getCellType() != CellType.BLANK) {
                    try{
                        if(precioCell.getCellType() == CellType.NUMERIC){
                            precio = BigDecimal.valueOf(precioCell.getNumericCellValue());
                        } else {
                            String valor = formatter.formatCellValue(precioCell)
                                    .replace(".", "")
                                    .replace(",", ".")
                                    .trim();

                            if(!valor.isEmpty()){
                                precio = new BigDecimal(valor);
                            }
                        }
                    } catch (NumberFormatException ex) {
                        errores++;
                    }
                }

                RegistroLista registro = new RegistroLista(codigo, nombre, lista, precio);
                mapa.put(codigo, registro);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo Excel: " + archivo.getOriginalFilename(), e);
        }

        return new ResultadoLectura(mapa, errores);
    }
}
