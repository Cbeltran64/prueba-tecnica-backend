package com.microservicios.inventario_service.controller;

import com.microservicios.inventario_service.dto.InventarioRequestDTO;
import com.microservicios.inventario_service.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping("/{productoId}")
    public ResponseEntity<Object> obtenerInventario(@PathVariable Long productoId) {
        var response = inventarioService.consultarPorProductoId(productoId);
        return ResponseEntity.ok(new Object() {
            public final Object data = response;
        });
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<Object> actualizarInventario(@PathVariable Long productoId,
                                                       @RequestBody @Valid InventarioRequestDTO request) {
        var response = inventarioService.actualizarCantidad(productoId, request);
        return ResponseEntity.ok(new Object() {
            public final Object data = response;
        });
    }
}
