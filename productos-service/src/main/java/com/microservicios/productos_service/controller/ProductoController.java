package com.microservicios.productos_service.controller;

import com.microservicios.productos_service.dto.ProductoRequestDTO;
import com.microservicios.productos_service.dto.ProductoResponseDTO;
import com.microservicios.productos_service.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<Object> crearProducto(@RequestBody @Valid ProductoRequestDTO request) {
        ProductoResponseDTO response = productoService.crearProducto(request);
        return ResponseEntity.status(201).body(wrapInJsonApi(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerProducto(@PathVariable Long id) {
        ProductoResponseDTO response = productoService.obtenerProducto(id);
        return ResponseEntity.ok(wrapInJsonApi(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarProducto(@PathVariable Long id,
                                                     @RequestBody @Valid ProductoRequestDTO request) {
        ProductoResponseDTO response = productoService.actualizarProducto(id, request);
        return ResponseEntity.ok(wrapInJsonApi(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Object> listarProductos(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Page<ProductoResponseDTO> pageResult = productoService.listarProductos(PageRequest.of(page, size));

        return ResponseEntity.ok(new Object() {
            public final Object data = pageResult.getContent();
            public final Object meta = new Object() {
                public final int totalElements = (int) pageResult.getTotalElements();
                public final int totalPages = pageResult.getTotalPages();
                public final int currentPage = page;
                public final int size = pageResult.getSize();
            };
            public final Object links = new Object() {
                public final String self = "/api/v1/productos?page=" + page + "&size=" + size;
                public final String next = (page + 1 < pageResult.getTotalPages())
                        ? "/api/v1/productos?page=" + (page + 1) + "&size=" + size : null;
                public final String prev = (page > 0)
                        ? "/api/v1/productos?page=" + (page - 1) + "&size=" + size : null;
            };
        });
    }

    private Object wrapInJsonApi(Object data) {
        return new Object() {
            public final Object responseData = data;
        };
    }
}
