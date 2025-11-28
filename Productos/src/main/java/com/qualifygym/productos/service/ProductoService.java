package com.qualifygym.productos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qualifygym.productos.model.Producto;
import com.qualifygym.productos.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + id));
    }

    public List<Producto> obtenerPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Producto crearProducto(String nombre, String descripcion, Double precio, String categoria, String imagen, Integer stock) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setCategoria(categoria);
        producto.setImagen(imagen);
        producto.setStock(stock);
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, String nombre, String descripcion, Double precio, String categoria, String imagen, Integer stock) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + id));

        if (nombre != null && !nombre.trim().isEmpty()) {
            producto.setNombre(nombre);
        }
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            producto.setDescripcion(descripcion);
        }
        if (precio != null) {
            producto.setPrecio(precio);
        }
        if (categoria != null && !categoria.trim().isEmpty()) {
            producto.setCategoria(categoria);
        }
        if (imagen != null) {
            producto.setImagen(imagen);
        }
        if (stock != null) {
            producto.setStock(stock);
        }

        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    public void actualizarStock(Long id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + id));
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
    }
}

