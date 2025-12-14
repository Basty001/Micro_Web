package com.qualifygym.carrito.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qualifygym.carrito.model.ItemCarrito;
import com.qualifygym.carrito.service.CarritoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/carrito")
@Tag(name = "Carrito", description = "API para la gestión del carrito de compras")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(summary = "Obtener carrito por usuario", description = "Retorna todos los items del carrito de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemCarrito.class))),
            @ApiResponse(responseCode = "204", description = "Carrito vacío", content = @Content)
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ItemCarrito>> getCarritoPorUsuario(@PathVariable Long usuarioId) {
        List<ItemCarrito> items = carritoService.obtenerCarritoPorUsuario(usuarioId);
        return items.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(items);
    }

    @Operation(summary = "Agregar item al carrito", description = "Agrega un producto al carrito del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item agregado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemCarrito.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping("/agregar")
    public ResponseEntity<?> agregarItem(@RequestBody Map<String, Object> datos) {
        try {
            Long usuarioId = Long.valueOf(datos.get("usuarioId").toString());
            Long productoId = Long.valueOf(datos.get("productoId").toString());
            Integer cantidad = Integer.valueOf(datos.get("cantidad").toString());
            Double precioUnitario = Double.valueOf(datos.get("precioUnitario").toString());
            
            ItemCarrito item = carritoService.agregarItem(usuarioId, productoId, cantidad, precioUnitario);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Formato de número inválido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar cantidad de item", description = "Actualiza la cantidad de un item en el carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemCarrito.class))),
            @ApiResponse(responseCode = "404", description = "Item no encontrado", content = @Content)
    })
    @PutMapping("/item/{itemId}")
    public ResponseEntity<?> actualizarCantidad(@PathVariable Long itemId, @RequestBody Map<String, Object> datos) {
        try {
            Integer cantidad = Integer.valueOf(datos.get("cantidad").toString());
            ItemCarrito item = carritoService.actualizarCantidad(itemId, cantidad);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar item del carrito", description = "Elimina un item específico del carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<?> eliminarItem(@PathVariable Long itemId) {
        try {
            carritoService.eliminarItem(itemId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar item: " + e.getMessage()));
        }
    }

    @Operation(summary = "Vaciar carrito", description = "Elimina todos los items del carrito de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Carrito vaciado exitosamente")
    })
    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> vaciarCarrito(@PathVariable Long usuarioId) {
        try {
            carritoService.vaciarCarrito(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al vaciar carrito: " + e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar item por producto", description = "Elimina un item del carrito por usuario y producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item eliminado exitosamente")
    })
    @DeleteMapping("/usuario/{usuarioId}/producto/{productoId}")
    public ResponseEntity<?> eliminarItemPorProducto(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        try {
            carritoService.eliminarItemPorProducto(usuarioId, productoId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar item: " + e.getMessage()));
        }
    }
}
