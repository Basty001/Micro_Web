package com.qualifygym.ordenes.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.qualifygym.ordenes.model.ItemOrden;
import com.qualifygym.ordenes.model.Orden;
import com.qualifygym.ordenes.service.OrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests de integración para OrdenController
 * 
 * Esta clase contiene tests que verifican los endpoints REST del controlador de órdenes.
 */
@WebMvcTest(OrdenController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrdenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenService ordenService;

    @Autowired
    private ObjectMapper objectMapper;

    private Orden ordenTest;
    private ItemOrden itemOrdenTest;

    @BeforeEach
    void setUp() {
        ordenTest = new Orden();
        ordenTest.setId(1L);
        ordenTest.setUsuarioId(1L);
        ordenTest.setTotal(99.99);
        ordenTest.setEstado("pendiente");
        ordenTest.setFechaCreacion(LocalDateTime.now());
        ordenTest.setFechaActualizacion(LocalDateTime.now());
        
        itemOrdenTest = new ItemOrden();
        itemOrdenTest.setId(1L);
        itemOrdenTest.setOrden(ordenTest);
        itemOrdenTest.setProductoId(1L);
        itemOrdenTest.setCantidad(2);
        itemOrdenTest.setPrecioUnitario(49.99);
        itemOrdenTest.setSubtotal(99.98);
    }

    @Test
    void getOrdenes_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        List<Orden> ordenes = new ArrayList<>();
        ordenes.add(ordenTest);
        
        when(ordenService.obtenerTodas()).thenReturn(ordenes);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/ordenes")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1L))
               .andExpect(jsonPath("$[0].usuarioId").value(1L))
               .andExpect(jsonPath("$[0].total").value(99.99));
        
        verify(ordenService, times(1)).obtenerTodas();
    }

    @Test
    void getOrdenes_conListaVacia_deberiaRetornarStatus204() throws Exception {
        // Arrange
        when(ordenService.obtenerTodas()).thenReturn(new ArrayList<>());
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/ordenes")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(ordenService, times(1)).obtenerTodas();
    }

    @Test
    void getOrdenPorId_conIdExistente_deberiaRetornarOrdenYStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        when(ordenService.obtenerPorId(id)).thenReturn(ordenTest);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/ordenes/{id}", id)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.usuarioId").value(1L));
        
        verify(ordenService, times(1)).obtenerPorId(id);
    }

    @Test
    void getOrdenPorId_conIdInexistente_deberiaRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        when(ordenService.obtenerPorId(id))
            .thenThrow(new RuntimeException("Orden no encontrada ID: " + id));
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/ordenes/{id}", id)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").exists());
        
        verify(ordenService, times(1)).obtenerPorId(id);
    }

    @Test
    void getOrdenesPorUsuario_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        Long usuarioId = 1L;
        List<Orden> ordenes = new ArrayList<>();
        ordenes.add(ordenTest);
        
        when(ordenService.obtenerPorUsuario(usuarioId)).thenReturn(ordenes);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/ordenes/usuario/{usuarioId}", usuarioId)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1L));
        
        verify(ordenService, times(1)).obtenerPorUsuario(usuarioId);
    }

    @Test
    void getOrdenesPorEstado_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        String estado = "pendiente";
        List<Orden> ordenes = new ArrayList<>();
        ordenes.add(ordenTest);
        
        when(ordenService.obtenerPorEstado(estado)).thenReturn(ordenes);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/ordenes/estado/{estado}", estado)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].estado").value("pendiente"));
        
        verify(ordenService, times(1)).obtenerPorEstado(estado);
    }

    @Test
    void getItemsPorOrden_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        List<ItemOrden> items = new ArrayList<>();
        items.add(itemOrdenTest);
        
        when(ordenService.obtenerItemsPorOrden(id)).thenReturn(items);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/ordenes/{id}/items", id)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1L));
        
        verify(ordenService, times(1)).obtenerItemsPorOrden(id);
    }

    @Test
    void crearOrden_conDatosValidos_deberiaRetornarStatus201() throws Exception {
        // Arrange
        String requestBody = """
            {
                "usuarioId": 1,
                "total": 99.99,
                "direccionEnvio": "Calle Principal 123",
                "notas": "Notas de prueba",
                "items": [
                    {
                        "productoId": 1,
                        "cantidad": 2,
                        "precioUnitario": 49.99
                    }
                ]
            }
            """;
        
        when(ordenService.crearOrden(anyLong(), anyDouble(), anyString(), anyString(), anyList()))
            .thenReturn(ordenTest);
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/ordenes")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1L));
        
        verify(ordenService, times(1)).crearOrden(anyLong(), anyDouble(), anyString(), anyString(), anyList());
    }

    @Test
    void crearOrden_conCamposFaltantes_deberiaRetornarStatus400() throws Exception {
        // Arrange
        String requestBody = """
            {
                "usuarioId": 1,
                "total": 99.99
            }
            """;
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/ordenes")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error").exists());
        
        verify(ordenService, never()).crearOrden(anyLong(), anyDouble(), anyString(), anyString(), anyList());
    }

    @Test
    void actualizarEstado_conDatosValidos_deberiaRetornarStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        String requestBody = """
            {
                "estado": "completado"
            }
            """;
        
        ordenTest.setEstado("completado");
        when(ordenService.actualizarEstado(id, "completado")).thenReturn(ordenTest);
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/ordenes/{id}/estado", id)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.estado").value("completado"));
        
        verify(ordenService, times(1)).actualizarEstado(id, "completado");
    }

    @Test
    void actualizarEstado_conIdInexistente_deberiaRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        String requestBody = """
            {
                "estado": "completado"
            }
            """;
        
        when(ordenService.actualizarEstado(id, "completado"))
            .thenThrow(new RuntimeException("Orden no encontrada ID: " + id));
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/ordenes/{id}/estado", id)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").exists());
        
        verify(ordenService, times(1)).actualizarEstado(id, "completado");
    }

    @Test
    void eliminarOrden_conIdValido_deberiaRetornarStatus204() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(ordenService).eliminarOrden(id);
        
        // Act & Assert
        mockMvc.perform(delete("/api/v1/ordenes/{id}", id)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(ordenService, times(1)).eliminarOrden(id);
    }
}

