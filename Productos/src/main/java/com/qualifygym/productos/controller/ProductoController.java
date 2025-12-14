package com.qualifygym.productos.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qualifygym.productos.model.Producto;
import com.qualifygym.productos.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para la gestión de productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "204", description = "No hay productos", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Producto>> getProductos() {
        List<Producto> productos = productoService.obtenerTodos();
        return productos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductoPorId(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerPorId(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Buscar productos por categoría", description = "Retorna productos filtrados por categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "204", description = "No hay productos en esta categoría", content = @Content)
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> getProductosPorCategoria(@PathVariable String categoria) {
        List<Producto> productos = productoService.obtenerPorCategoria(categoria);
        return productos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productos);
    }

    @Operation(summary = "Buscar productos por nombre", description = "Retorna productos que coincidan con el nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "204", description = "No hay productos con ese nombre", content = @Content)
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        return productos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productos);
    }

    @Operation(summary = "Crear producto", description = "Crea un nuevo producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> crearProducto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para crear producto", required = true, content = @Content(mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(name = "EjemploProducto", value = "{\"nombre\": \"Proteína Whey 2kg\", \"descripcion\": \"Proteína de suero sabor chocolate\", \"precio\": 89.99, \"categoria\": \"supplement\", \"stock\": 50, \"imagen\": \"https://example.com/whey.jpg\"}"))) @RequestBody Map<String, Object> datos) {
        try {
            String nombre = (String) datos.get("nombre");
            String descripcion = (String) datos.get("descripcion");
            Double precio = Double.valueOf(datos.get("precio").toString());
            String categoria = (String) datos.get("categoria");
            String imagen = datos.get("imagen") != null ? (String) datos.get("imagen") : null;
            Integer stock = Integer.valueOf(datos.get("stock").toString());

            if (nombre == null || descripcion == null || precio == null || categoria == null || stock == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Faltan campos requeridos"));
            }

            Producto producto = productoService.crearProducto(nombre, descripcion, precio, categoria, imagen, stock);
            return ResponseEntity.status(HttpStatus.CREATED).body(producto);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Formato de número inválido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos a actualizar del producto", required = true, content = @Content(mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(name = "EjemploActualizacion", value = "{\"precio\": 95.00, \"stock\": 45}"))) @RequestBody Map<String, Object> datos) {
        try {
            String nombre = datos.get("nombre") != null ? (String) datos.get("nombre") : null;
            String descripcion = datos.get("descripcion") != null ? (String) datos.get("descripcion") : null;
            Double precio = datos.get("precio") != null ? Double.valueOf(datos.get("precio").toString()) : null;
            String categoria = datos.get("categoria") != null ? (String) datos.get("categoria") : null;
            String imagen = datos.get("imagen") != null ? (String) datos.get("imagen") : null;
            Integer stock = datos.get("stock") != null ? Integer.valueOf(datos.get("stock").toString()) : null;

            Producto producto = productoService.actualizarProducto(id, nombre, descripcion, precio, categoria, imagen,
                    stock);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar producto: " + e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar stock", description = "Actualiza el stock de un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PutMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cantidad de stock a sumar/restar o establecer", required = true, content = @Content(mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(name = "EjemploStock", value = "{\"cantidad\": 100}"))) @RequestBody Map<String, Object> datos) {
        try {
            Integer cantidad = Integer.valueOf(datos.get("cantidad").toString());
            productoService.actualizarStock(id, cantidad);
            Producto producto = productoService.obtenerPorId(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
