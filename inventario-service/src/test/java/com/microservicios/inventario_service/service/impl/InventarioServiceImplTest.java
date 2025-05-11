package com.microservicios.inventario_service.service.impl;

import com.microservicios.inventario_service.dto.InventarioRequestDTO;
import com.microservicios.inventario_service.dto.InventarioResponseDTO;
import com.microservicios.inventario_service.exception.RecursoNoEncontradoException;
import com.microservicios.inventario_service.model.Inventario;
import com.microservicios.inventario_service.repository.InventarioRepository;
import com.microservicios.inventario_service.service.Impl.InventarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

class InventarioServiceImplTest {

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventarioService = new InventarioServiceImpl(inventarioRepository, restTemplate);
    }

    @Test
    void consultarPorProductoId_productoExiste() {
        Long productoId = 1L;
        Inventario inventario = new Inventario(1L, productoId, 10);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventario));

        InventarioResponseDTO result = inventarioService.consultarPorProductoId(productoId);

        assertEquals(productoId, result.getAttributes().getProductoId());
        assertEquals(10, result.getAttributes().getCantidad());
    }

    @Test
    void consultarPorProductoId_productoNoExiste() {
        Long productoId = 99L;

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () ->
                inventarioService.consultarPorProductoId(productoId));

        assertTrue(exception.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void actualizarCantidad_existente() {
        Long productoId = 1L;
        Inventario inventario = new Inventario(1L, productoId, 10);
        InventarioRequestDTO request = new InventarioRequestDTO(productoId, 20);

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any())).thenReturn(inventario);

        InventarioResponseDTO result = inventarioService.actualizarCantidad(productoId, request);

        assertEquals(20, result.getAttributes().getCantidad());
    }

    @Test
    void actualizarCantidad_noExistente_creaRegistro() {
        Long productoId = 2L;
        InventarioRequestDTO request = new InventarioRequestDTO(productoId, 5);

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.empty());
        when(inventarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        InventarioResponseDTO result = inventarioService.actualizarCantidad(productoId, request);

        assertEquals(5, result.getAttributes().getCantidad());
        assertEquals(productoId, result.getAttributes().getProductoId());
    }
}
