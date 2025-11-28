package com.qualifygym.carrito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qualifygym.carrito.model.ItemCarrito;
import com.qualifygym.carrito.repository.ItemCarritoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoService {

    @Autowired
    private ItemCarritoRepository carritoRepository;

    public List<ItemCarrito> obtenerCarritoPorUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    public ItemCarrito agregarItem(Long usuarioId, Long productoId, Integer cantidad, Double precioUnitario) {
        Optional<ItemCarrito> itemExistente = carritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId);
        
        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            return carritoRepository.save(item);
        } else {
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setUsuarioId(usuarioId);
            nuevoItem.setProductoId(productoId);
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setPrecioUnitario(precioUnitario);
            return carritoRepository.save(nuevoItem);
        }
    }

    public ItemCarrito actualizarCantidad(Long itemId, Integer cantidad) {
        ItemCarrito item = carritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado ID: " + itemId));
        item.setCantidad(cantidad);
        return carritoRepository.save(item);
    }

    public void eliminarItem(Long itemId) {
        carritoRepository.deleteById(itemId);
    }

    public void vaciarCarrito(Long usuarioId) {
        carritoRepository.deleteByUsuarioId(usuarioId);
    }

    public void eliminarItemPorProducto(Long usuarioId, Long productoId) {
        carritoRepository.deleteByUsuarioIdAndProductoId(usuarioId, productoId);
    }

    public ItemCarrito obtenerItemPorId(Long itemId) {
        return carritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado ID: " + itemId));
    }
}
