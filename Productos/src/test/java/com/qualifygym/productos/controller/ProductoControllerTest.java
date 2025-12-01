package com.qualifygym.productos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.qualifygym.productos.model.Producto;
import com.qualifygym.productos.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests de integración para ProductoController
 * 
 * Esta clase contiene tests que verifican los endpoints REST del controlador de productos.
 */
@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto productoTest;

    @BeforeEach
    void setUp() {
        productoTest = new Producto();
        productoTest.setId(1L);
        productoTest.setNombre("Proteína Whey");
        productoTest.setDescripcion("Proteína de suero de leche de alta calidad");
        productoTest.setPrecio(29.99);
        productoTest.setCategoria("supplement");
        productoTest.setImagen("https://example.com/image.jpg");
        productoTest.setStock(100);
    }

    @Test
    void getProductos_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        List<Producto> productos = new ArrayList<>();
        productos.add(productoTest);
        
        when(productoService.obtenerTodos()).thenReturn(productos);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/productos")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1L))
               .andExpect(jsonPath("$[0].nombre").value("Proteína Whey"))
               .andExpect(jsonPath("$[0].precio").value(29.99));
        
        verify(productoService, times(1)).obtenerTodos();
    }

    @Test
    void getProductos_conListaVacia_deberiaRetornarStatus204() throws Exception {
        // Arrange
        when(productoService.obtenerTodos()).thenReturn(new ArrayList<>());
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/productos")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(productoService, times(1)).obtenerTodos();
    }

    @Test
    void getProductoPorId_conIdExistente_deberiaRetornarProductoYStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        when(productoService.obtenerPorId(id)).thenReturn(productoTest);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/{id}", id)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.nombre").value("Proteína Whey"));
        
        verify(productoService, times(1)).obtenerPorId(id);
    }

    @Test
    void getProductoPorId_conIdInexistente_deberiaRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        when(productoService.obtenerPorId(id))
            .thenThrow(new RuntimeException("Producto no encontrado ID: " + id));
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/{id}", id)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").exists());
        
        verify(productoService, times(1)).obtenerPorId(id);
    }

    @Test
    void getProductosPorCategoria_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        String categoria = "supplement";
        List<Producto> productos = new ArrayList<>();
        productos.add(productoTest);
        
        when(productoService.obtenerPorCategoria(categoria)).thenReturn(productos);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/categoria/{categoria}", categoria)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].categoria").value("supplement"));
        
        verify(productoService, times(1)).obtenerPorCategoria(categoria);
    }

    @Test
    void buscarProductos_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        String nombre = "Proteína";
        List<Producto> productos = new ArrayList<>();
        productos.add(productoTest);
        
        when(productoService.buscarPorNombre(nombre)).thenReturn(productos);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/buscar")
               .param("nombre", nombre)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].nombre").value("Proteína Whey"));
        
        verify(productoService, times(1)).buscarPorNombre(nombre);
    }

    @Test
    void crearProducto_conDatosValidos_deberiaRetornarStatus201() throws Exception {
        // Arrange
        String requestBody = """
            {
                "nombre": "Nuevo Producto",
                "descripcion": "Descripción del producto",
                "precio": 39.99,
                "categoria": "equipment",
                "imagen": "https://example.com/new.jpg",
                "stock": 50
            }
            """;
        
        Producto nuevoProducto = new Producto();
        nuevoProducto.setId(2L);
        nuevoProducto.setNombre("Nuevo Producto");
        
        when(productoService.crearProducto(anyString(), anyString(), anyDouble(), anyString(), anyString(), anyInt()))
            .thenReturn(nuevoProducto);
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/productos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(2L));
        
        verify(productoService, times(1)).crearProducto(anyString(), anyString(), anyDouble(), anyString(), anyString(), anyInt());
    }

    @Test
    void crearProducto_conCamposFaltantes_deberiaRetornarStatus400() throws Exception {
        // Arrange
        String requestBody = """
            {
                "nombre": "Nuevo Producto",
                "precio": 39.99
            }
            """;
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/productos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error").exists());
        
        verify(productoService, never()).crearProducto(anyString(), anyString(), anyDouble(), anyString(), anyString(), anyInt());
    }

    @Test
    void actualizarProducto_conDatosValidos_deberiaRetornarStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        String requestBody = """
            {
                "nombre": "Producto Actualizado",
                "precio": 34.99
            }
            """;
        
        productoTest.setNombre("Producto Actualizado");
        productoTest.setPrecio(34.99);
        
        when(productoService.actualizarProducto(eq(id), anyString(), isNull(), anyDouble(), isNull(), isNull(), isNull()))
            .thenReturn(productoTest);
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/productos/{id}", id)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre").value("Producto Actualizado"));
        
        verify(productoService, times(1)).actualizarProducto(eq(id), anyString(), isNull(), anyDouble(), isNull(), isNull(), isNull());
    }

    @Test
    void actualizarProducto_conIdInexistente_deberiaRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        String requestBody = """
            {
                "nombre": "Nuevo"
            }
            """;
        
        when(productoService.actualizarProducto(eq(id), anyString(), isNull(), isNull(), isNull(), isNull(), isNull()))
            .thenThrow(new RuntimeException("Producto no encontrado ID: " + id));
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/productos/{id}", id)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").exists());
        
        verify(productoService, times(1)).actualizarProducto(eq(id), anyString(), isNull(), isNull(), isNull(), isNull(), isNull());
    }

    @Test
    void eliminarProducto_conIdValido_deberiaRetornarStatus204() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(productoService).eliminarProducto(id);
        
        // Act & Assert
        mockMvc.perform(delete("/api/v1/productos/{id}", id)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(productoService, times(1)).eliminarProducto(id);
    }

    @Test
    void actualizarStock_conDatosValidos_deberiaRetornarStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        String requestBody = """
            {
                "cantidad": 10
            }
            """;
        
        when(productoService.obtenerPorId(id)).thenReturn(productoTest);
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/productos/{id}/stock", id)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L));
        
        verify(productoService, times(1)).actualizarStock(id, 10);
        verify(productoService, times(1)).obtenerPorId(id);
    }
}

