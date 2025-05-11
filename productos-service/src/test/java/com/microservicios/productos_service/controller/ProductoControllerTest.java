package com.microservicios.productos_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.productos_service.dto.ProductoRequestDTO;
import com.microservicios.productos_service.dto.ProductoResponseDTO;
import com.microservicios.productos_service.dto.ProductoResponseDTO.Attributes;
import com.microservicios.productos_service.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoControllerTest.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testObtenerProducto() throws Exception {
        ProductoResponseDTO response = ProductoResponseDTO.builder()
                .id(1L)
                .attributes(new Attributes("Tenis", new BigDecimal("120.0")))
                .build();

        Mockito.when(productoService.obtenerProducto(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.attributes.nombre").value("Tenis"))
                .andExpect(jsonPath("$.data.attributes.precio").value(120.0));
    }

    @Test
    void testCrearProducto() throws Exception {
        ProductoRequestDTO request = new ProductoRequestDTO("Bolso", new BigDecimal("180.0"));
        ProductoResponseDTO response = ProductoResponseDTO.builder()
                .id(2L)
                .attributes(new Attributes("Bolso", new BigDecimal("180.0")))
                .build();

        Mockito.when(productoService.crearProducto(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.attributes.nombre").value("Bolso"))
                .andExpect(jsonPath("$.data.attributes.precio").value(180.0));
    }
}
