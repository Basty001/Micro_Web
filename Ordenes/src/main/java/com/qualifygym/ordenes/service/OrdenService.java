package com.qualifygym.ordenes.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qualifygym.ordenes.model.ItemOrden;
import com.qualifygym.ordenes.model.Orden;
import com.qualifygym.ordenes.repository.ItemOrdenRepository;
import com.qualifygym.ordenes.repository.OrdenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ItemOrdenRepository itemOrdenRepository;

    public List<Orden> obtenerTodas() {
        return ordenRepository.findAll();
    }

    public Orden obtenerPorId(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada ID: " + id));
    }

    public List<Orden> obtenerPorUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioId(usuarioId);
    }

    public List<Orden> obtenerPorEstado(String estado) {
        return ordenRepository.findByEstado(estado);
    }

    public List<ItemOrden> obtenerItemsPorOrden(Long ordenId) {
        return itemOrdenRepository.findByOrdenId(ordenId);
    }

    public Orden crearOrden(Long usuarioId, Double total, String direccionEnvio, String notas, List<Map<String, Object>> items) {
        Orden orden = new Orden();
        orden.setUsuarioId(usuarioId);
        orden.setTotal(total);
        orden.setEstado("pendiente");
        orden.setFechaCreacion(LocalDateTime.now());
        orden.setFechaActualizacion(LocalDateTime.now());
        orden.setDireccionEnvio(direccionEnvio);
        orden.setNotas(notas);
        
        Orden ordenGuardada = ordenRepository.save(orden);

        // Crear items de la orden
        for (Map<String, Object> itemData : items) {
            ItemOrden item = new ItemOrden();
            item.setOrden(ordenGuardada);
            item.setProductoId(Long.valueOf(itemData.get("productoId").toString()));
            item.setCantidad(Integer.valueOf(itemData.get("cantidad").toString()));
            item.setPrecioUnitario(Double.valueOf(itemData.get("precioUnitario").toString()));
            item.setSubtotal(item.getPrecioUnitario() * item.getCantidad());
            itemOrdenRepository.save(item);
        }

        return ordenGuardada;
    }

    public Orden actualizarEstado(Long id, String estado) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada ID: " + id));
        orden.setEstado(estado);
        orden.setFechaActualizacion(LocalDateTime.now());
        return ordenRepository.save(orden);
    }

    public void eliminarOrden(Long id) {
        // Eliminar items primero
        List<ItemOrden> items = itemOrdenRepository.findByOrdenId(id);
        itemOrdenRepository.deleteAll(items);
        // Eliminar orden
        ordenRepository.deleteById(id);
    }
}

