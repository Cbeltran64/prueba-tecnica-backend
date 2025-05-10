package com.microservicios.productos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {

    private String type = "productos";
    private Long id;
    private Attributes attributes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attributes {
        private String nombre;
        private BigDecimal precio;
    }
}
