package cl.cecinasllanquihue.gestor_precios.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer id;

    @Column(name = "usuario_nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "usuario_contrasena", nullable = false, length = 100)
    private String contrasena;
}
