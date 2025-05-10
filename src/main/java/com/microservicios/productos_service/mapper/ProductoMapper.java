package com.microservicios.productos_service.mapper;

import com.microservicios.productos_service.dto.ProductoRequestDTO;
import com.microservicios.productos_service.dto.ProductoResponseDTO;
import com.microservicios.productos_service.model.Producto;

public class ProductoMapper {

    public static Producto toEntity(ProductoRequestDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .build();
    }

    public static ProductoResponseDTO toResponseDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setType("productos");
        dto.setAttributes(
                new ProductoResponseDTO.Attributes(
                        producto.getNombre(),
                        producto.getPrecio()
                )
        );
        return dto;
    }
}