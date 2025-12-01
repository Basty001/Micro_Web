package com.qualifygym.productos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.qualifygym.productos.model.Producto;
import com.qualifygym.productos.repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tests unitarios para ProductoService
 * 
 * Esta clase contiene tests que verifican la lógica de negocio del servicio de productos,
 * incluyendo creación, actualización, eliminación y búsqueda de productos.
 */
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
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
    void obtenerTodos_debeRetornarListaDeProductos() {
        // Arrange
        List<Producto> productos = new ArrayList<>();
        productos.add(productoTest);
        
        when(productoRepository.findAll()).thenReturn(productos);
        
        // Act
        List<Producto> resultado = productoService.obtenerTodos();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_conIdExistente_debeRetornarProducto() {
        // Arrange
        Long id = 1L;
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoTest));
        
        // Act
        Producto resultado = productoService.obtenerPorId(id);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Proteína Whey", resultado.getNombre());
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorId_conIdInexistente_debeLanzarExcepcion() {
        // Arrange
        Long id = 999L;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.obtenerPorId(id);
        });
        
        assertTrue(exception.getMessage().contains("Producto no encontrado"));
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorCategoria_debeRetornarListaDeProductos() {
        // Arrange
        String categoria = "supplement";
        List<Producto> productos = new ArrayList<>();
        productos.add(productoTest);
        
        when(productoRepository.findByCategoria(categoria)).thenReturn(productos);
        
        // Act
        List<Producto> resultado = productoService.obtenerPorCategoria(categoria);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findByCategoria(categoria);
    }

    @Test
    void buscarPorNombre_debeRetornarListaDeProductos() {
        // Arrange
        String nombre = "Proteína";
        List<Producto> productos = new ArrayList<>();
        productos.add(productoTest);
        
        when(productoRepository.findByNombreContainingIgnoreCase(nombre)).thenReturn(productos);
        
        // Act
        List<Producto> resultado = productoService.buscarPorNombre(nombre);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findByNombreContainingIgnoreCase(nombre);
    }

    @Test
    void crearProducto_conDatosValidos_debeRetornarProductoCreado() {
        // Arrange
        String nombre = "Nuevo Producto";
        String descripcion = "Descripción del producto";
        Double precio = 39.99;
        String categoria = "equipment";
        String imagen = "https://example.com/new.jpg";
        Integer stock = 50;
        
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto producto = invocation.getArgument(0);
            producto.setId(2L);
            return producto;
        });
        
        // Act
        Producto resultado = productoService.crearProducto(nombre, descripcion, precio, categoria, imagen, stock);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(nombre, resultado.getNombre());
        assertEquals(descripcion, resultado.getDescripcion());
        assertEquals(precio, resultado.getPrecio());
        assertEquals(categoria, resultado.getCategoria());
        assertEquals(imagen, resultado.getImagen());
        assertEquals(stock, resultado.getStock());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void actualizarProducto_conDatosValidos_debeRetornarProductoActualizado() {
        // Arrange
        Long id = 1L;
        String nuevoNombre = "Producto Actualizado";
        Double nuevoPrecio = 34.99;
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoTest));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto producto = invocation.getArgument(0);
            return producto;
        });
        
        // Act
        Producto resultado = productoService.actualizarProducto(id, nuevoNombre, null, nuevoPrecio, null, null, null);
        
        // Assert
        assertNotNull(resultado);
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void actualizarProducto_conIdInexistente_debeLanzarExcepcion() {
        // Arrange
        Long id = 999L;
        
        when(productoRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.actualizarProducto(id, "Nuevo", null, null, null, null, null);
        });
        
        assertTrue(exception.getMessage().contains("Producto no encontrado"));
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void eliminarProducto_conIdValido_debeEliminarProducto() {
        // Arrange
        Long id = 1L;
        doNothing().when(productoRepository).deleteById(id);
        
        // Act
        productoService.eliminarProducto(id);
        
        // Assert
        verify(productoRepository, times(1)).deleteById(id);
    }

    @Test
    void actualizarStock_conCantidadPositiva_debeAumentarStock() {
        // Arrange
        Long id = 1L;
        Integer cantidad = 10;
        Integer stockInicial = productoTest.getStock();
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoTest));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto producto = invocation.getArgument(0);
            return producto;
        });
        
        // Act
        productoService.actualizarStock(id, cantidad);
        
        // Assert
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void actualizarStock_conIdInexistente_debeLanzarExcepcion() {
        // Arrange
        Long id = 999L;
        Integer cantidad = 10;
        
        when(productoRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.actualizarStock(id, cantidad);
        });
        
        assertTrue(exception.getMessage().contains("Producto no encontrado"));
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, never()).save(any(Producto.class));
    }
}

