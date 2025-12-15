package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "parametro")
@Data
public class Parametro {

    @Id
    @Column(name = "param_clave", length = 50)
    private String clave;

    @Column(name = "param_valor", length = 255, nullable = false)
    private String valor;
}
