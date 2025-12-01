package com.qualifygym.carrito.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.qualifygym.carrito.model.ItemCarrito;
import com.qualifygym.carrito.service.CarritoService;
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
 * Tests de integración para CarritoController
 * 
 * Esta clase contiene tests que verifican los endpoints REST del controlador de carrito.
 */
@WebMvcTest(CarritoController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarritoService carritoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemCarrito itemCarritoTest;

    @BeforeEach
    void setUp() {
        itemCarritoTest = new ItemCarrito();
        itemCarritoTest.setId(1L);
        itemCarritoTest.setUsuarioId(1L);
        itemCarritoTest.setProductoId(1L);
        itemCarritoTest.setCantidad(2);
        itemCarritoTest.setPrecioUnitario(29.99);
    }

    @Test
    void getCarritoPorUsuario_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        Long usuarioId = 1L;
        List<ItemCarrito> items = new ArrayList<>();
        items.add(itemCarritoTest);
        
        when(carritoService.obtenerCarritoPorUsuario(usuarioId)).thenReturn(items);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/carrito/usuario/{usuarioId}", usuarioId)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1L))
               .andExpect(jsonPath("$[0].productoId").value(1L))
               .andExpect(jsonPath("$[0].cantidad").value(2));
        
        verify(carritoService, times(1)).obtenerCarritoPorUsuario(usuarioId);
    }

    @Test
    void getCarritoPorUsuario_conCarritoVacio_deberiaRetornarStatus204() throws Exception {
        // Arrange
        Long usuarioId = 1L;
        when(carritoService.obtenerCarritoPorUsuario(usuarioId)).thenReturn(new ArrayList<>());
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/carrito/usuario/{usuarioId}", usuarioId)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(carritoService, times(1)).obtenerCarritoPorUsuario(usuarioId);
    }

    @Test
    void agregarItem_conDatosValidos_deberiaRetornarStatus201() throws Exception {
        // Arrange
        String requestBody = """
            {
                "usuarioId": 1,
                "productoId": 2,
                "cantidad": 1,
                "precioUnitario": 39.99
            }
            """;
        
        ItemCarrito nuevoItem = new ItemCarrito();
        nuevoItem.setId(2L);
        nuevoItem.setUsuarioId(1L);
        nuevoItem.setProductoId(2L);
        nuevoItem.setCantidad(1);
        nuevoItem.setPrecioUnitario(39.99);
        
        when(carritoService.agregarItem(anyLong(), anyLong(), anyInt(), anyDouble()))
            .thenReturn(nuevoItem);
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/carrito/agregar")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(2L))
               .andExpect(jsonPath("$.productoId").value(2L));
        
        verify(carritoService, times(1)).agregarItem(anyLong(), anyLong(), anyInt(), anyDouble());
    }

    @Test
    void agregarItem_conCamposFaltantes_deberiaRetornarStatus400() throws Exception {
        // Arrange
        String requestBody = """
            {
                "usuarioId": 1,
                "productoId": 2
            }
            """;
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/carrito/agregar")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error").exists());
        
        verify(carritoService, never()).agregarItem(anyLong(), anyLong(), anyInt(), anyDouble());
    }

    @Test
    void actualizarCantidad_conDatosValidos_deberiaRetornarStatus200() throws Exception {
        // Arrange
        Long itemId = 1L;
        String requestBody = """
            {
                "cantidad": 5
            }
            """;
        
        itemCarritoTest.setCantidad(5);
        when(carritoService.actualizarCantidad(itemId, 5)).thenReturn(itemCarritoTest);
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/carrito/item/{itemId}", itemId)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.cantidad").value(5));
        
        verify(carritoService, times(1)).actualizarCantidad(itemId, 5);
    }

    @Test
    void actualizarCantidad_conIdInexistente_deberiaRetornarStatus404() throws Exception {
        // Arrange
        Long itemId = 999L;
        String requestBody = """
            {
                "cantidad": 5
            }
            """;
        
        when(carritoService.actualizarCantidad(itemId, 5))
            .thenThrow(new RuntimeException("Item no encontrado ID: " + itemId));
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/carrito/item/{itemId}", itemId)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").exists());
        
        verify(carritoService, times(1)).actualizarCantidad(itemId, 5);
    }

    @Test
    void eliminarItem_conIdValido_deberiaRetornarStatus204() throws Exception {
        // Arrange
        Long itemId = 1L;
        doNothing().when(carritoService).eliminarItem(itemId);
        
        // Act & Assert
        mockMvc.perform(delete("/api/v1/carrito/item/{itemId}", itemId)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(carritoService, times(1)).eliminarItem(itemId);
    }

    @Test
    void vaciarCarrito_conUsuarioIdValido_deberiaRetornarStatus204() throws Exception {
        // Arrange
        Long usuarioId = 1L;
        doNothing().when(carritoService).vaciarCarrito(usuarioId);
        
        // Act & Assert
        mockMvc.perform(delete("/api/v1/carrito/usuario/{usuarioId}", usuarioId)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(carritoService, times(1)).vaciarCarrito(usuarioId);
    }

    @Test
    void eliminarItemPorProducto_conDatosValidos_deberiaRetornarStatus204() throws Exception {
        // Arrange
        Long usuarioId = 1L;
        Long productoId = 1L;
        doNothing().when(carritoService).eliminarItemPorProducto(usuarioId, productoId);
        
        // Act & Assert
        mockMvc.perform(delete("/api/v1/carrito/usuario/{usuarioId}/producto/{productoId}", usuarioId, productoId)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(carritoService, times(1)).eliminarItemPorProducto(usuarioId, productoId);
    }
}

