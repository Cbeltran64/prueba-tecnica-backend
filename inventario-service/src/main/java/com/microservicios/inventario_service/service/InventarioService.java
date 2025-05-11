package com.microservicios.inventario_service.service;

import com.microservicios.inventario_service.dto.InventarioRequestDTO;
import com.microservicios.inventario_service.dto.InventarioResponseDTO;

public interface InventarioService {
    InventarioResponseDTO consultarPorProductoId(Long productoId);

    InventarioResponseDTO actualizarCantidad(Long productoId, InventarioRequestDTO requestDTO);
}
