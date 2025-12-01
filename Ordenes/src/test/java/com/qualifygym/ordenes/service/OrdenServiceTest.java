package com.qualifygym.ordenes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.qualifygym.ordenes.model.ItemOrden;
import com.qualifygym.ordenes.model.Orden;
import com.qualifygym.ordenes.repository.OrdenRepository;
import com.qualifygym.ordenes.repository.ItemOrdenRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

/**
 * Tests unitarios para OrdenService
 * 
 * Esta clase contiene tests que verifican la lógica de negocio del servicio de órdenes,
 * incluyendo creación, actualización, eliminación y búsqueda de órdenes.
 */
class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private ItemOrdenRepository itemOrdenRepository;

    @InjectMocks
    private OrdenService ordenService;

    private Orden ordenTest;
    private ItemOrden itemOrdenTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
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
    void obtenerTodas_debeRetornarListaDeOrdenes() {
        // Arrange
        List<Orden> ordenes = new ArrayList<>();
        ordenes.add(ordenTest);
        
        when(ordenRepository.findAll()).thenReturn(ordenes);
        
        // Act
        List<Orden> resultado = ordenService.obtenerTodas();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(ordenRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_conIdExistente_debeRetornarOrden() {
        // Arrange
        Long id = 1L;
        when(ordenRepository.findById(id)).thenReturn(Optional.of(ordenTest));
        
        // Act
        Orden resultado = ordenService.obtenerPorId(id);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(ordenRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorId_conIdInexistente_debeLanzarExcepcion() {
        // Arrange
        Long id = 999L;
        when(ordenRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ordenService.obtenerPorId(id);
        });
        
        assertTrue(exception.getMessage().contains("Orden no encontrada"));
        verify(ordenRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorUsuario_debeRetornarListaDeOrdenes() {
        // Arrange
        Long usuarioId = 1L;
        List<Orden> ordenes = new ArrayList<>();
        ordenes.add(ordenTest);
        
        when(ordenRepository.findByUsuarioId(usuarioId)).thenReturn(ordenes);
        
        // Act
        List<Orden> resultado = ordenService.obtenerPorUsuario(usuarioId);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(ordenRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    void obtenerPorEstado_debeRetornarListaDeOrdenes() {
        // Arrange
        String estado = "pendiente";
        List<Orden> ordenes = new ArrayList<>();
        ordenes.add(ordenTest);
        
        when(ordenRepository.findByEstado(estado)).thenReturn(ordenes);
        
        // Act
        List<Orden> resultado = ordenService.obtenerPorEstado(estado);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(ordenRepository, times(1)).findByEstado(estado);
    }

    @Test
    void obtenerItemsPorOrden_debeRetornarListaDeItems() {
        // Arrange
        Long ordenId = 1L;
        List<ItemOrden> items = new ArrayList<>();
        items.add(itemOrdenTest);
        
        when(itemOrdenRepository.findByOrdenId(ordenId)).thenReturn(items);
        
        // Act
        List<ItemOrden> resultado = ordenService.obtenerItemsPorOrden(ordenId);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(itemOrdenRepository, times(1)).findByOrdenId(ordenId);
    }

    @Test
    void crearOrden_conDatosValidos_debeRetornarOrdenCreada() {
        // Arrange
        Long usuarioId = 1L;
        Double total = 99.99;
        String direccionEnvio = "Calle Principal 123";
        String notas = "Notas de prueba";
        
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item1 = new HashMap<>();
        item1.put("productoId", 1L);
        item1.put("cantidad", 2);
        item1.put("precioUnitario", 49.99);
        items.add(item1);
        
        when(ordenRepository.save(any(Orden.class))).thenAnswer(invocation -> {
            Orden orden = invocation.getArgument(0);
            orden.setId(1L);
            return orden;
        });
        
        when(itemOrdenRepository.save(any(ItemOrden.class))).thenReturn(itemOrdenTest);
        
        // Act
        Orden resultado = ordenService.crearOrden(usuarioId, total, direccionEnvio, notas, items);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getUsuarioId());
        assertEquals(total, resultado.getTotal());
        assertEquals("pendiente", resultado.getEstado());
        verify(ordenRepository, times(1)).save(any(Orden.class));
        verify(itemOrdenRepository, times(1)).save(any(ItemOrden.class));
    }

    @Test
    void actualizarEstado_conEstadoValido_debeRetornarOrdenActualizada() {
        // Arrange
        Long id = 1L;
        String nuevoEstado = "completado";
        
        when(ordenRepository.findById(id)).thenReturn(Optional.of(ordenTest));
        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenTest);
        
        // Act
        Orden resultado = ordenService.actualizarEstado(id, nuevoEstado);
        
        // Assert
        assertNotNull(resultado);
        verify(ordenRepository, times(1)).findById(id);
        verify(ordenRepository, times(1)).save(any(Orden.class));
    }

    @Test
    void actualizarEstado_conIdInexistente_debeLanzarExcepcion() {
        // Arrange
        Long id = 999L;
        String estado = "completado";
        
        when(ordenRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ordenService.actualizarEstado(id, estado);
        });
        
        assertTrue(exception.getMessage().contains("Orden no encontrada"));
        verify(ordenRepository, times(1)).findById(id);
        verify(ordenRepository, never()).save(any(Orden.class));
    }

    @Test
    void eliminarOrden_conIdValido_debeEliminarOrdenYItems() {
        // Arrange
        Long id = 1L;
        List<ItemOrden> items = new ArrayList<>();
        items.add(itemOrdenTest);
        
        when(itemOrdenRepository.findByOrdenId(id)).thenReturn(items);
        doNothing().when(itemOrdenRepository).deleteAll(items);
        doNothing().when(ordenRepository).deleteById(id);
        
        // Act
        ordenService.eliminarOrden(id);
        
        // Assert
        verify(itemOrdenRepository, times(1)).findByOrdenId(id);
        verify(itemOrdenRepository, times(1)).deleteAll(items);
        verify(ordenRepository, times(1)).deleteById(id);
    }
}

