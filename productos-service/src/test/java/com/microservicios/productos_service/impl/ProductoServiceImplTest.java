package com.microservicios.productos_service.impl;

import com.microservicios.productos_service.dto.ProductoRequestDTO;
import com.microservicios.productos_service.dto.ProductoResponseDTO;
import com.microservicios.productos_service.model.Producto;
import com.microservicios.productos_service.repository.ProductoRepository;
import com.microservicios.productos_service.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductoServiceImplTest {

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Mock
    private ProductoRepository productoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearProducto() {
        ProductoRequestDTO request = new ProductoRequestDTO("Zapato", new BigDecimal("150.0"));
        Producto producto = new Producto(null, "Zapato", new BigDecimal("150.0"));
        Producto saved = new Producto(1L, "Zapato", new BigDecimal("150.0"));

        when(productoRepository.save(any())).thenReturn(saved);

        ProductoResponseDTO response = productoService.crearProducto(request);

        assertEquals("Zapato", response.getAttributes().getNombre());
        assertEquals(150.0, response.getAttributes().getPrecio());
        verify(productoRepository, times(1)).save(any());
    }

    @Test
    void testObtenerProductoPorId() {
        Producto producto = new Producto(1L, "Camisa", new BigDecimal("99.99"));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        ProductoResponseDTO response = productoService.obtenerProducto(1L);

        assertEquals("Camisa", response.getAttributes().getNombre());
        assertEquals(99.99, response.getAttributes().getPrecio());
    }

    @Test
    void testEliminarProducto() {
        productoService.eliminarProducto(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }
}
