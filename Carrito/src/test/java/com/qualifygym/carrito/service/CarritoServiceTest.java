package com.qualifygym.carrito.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.qualifygym.carrito.model.ItemCarrito;
import com.qualifygym.carrito.repository.ItemCarritoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tests unitarios para CarritoService
 * 
 * Esta clase contiene tests que verifican la lógica de negocio del servicio de carrito,
 * incluyendo agregar, actualizar, eliminar items y vaciar carrito.
 */
class CarritoServiceTest {

    @Mock
    private ItemCarritoRepository carritoRepository;

    @InjectMocks
    private CarritoService carritoService;

    private ItemCarrito itemCarritoTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        itemCarritoTest = new ItemCarrito();
        itemCarritoTest.setId(1L);
        itemCarritoTest.setUsuarioId(1L);
        itemCarritoTest.setProductoId(1L);
        itemCarritoTest.setCantidad(2);
        itemCarritoTest.setPrecioUnitario(29.99);
    }

    @Test
    void obtenerCarritoPorUsuario_debeRetornarListaDeItems() {
        // Arrange
        Long usuarioId = 1L;
        List<ItemCarrito> items = new ArrayList<>();
        items.add(itemCarritoTest);
        
        when(carritoRepository.findByUsuarioId(usuarioId)).thenReturn(items);
        
        // Act
        List<ItemCarrito> resultado = carritoService.obtenerCarritoPorUsuario(usuarioId);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(carritoRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    void agregarItem_conItemNuevo_debeRetornarItemCreado() {
        // Arrange
        Long usuarioId = 1L;
        Long productoId = 2L;
        Integer cantidad = 1;
        Double precioUnitario = 39.99;
        
        when(carritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId))
            .thenReturn(Optional.empty());
        when(carritoRepository.save(any(ItemCarrito.class))).thenAnswer(invocation -> {
            ItemCarrito item = invocation.getArgument(0);
            item.setId(2L);
            return item;
        });
        
        // Act
        ItemCarrito resultado = carritoService.agregarItem(usuarioId, productoId, cantidad, precioUnitario);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getUsuarioId());
        assertEquals(productoId, resultado.getProductoId());
        assertEquals(cantidad, resultado.getCantidad());
        assertEquals(precioUnitario, resultado.getPrecioUnitario());
        verify(carritoRepository, times(1)).findByUsuarioIdAndProductoId(usuarioId, productoId);
        verify(carritoRepository, times(1)).save(any(ItemCarrito.class));
    }

    @Test
    void agregarItem_conItemExistente_debeActualizarCantidad() {
        // Arrange
        Long usuarioId = 1L;
        Long productoId = 1L;
        Integer cantidadNueva = 3;
        Double precioUnitario = 29.99;
        
        when(carritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId))
            .thenReturn(Optional.of(itemCarritoTest));
        when(carritoRepository.save(any(ItemCarrito.class))).thenReturn(itemCarritoTest);
        
        // Act
        ItemCarrito resultado = carritoService.agregarItem(usuarioId, productoId, cantidadNueva, precioUnitario);
        
        // Assert
        assertNotNull(resultado);
        verify(carritoRepository, times(1)).findByUsuarioIdAndProductoId(usuarioId, productoId);
        verify(carritoRepository, times(1)).save(any(ItemCarrito.class));
    }

    @Test
    void actualizarCantidad_conItemExistente_debeRetornarItemActualizado() {
        // Arrange
        Long itemId = 1L;
        Integer nuevaCantidad = 5;
        
        when(carritoRepository.findById(itemId)).thenReturn(Optional.of(itemCarritoTest));
        when(carritoRepository.save(any(ItemCarrito.class))).thenReturn(itemCarritoTest);
        
        // Act
        ItemCarrito resultado = carritoService.actualizarCantidad(itemId, nuevaCantidad);
        
        // Assert
        assertNotNull(resultado);
        verify(carritoRepository, times(1)).findById(itemId);
        verify(carritoRepository, times(1)).save(any(ItemCarrito.class));
    }

    @Test
    void actualizarCantidad_conIdInexistente_debeLanzarExcepcion() {
        // Arrange
        Long itemId = 999L;
        Integer cantidad = 5;
        
        when(carritoRepository.findById(itemId)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            carritoService.actualizarCantidad(itemId, cantidad);
        });
        
        assertTrue(exception.getMessage().contains("Item no encontrado"));
        verify(carritoRepository, times(1)).findById(itemId);
        verify(carritoRepository, never()).save(any(ItemCarrito.class));
    }

    @Test
    void eliminarItem_conIdValido_debeEliminarItem() {
        // Arrange
        Long itemId = 1L;
        doNothing().when(carritoRepository).deleteById(itemId);
        
        // Act
        carritoService.eliminarItem(itemId);
        
        // Assert
        verify(carritoRepository, times(1)).deleteById(itemId);
    }

    @Test
    void vaciarCarrito_conUsuarioIdValido_debeEliminarTodosLosItems() {
        // Arrange
        Long usuarioId = 1L;
        doNothing().when(carritoRepository).deleteByUsuarioId(usuarioId);
        
        // Act
        carritoService.vaciarCarrito(usuarioId);
        
        // Assert
        verify(carritoRepository, times(1)).deleteByUsuarioId(usuarioId);
    }

    @Test
    void eliminarItemPorProducto_conDatosValidos_debeEliminarItem() {
        // Arrange
        Long usuarioId = 1L;
        Long productoId = 1L;
        doNothing().when(carritoRepository).deleteByUsuarioIdAndProductoId(usuarioId, productoId);
        
        // Act
        carritoService.eliminarItemPorProducto(usuarioId, productoId);
        
        // Assert
        verify(carritoRepository, times(1)).deleteByUsuarioIdAndProductoId(usuarioId, productoId);
    }

    @Test
    void obtenerItemPorId_conIdExistente_debeRetornarItem() {
        // Arrange
        Long itemId = 1L;
        when(carritoRepository.findById(itemId)).thenReturn(Optional.of(itemCarritoTest));
        
        // Act
        ItemCarrito resultado = carritoService.obtenerItemPorId(itemId);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(itemId, resultado.getId());
        verify(carritoRepository, times(1)).findById(itemId);
    }

    @Test
    void obtenerItemPorId_conIdInexistente_debeLanzarExcepcion() {
        // Arrange
        Long itemId = 999L;
        when(carritoRepository.findById(itemId)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            carritoService.obtenerItemPorId(itemId);
        });
        
        assertTrue(exception.getMessage().contains("Item no encontrado"));
        verify(carritoRepository, times(1)).findById(itemId);
    }
}

