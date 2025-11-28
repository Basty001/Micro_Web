package com.qualifygym.ordenes.controller;

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

import com.qualifygym.ordenes.model.ItemOrden;
import com.qualifygym.ordenes.model.Orden;
import com.qualifygym.ordenes.service.OrdenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Ordenes", description = "API para la gestión de órdenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @Operation(summary = "Obtener todas las órdenes", description = "Retorna una lista de todas las órdenes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay órdenes")
    })
    @GetMapping
    public ResponseEntity<List<Orden>> getOrdenes() {
        List<Orden> ordenes = ordenService.obtenerTodas();
        return ordenes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ordenes);
    }

    @Operation(summary = "Obtener orden por ID", description = "Retorna una orden específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden encontrada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrdenPorId(@PathVariable Long id) {
        try {
            Orden orden = ordenService.obtenerPorId(id);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Obtener órdenes por usuario", description = "Retorna todas las órdenes de un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Orden>> getOrdenesPorUsuario(@PathVariable Long usuarioId) {
        List<Orden> ordenes = ordenService.obtenerPorUsuario(usuarioId);
        return ordenes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ordenes);
    }

    @Operation(summary = "Obtener órdenes por estado", description = "Retorna todas las órdenes con un estado específico")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Orden>> getOrdenesPorEstado(@PathVariable String estado) {
        List<Orden> ordenes = ordenService.obtenerPorEstado(estado);
        return ordenes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ordenes);
    }

    @Operation(summary = "Obtener items de una orden", description = "Retorna todos los items de una orden")
    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemOrden>> getItemsPorOrden(@PathVariable Long id) {
        List<ItemOrden> items = ordenService.obtenerItemsPorOrden(id);
        return items.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(items);
    }

    @Operation(summary = "Crear orden", description = "Crea una nueva orden con sus items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<?> crearOrden(@RequestBody Map<String, Object> datos) {
        try {
            Long usuarioId = Long.valueOf(datos.get("usuarioId").toString());
            Double total = Double.valueOf(datos.get("total").toString());
            String direccionEnvio = datos.get("direccionEnvio") != null ? 
                    (String) datos.get("direccionEnvio") : null;
            String notas = datos.get("notas") != null ? (String) datos.get("notas") : null;
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) datos.get("items");

            if (usuarioId == null || total == null || items == null || items.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Faltan campos requeridos"));
            }

            Orden orden = ordenService.crearOrden(usuarioId, total, direccionEnvio, notas, items);
            return ResponseEntity.status(HttpStatus.CREATED).body(orden);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Formato de número inválido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar estado de orden", description = "Actualiza el estado de una orden")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        try {
            String estado = (String) datos.get("estado");
            if (estado == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El campo 'estado' es requerido"));
            }
            Orden orden = ordenService.actualizarEstado(id, estado);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar orden", description = "Elimina una orden y sus items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Orden eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarOrden(@PathVariable Long id) {
        try {
            ordenService.eliminarOrden(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar orden: " + e.getMessage()));
        }
    }
}

