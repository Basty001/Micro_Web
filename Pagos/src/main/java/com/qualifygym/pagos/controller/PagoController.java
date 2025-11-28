package com.qualifygym.pagos.controller;

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

import com.qualifygym.pagos.model.Pago;
import com.qualifygym.pagos.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "API para la gestión de pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Operation(summary = "Obtener todos los pagos", description = "Retorna una lista de todos los pagos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay pagos")
    })
    @GetMapping
    public ResponseEntity<List<Pago>> getPagos() {
        List<Pago> pagos = pagoService.obtenerTodos();
        return pagos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pagos);
    }

    @Operation(summary = "Obtener pago por ID", description = "Retorna un pago específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPagoPorId(@PathVariable Long id) {
        try {
            Pago pago = pagoService.obtenerPorId(id);
            return ResponseEntity.ok(pago);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener pagos por usuario", description = "Retorna todos los pagos de un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> getPagosPorUsuario(@PathVariable Long usuarioId) {
        List<Pago> pagos = pagoService.obtenerPorUsuario(usuarioId);
        return pagos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pagos);
    }

    @Operation(summary = "Obtener pagos por orden", description = "Retorna todos los pagos de una orden")
    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<List<Pago>> getPagosPorOrden(@PathVariable Long ordenId) {
        List<Pago> pagos = pagoService.obtenerPorOrden(ordenId);
        return pagos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pagos);
    }

    @Operation(summary = "Obtener pagos por estado", description = "Retorna todos los pagos con un estado específico")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> getPagosPorEstado(@PathVariable String estado) {
        List<Pago> pagos = pagoService.obtenerPorEstado(estado);
        return pagos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pagos);
    }

    @Operation(summary = "Crear pago", description = "Crea un nuevo pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<?> crearPago(@RequestBody Map<String, Object> datos) {
        try {
            Long ordenId = Long.valueOf(datos.get("ordenId").toString());
            Long usuarioId = Long.valueOf(datos.get("usuarioId").toString());
            Double monto = Double.valueOf(datos.get("monto").toString());
            String metodoPago = (String) datos.get("metodoPago");
            String informacionAdicional = datos.get("informacionAdicional") != null ? 
                    (String) datos.get("informacionAdicional") : null;

            if (ordenId == null || usuarioId == null || monto == null || metodoPago == null) {
                return ResponseEntity.badRequest().body("Faltan campos requeridos");
            }

            Pago pago = pagoService.crearPago(ordenId, usuarioId, monto, metodoPago, informacionAdicional);
            return ResponseEntity.status(HttpStatus.CREATED).body(pago);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar estado de pago", description = "Actualiza el estado de un pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        try {
            String estado = (String) datos.get("estado");
            if (estado == null) {
                return ResponseEntity.badRequest().body("El campo 'estado' es requerido");
            }
            Pago pago = pagoService.actualizarEstado(id, estado);
            return ResponseEntity.ok(pago);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar pago", description = "Elimina un pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPago(@PathVariable Long id) {
        try {
            pagoService.eliminarPago(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar pago: " + e.getMessage());
        }
    }
}

