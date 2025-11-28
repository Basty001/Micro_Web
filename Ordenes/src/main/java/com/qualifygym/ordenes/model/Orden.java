package com.qualifygym.ordenes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa una orden en el sistema")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la orden", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "ID del usuario que realiza la orden", example = "1")
    private Long usuarioId;

    @Column(nullable = false)
    @Schema(description = "Monto total de la orden", example = "99.99")
    private Double total;

    @Column(nullable = false, length = 50)
    @Schema(description = "Estado de la orden", example = "pendiente")
    private String estado;

    @Column(name = "fecha_creacion")
    @Schema(description = "Fecha y hora de creación de la orden")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    @Schema(description = "Fecha y hora de última actualización")
    private LocalDateTime fechaActualizacion;

    @Column(length = 500)
    @Schema(description = "Dirección de envío", example = "Calle Principal 123")
    private String direccionEnvio;

    @Column(length = 1000)
    @Schema(description = "Notas adicionales de la orden")
    private String notas;
}

