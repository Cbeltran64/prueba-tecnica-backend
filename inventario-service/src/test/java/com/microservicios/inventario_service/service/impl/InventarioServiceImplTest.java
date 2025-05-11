package com.microservicios.inventario_service.service.impl;

import com.microservicios.inventario_service.controller.InventarioController;
import com.microservicios.inventario_service.dto.InventarioRequestDTO;
import com.microservicios.inventario_service.dto.InventarioResponseDTO;
import com.microservicios.inventario_service.exception.RecursoNoEncontradoException;
import com.microservicios.inventario_service.model.Inventario;
import com.microservicios.inventario_service.repository.InventarioRepository;
import com.microservicios.inventario_service.service.Impl.InventarioServiceImpl;
import com.microservicios.inventario_service.service.InventarioService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class InventarioServiceImplTest {

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
        Exception exception = assertThrows(RuntimeException.class, () ->
                inventarioService.consultarPorProductoId(productoId));
        assertTrue(exception.getMessage().contains("Error comunicándose con productos-service"));
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

    @Test
    void testRecursoNoEncontradoExceptionMessage() {
        RecursoNoEncontradoException exception = new RecursoNoEncontradoException("Inventario no encontrado");
        assertEquals("Inventario no encontrado", exception.getMessage());
    }

    @Test
    void testInventarioControllerConsultar() {
        InventarioService mockService = mock(InventarioService.class);
        InventarioController controller = new InventarioController(mockService);
        InventarioResponseDTO response = new InventarioResponseDTO();
        when(mockService.consultarPorProductoId(1L)).thenReturn(response);
        ResponseEntity<Object> result = controller.obtenerInventario(1L);
        verify(mockService).consultarPorProductoId(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testInventarioControllerActualizar() {
        InventarioService mockService = mock(InventarioService.class);
        InventarioController controller = new InventarioController(mockService);
        InventarioRequestDTO request = new InventarioRequestDTO(1L, 15);
        InventarioResponseDTO response = new InventarioResponseDTO();
        when(mockService.actualizarCantidad(1L, request)).thenReturn(response);
        ResponseEntity<Object> result = controller.actualizarInventario(1L, request);
        verify(mockService).actualizarCantidad(1L, request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }
}