package com.microservicios.inventario_service.mapper;

import com.microservicios.inventario_service.dto.InventarioRequestDTO;
import com.microservicios.inventario_service.dto.InventarioResponseDTO;
import com.microservicios.inventario_service.model.Inventario;

public class InventarioMapper {

    public static Inventario toEntity(InventarioRequestDTO dto) {
        return Inventario.builder()
                .productoId(dto.getProductoId())
                .cantidad(dto.getCantidad())
                .build();
    }

    public static InventarioResponseDTO toResponseDTO(Inventario inventario) {
        return InventarioResponseDTO.builder()
                .id(inventario.getId())
                .attributes(new InventarioResponseDTO.Attributes(
                        inventario.getProductoId(),
                        inventario.getCantidad()
                ))
                .build();
    }
}
