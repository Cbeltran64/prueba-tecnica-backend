package com.microservicios.inventario_service.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.inventario_service.controller.InventarioController;
import com.microservicios.inventario_service.dto.InventarioRequestDTO;
import com.microservicios.inventario_service.dto.InventarioResponseDTO;
import com.microservicios.inventario_service.dto.InventarioResponseDTO.Attributes;
import com.microservicios.inventario_service.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventarioController.class)
public class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testConsultarInventarioPorProductoId() throws Exception {
        Long productoId = 1L;
        InventarioResponseDTO response = InventarioResponseDTO.builder()
                .id(productoId)
                .attributes(new Attributes(productoId, 100))
                .build();

        Mockito.when(inventarioService.consultarPorProductoId(productoId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/inventario/{productoId}", productoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.attributes.productoId").value(productoId))
                .andExpect(jsonPath("$.data.attributes.cantidad").value(100));
    }

    @Test
    void testActualizarCantidad() throws Exception {
        Long productoId = 1L;
        InventarioRequestDTO request = new InventarioRequestDTO(productoId, 50);
        InventarioResponseDTO response = InventarioResponseDTO.builder()
                .id(productoId)
                .attributes(new Attributes(productoId, 50))
                .build();

        Mockito.when(inventarioService.actualizarCantidad(any(), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/inventario/{productoId}", productoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.attributes.productoId").value(productoId))
                .andExpect(jsonPath("$.data.attributes.cantidad").value(50));
    }
}
