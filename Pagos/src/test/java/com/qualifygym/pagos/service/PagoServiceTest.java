package com.qualifygym.pagos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.qualifygym.pagos.model.Pago;
import com.qualifygym.pagos.repository.PagoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tests unitarios para PagoService
 * 
 * Esta clase contiene tests que verifican la lógica de negocio del servicio de pagos,
 * incluyendo creación, actualización, eliminación y búsqueda de pagos.
 */
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Pago pagoTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
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
    void obtenerTodos_debeRetornarListaDePagos() {
        // Arrange
        List<Pago> pagos = new ArrayList<>();
        pagos.add(pagoTest);
        
        when(pagoRepository.findAll()).thenReturn(pagos);
        
        // Act
        List<Pago> resultado = pagoService.obtenerTodos();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_conIdExistente_debeRetornarPago() {
        // Arrange
        Long id = 1L;
        when(pagoRepository.findById(id)).thenReturn(Optional.of(pagoTest));
        
        // Act
        Pago resultado = pagoService.obtenerPorId(id);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(pagoRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorId_conIdInexistente_debeLanzarExcepcion() {
        // Arrange
        Long id = 999L;
        when(pagoRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.obtenerPorId(id);
        });
        
        assertTrue(exception.getMessage().contains("Pago no encontrado"));
        verify(pagoRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorUsuario_debeRetornarListaDePagos() {
        // Arrange
        Long usuarioId = 1L;
        List<Pago> pagos = new ArrayList<>();
        pagos.add(pagoTest);
        
        when(pagoRepository.findByUsuarioId(usuarioId)).thenReturn(pagos);
        
        // Act
        List<Pago> resultado = pagoService.obtenerPorUsuario(usuarioId);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pagoRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    void obtenerPorOrden_debeRetornarListaDePagos() {
        // Arrange
        Long ordenId = 1L;
        List<Pago> pagos = new ArrayList<>();
        pagos.add(pagoTest);
        
        when(pagoRepository.findByOrdenId(ordenId)).thenReturn(pagos);
        
        // Act
        List<Pago> resultado = pagoService.obtenerPorOrden(ordenId);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pagoRepository, times(1)).findByOrdenId(ordenId);
    }

    @Test
    void obtenerPorEstado_debeRetornarListaDePagos() {
        // Arrange
        String estado = "pendiente";
        List<Pago> pagos = new ArrayList<>();
        pagos.add(pagoTest);
        
        when(pagoRepository.findByEstado(estado)).thenReturn(pagos);
        
        // Act
        List<Pago> resultado = pagoService.obtenerPorEstado(estado);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pagoRepository, times(1)).findByEstado(estado);
    }

    @Test
    void crearPago_conDatosValidos_debeRetornarPagoCreado() {
        // Arrange
        Long ordenId = 1L;
        Long usuarioId = 1L;
        Double monto = 99.99;
        String metodoPago = "tarjeta";
        String informacionAdicional = "Transacción aprobada";
        
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> {
            Pago pago = invocation.getArgument(0);
            pago.setId(1L);
            return pago;
        });
        
        // Act
        Pago resultado = pagoService.crearPago(ordenId, usuarioId, monto, metodoPago, informacionAdicional);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(ordenId, resultado.getOrdenId());
        assertEquals(usuarioId, resultado.getUsuarioId());
        assertEquals(monto, resultado.getMonto());
        assertEquals(metodoPago, resultado.getMetodoPago());
        assertEquals("pendiente", resultado.getEstado());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void actualizarEstado_conEstadoValido_debeRetornarPagoActualizado() {
        // Arrange
        Long id = 1L;
        String nuevoEstado = "completado";
        
        when(pagoRepository.findById(id)).thenReturn(Optional.of(pagoTest));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoTest);
        
        // Act
        Pago resultado = pagoService.actualizarEstado(id, nuevoEstado);
        
        // Assert
        assertNotNull(resultado);
        verify(pagoRepository, times(1)).findById(id);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void actualizarEstado_aCompletado_debeActualizarFechaPago() {
        // Arrange
        Long id = 1L;
        String estado = "completado";
        
        when(pagoRepository.findById(id)).thenReturn(Optional.of(pagoTest));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> {
            Pago pago = invocation.getArgument(0);
            pago.setEstado(estado);
            return pago;
        });
        
        // Act
        Pago resultado = pagoService.actualizarEstado(id, estado);
        
        // Assert
        assertNotNull(resultado);
        verify(pagoRepository, times(1)).findById(id);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void actualizarEstado_conIdInexistente_debeLanzarExcepcion() {
        // Arrange
        Long id = 999L;
        String estado = "completado";
        
        when(pagoRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.actualizarEstado(id, estado);
        });
        
        assertTrue(exception.getMessage().contains("Pago no encontrado"));
        verify(pagoRepository, times(1)).findById(id);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void eliminarPago_conIdValido_debeEliminarPago() {
        // Arrange
        Long id = 1L;
        doNothing().when(pagoRepository).deleteById(id);
        
        // Act
        pagoService.eliminarPago(id);
        
        // Assert
        verify(pagoRepository, times(1)).deleteById(id);
    }
}

