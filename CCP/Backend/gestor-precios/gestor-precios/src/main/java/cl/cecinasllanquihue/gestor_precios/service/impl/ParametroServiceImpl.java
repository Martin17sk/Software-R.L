package cl.cecinasllanquihue.gestor_precios.service.impl;

import cl.cecinasllanquihue.gestor_precios.model.Parametro;
import cl.cecinasllanquihue.gestor_precios.repository.ParametroRepository;
import cl.cecinasllanquihue.gestor_precios.service.ParametroService;
import org.springframework.stereotype.Service;

@Service
public class ParametroServiceImpl implements ParametroService {

    private final ParametroRepository parametroRepository;

    public ParametroServiceImpl(ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    @Override
    public int getHorasMaxEliminacionHistorial() {
        return parametroRepository.findById("HISTORIAL_HORAS_ELIMINACION")
                .map(Parametro::getValor)
                .map(Integer::parseInt).orElse(24); //default
    }

    @Override
    public void setHorasMaxEliminacionHistorial(int horas) {
        if (horas <= 0) {
            throw new IllegalArgumentException("CONFIG_E001: horas debe ser mayor a 0");
        }

        Parametro parametro = parametroRepository.findById("HISTORIAL_HORAS_ELIMINACION")
                .orElseGet(() -> {
                    Parametro p = new Parametro();
                    p.setClave("HISTORIAL_HORAS_ELIMINACION");
                    return p;
                });

        parametro.setValor(Integer.toString(horas));
        parametroRepository.save(parametro);
    }
}
