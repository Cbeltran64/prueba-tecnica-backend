package com.microservicios.productos_service.service;

import com.microservicios.productos_service.dto.ProductoRequestDTO;
import com.microservicios.productos_service.dto.ProductoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoService {

    ProductoResponseDTO crearProducto(ProductoRequestDTO dto);

    ProductoResponseDTO obtenerProducto(Long id);

    ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO dto);

    void eliminarProducto(Long id);

    Page<ProductoResponseDTO> listarProductos(Pageable pageable);
}
