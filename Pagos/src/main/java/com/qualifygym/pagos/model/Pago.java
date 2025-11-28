package com.qualifygym.pagos.model;

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
@Table(name = "pagos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un pago en el sistema")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del pago", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "ID de la orden asociada", example = "1")
    private Long ordenId;

    @Column(nullable = false)
    @Schema(description = "ID del usuario que realiza el pago", example = "1")
    private Long usuarioId;

    @Column(nullable = false)
    @Schema(description = "Monto total del pago", example = "99.99")
    private Double monto;

    @Column(nullable = false, length = 50)
    @Schema(description = "Método de pago", example = "tarjeta")
    private String metodoPago;

    @Column(nullable = false, length = 50)
    @Schema(description = "Estado del pago", example = "completado")
    private String estado;

    @Column(name = "fecha_pago")
    @Schema(description = "Fecha y hora del pago")
    private LocalDateTime fechaPago;

    @Column(length = 500)
    @Schema(description = "Información adicional del pago", example = "Transacción aprobada")
    private String informacionAdicional;
}

