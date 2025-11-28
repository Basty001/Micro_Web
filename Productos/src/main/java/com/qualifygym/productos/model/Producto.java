package com.qualifygym.productos.model;

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
@Table(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un producto en el sistema")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del producto", example = "1")
    private Long id;

    @Column(nullable = false, length = 200)
    @Schema(description = "Nombre del producto", example = "Proteína Whey")
    private String nombre;

    @Column(nullable = false, length = 1000)
    @Schema(description = "Descripción del producto", example = "Proteína de suero de leche de alta calidad")
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "Precio del producto", example = "29.99")
    private Double precio;

    @Column(nullable = false, length = 50)
    @Schema(description = "Categoría del producto", example = "supplement")
    private String categoria;

    @Column(length = 500)
    @Schema(description = "URL de la imagen del producto", example = "https://example.com/image.jpg")
    private String imagen;

    @Column(nullable = false)
    @Schema(description = "Cantidad disponible en stock", example = "100")
    private Integer stock;
}

