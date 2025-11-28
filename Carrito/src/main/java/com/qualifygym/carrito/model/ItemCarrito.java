package com.qualifygym.carrito.model;

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

@Entity
@Table(name = "items_carrito")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un item en el carrito de compras")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del item", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "ID del usuario propietario del carrito", example = "1")
    private Long usuarioId;

    @Column(nullable = false)
    @Schema(description = "ID del producto", example = "1")
    private Long productoId;

    @Column(nullable = false)
    @Schema(description = "Cantidad del producto", example = "2")
    private Integer cantidad;

    @Column(nullable = false)
    @Schema(description = "Precio unitario del producto al momento de agregarlo", example = "29.99")
    private Double precioUnitario;
}
