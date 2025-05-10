package com.microservicios.productos_service.service.impl;

import com.microservicios.productos_service.dto.ProductoRequestDTO;
import com.microservicios.productos_service.dto.ProductoResponseDTO;
import com.microservicios.productos_service.exception.RecursoNoEncontradoException;
import com.microservicios.productos_service.mapper.ProductoMapper;
import com.microservicios.productos_service.model.Producto;
import com.microservicios.productos_service.repository.ProductoRepository;
import com.microservicios.productos_service.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        Producto producto = ProductoMapper.toEntity(dto);
        return ProductoMapper.toResponseDTO(productoRepository.save(producto));
    }

    @Override
    public ProductoResponseDTO obtenerProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
        return ProductoMapper.toResponseDTO(producto);
    }

    @Override
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        return ProductoMapper.toResponseDTO(productoRepository.save(producto));
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    @Override
    public Page<ProductoResponseDTO> listarProductos(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(ProductoMapper::toResponseDTO);
    }
}
