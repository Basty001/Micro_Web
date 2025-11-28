package com.qualifygym.ordenes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items_orden")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un item en una orden")
public class ItemOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del item", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_id", nullable = false)
    @Schema(description = "Orden a la que pertenece el item")
    private Orden orden;

    @Column(nullable = false)
    @Schema(description = "ID del producto", example = "1")
    private Long productoId;

    @Column(nullable = false)
    @Schema(description = "Cantidad del producto", example = "2")
    private Integer cantidad;

    @Column(nullable = false)
    @Schema(description = "Precio unitario del producto", example = "29.99")
    private Double precioUnitario;

    @Column(nullable = false)
    @Schema(description = "Subtotal del item", example = "59.98")
    private Double subtotal;
}

