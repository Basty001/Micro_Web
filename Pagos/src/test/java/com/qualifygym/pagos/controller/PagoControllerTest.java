package com.qualifygym.pagos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.qualifygym.pagos.model.Pago;
import com.qualifygym.pagos.service.PagoService;
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

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests de integración para PagoController
 * 
 * Esta clase contiene tests que verifican los endpoints REST del controlador de pagos.
 */
@WebMvcTest(PagoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pago pagoTest;

    @BeforeEach
    void setUp() {
        pagoTest = new Pago();
        pagoTest.setId(1L);
        pagoTest.setOrdenId(1L);
        pagoTest.setUsuarioId(1L);
        pagoTest.setMonto(99.99);
        pagoTest.setMetodoPago("tarjeta");
        pagoTest.setEstado("pendiente");
        pagoTest.setFechaPago(LocalDateTime.now());
    }

    @Test
    void getPagos_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        List<Pago> pagos = new ArrayList<>();
        pagos.add(pagoTest);
        
        when(pagoService.obtenerTodos()).thenReturn(pagos);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/pagos")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1L))
               .andExpect(jsonPath("$[0].monto").value(99.99));
        
        verify(pagoService, times(1)).obtenerTodos();
    }

    @Test
    void getPagos_conListaVacia_deberiaRetornarStatus204() throws Exception {
        // Arrange
        when(pagoService.obtenerTodos()).thenReturn(new ArrayList<>());
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/pagos")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(pagoService, times(1)).obtenerTodos();
    }

    @Test
    void getPagoPorId_conIdExistente_deberiaRetornarPagoYStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        when(pagoService.obtenerPorId(id)).thenReturn(pagoTest);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/pagos/{id}", id)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.monto").value(99.99));
        
        verify(pagoService, times(1)).obtenerPorId(id);
    }

    @Test
    void getPagoPorId_conIdInexistente_deberiaRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        when(pagoService.obtenerPorId(id))
            .thenThrow(new RuntimeException("Pago no encontrado ID: " + id));
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/pagos/{id}", id)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").exists());
        
        verify(pagoService, times(1)).obtenerPorId(id);
    }

    @Test
    void getPagosPorUsuario_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        Long usuarioId = 1L;
        List<Pago> pagos = new ArrayList<>();
        pagos.add(pagoTest);
        
        when(pagoService.obtenerPorUsuario(usuarioId)).thenReturn(pagos);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/pagos/usuario/{usuarioId}", usuarioId)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1L));
        
        verify(pagoService, times(1)).obtenerPorUsuario(usuarioId);
    }

    @Test
    void getPagosPorOrden_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        Long ordenId = 1L;
        List<Pago> pagos = new ArrayList<>();
        pagos.add(pagoTest);
        
        when(pagoService.obtenerPorOrden(ordenId)).thenReturn(pagos);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/pagos/orden/{ordenId}", ordenId)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].ordenId").value(1L));
        
        verify(pagoService, times(1)).obtenerPorOrden(ordenId);
    }

    @Test
    void getPagosPorEstado_deberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        String estado = "pendiente";
        List<Pago> pagos = new ArrayList<>();
        pagos.add(pagoTest);
        
        when(pagoService.obtenerPorEstado(estado)).thenReturn(pagos);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/pagos/estado/{estado}", estado)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].estado").value("pendiente"));
        
        verify(pagoService, times(1)).obtenerPorEstado(estado);
    }

    @Test
    void crearPago_conDatosValidos_deberiaRetornarStatus201() throws Exception {
        // Arrange
        String requestBody = """
            {
                "ordenId": 1,
                "usuarioId": 1,
                "monto": 99.99,
                "metodoPago": "tarjeta",
                "informacionAdicional": "Transacción aprobada"
            }
            """;
        
        when(pagoService.crearPago(anyLong(), anyLong(), anyDouble(), anyString(), anyString()))
            .thenReturn(pagoTest);
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/pagos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1L));
        
        verify(pagoService, times(1)).crearPago(anyLong(), anyLong(), anyDouble(), anyString(), anyString());
    }

    @Test
    void crearPago_conCamposFaltantes_deberiaRetornarStatus400() throws Exception {
        // Arrange
        String requestBody = """
            {
                "ordenId": 1,
                "monto": 99.99
            }
            """;
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/pagos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error").exists());
        
        verify(pagoService, never()).crearPago(anyLong(), anyLong(), anyDouble(), anyString(), anyString());
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
        
        pagoTest.setEstado("completado");
        when(pagoService.actualizarEstado(id, "completado")).thenReturn(pagoTest);
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/pagos/{id}/estado", id)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.estado").value("completado"));
        
        verify(pagoService, times(1)).actualizarEstado(id, "completado");
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
        
        when(pagoService.actualizarEstado(id, "completado"))
            .thenThrow(new RuntimeException("Pago no encontrado ID: " + id));
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/pagos/{id}/estado", id)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").exists());
        
        verify(pagoService, times(1)).actualizarEstado(id, "completado");
    }

    @Test
    void eliminarPago_conIdValido_deberiaRetornarStatus204() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(pagoService).eliminarPago(id);
        
        // Act & Assert
        mockMvc.perform(delete("/api/v1/pagos/{id}", id)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        verify(pagoService, times(1)).eliminarPago(id);
    }
}

