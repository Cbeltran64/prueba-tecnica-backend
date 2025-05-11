package com.microservicios.productos_service.impl;

import com.microservicios.productos_service.config.SwaggerConfig;
import com.microservicios.productos_service.controller.ProductoController;
import com.microservicios.productos_service.dto.ProductoRequestDTO;
import com.microservicios.productos_service.dto.ProductoResponseDTO;
import com.microservicios.productos_service.exception.GlobalExceptionHandler;
import com.microservicios.productos_service.exception.RecursoNoEncontradoException;
import com.microservicios.productos_service.model.Producto;
import com.microservicios.productos_service.repository.ProductoRepository;
import com.microservicios.productos_service.service.ProductoService;
import com.microservicios.productos_service.service.impl.ProductoServiceImpl;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
public class ProductoServiceImplTest {

    private final ProductoRepository repository = mock(ProductoRepository.class);
    private final ProductoServiceImpl service = new ProductoServiceImpl(repository);

    @Test
    void testCrearProducto() {
        ProductoRequestDTO request = new ProductoRequestDTO("Zapato", new BigDecimal("100.0"));
        Producto producto = new Producto(1L, "Zapato", new BigDecimal("100.0"));
        when(repository.save(any())).thenReturn(producto);

        ProductoResponseDTO response = service.crearProducto(request);

        assertEquals("Zapato", response.getAttributes().getNombre());
        assertEquals(new BigDecimal("100.0"), response.getAttributes().getPrecio());
    }

    @Test
    void testObtenerProductoPorId() {
        Producto producto = new Producto(1L, "Camisa", new BigDecimal("80.0"));
        when(repository.findById(1L)).thenReturn(Optional.of(producto));

        ProductoResponseDTO response = service.obtenerProducto(1L);

        assertEquals("Camisa", response.getAttributes().getNombre());
        assertEquals(new BigDecimal("80.0"), response.getAttributes().getPrecio());
    }

    @Test
    void testObtenerProductoPorIdNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerProducto(99L));
    }

    @Test
    void testActualizarProducto() {
        Long id = 1L;
        Producto productoExistente = new Producto(id, "Zapato", new BigDecimal("100.0"));
        Producto productoActualizado = new Producto(id, "Tenis", new BigDecimal("200.0"));

        when(repository.findById(id)).thenReturn(Optional.of(productoExistente));
        when(repository.save(any())).thenReturn(productoActualizado);

        ProductoRequestDTO request = new ProductoRequestDTO("Tenis", new BigDecimal("200.0"));
        ProductoResponseDTO response = service.actualizarProducto(id, request);

        assertEquals("Tenis", response.getAttributes().getNombre());
        assertEquals(new BigDecimal("200.0"), response.getAttributes().getPrecio());
    }

    @Test
    void testActualizarProductoNoExiste() {
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        ProductoRequestDTO request = new ProductoRequestDTO("Tenis", new BigDecimal("200.0"));
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizarProducto(id, request));
    }

    @Test
    void testEliminarProductoNoExiste() {
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminarProducto(id));
    }

    @Test
    void testListarProductos() {
        Producto p1 = new Producto(1L, "Camisa", new BigDecimal("50.0"));
        Producto p2 = new Producto(2L, "Pantalón", new BigDecimal("90.0"));
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(p1, p2)));
        Page<ProductoResponseDTO> productos = service.listarProductos(pageable);
        assertEquals(2, productos.getContent().size());
    }

    @Test
    void testRecursoNoEncontradoExceptionMessage() {
        RecursoNoEncontradoException exception = new RecursoNoEncontradoException("Producto no encontrado");
        assertEquals("Producto no encontrado", exception.getMessage());
    }

    @Test
    void testSwaggerConfigApiInfoBeanCreation() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.apiInfo();
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("API Productos", openAPI.getInfo().getTitle());
    }

    @Test
    void testProductoControllerCrear() {
        ProductoService mockService = mock(ProductoService.class);
        ProductoController controller = new ProductoController(mockService);
        ProductoRequestDTO request = new ProductoRequestDTO("Zapato", new BigDecimal("100.0"));
        ProductoResponseDTO response = new ProductoResponseDTO();
        when(mockService.crearProducto(request)).thenReturn(response);
        ResponseEntity<Object> responseEntity = controller.crearProducto(request);
        verify(mockService).crearProducto(request);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testProductoControllerObtener() {
        ProductoService mockService = mock(ProductoService.class);
        ProductoController controller = new ProductoController(mockService);
        ProductoResponseDTO response = new ProductoResponseDTO();
        when(mockService.obtenerProducto(1L)).thenReturn(response);
        ResponseEntity<Object> responseEntity = controller.obtenerProducto(1L);
        verify(mockService).obtenerProducto(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testProductoControllerActualizar() {
        ProductoService mockService = mock(ProductoService.class);
        ProductoController controller = new ProductoController(mockService);
        ProductoRequestDTO request = new ProductoRequestDTO("Tenis", new BigDecimal("150.0"));
        ProductoResponseDTO response = new ProductoResponseDTO();
        when(mockService.actualizarProducto(1L, request)).thenReturn(response);
        ResponseEntity<Object> responseEntity = controller.actualizarProducto(1L, request);
        verify(mockService).actualizarProducto(1L, request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testGlobalExceptionHandlerValidation() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("producto", "nombre", "no puede estar vacío");
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        ResponseEntity<Object> response = handler.handleValidationErrors(exception);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }
}
