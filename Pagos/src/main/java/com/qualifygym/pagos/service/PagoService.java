package com.qualifygym.pagos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qualifygym.pagos.model.Pago;
import com.qualifygym.pagos.repository.PagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    public Pago obtenerPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado ID: " + id));
    }

    public List<Pago> obtenerPorUsuario(Long usuarioId) {
        return pagoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pago> obtenerPorOrden(Long ordenId) {
        return pagoRepository.findByOrdenId(ordenId);
    }

    public List<Pago> obtenerPorEstado(String estado) {
        return pagoRepository.findByEstado(estado);
    }

    public Pago crearPago(Long ordenId, Long usuarioId, Double monto, String metodoPago, String informacionAdicional) {
        Pago pago = new Pago();
        pago.setOrdenId(ordenId);
        pago.setUsuarioId(usuarioId);
        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago);
        pago.setEstado("pendiente");
        pago.setFechaPago(LocalDateTime.now());
        pago.setInformacionAdicional(informacionAdicional);
        return pagoRepository.save(pago);
    }

    public Pago actualizarEstado(Long id, String estado) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado ID: " + id));
        pago.setEstado(estado);
        if ("completado".equals(estado)) {
            pago.setFechaPago(LocalDateTime.now());
        }
        return pagoRepository.save(pago);
    }

    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }
}

