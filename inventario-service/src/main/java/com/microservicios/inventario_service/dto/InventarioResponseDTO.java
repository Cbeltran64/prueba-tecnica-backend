package com.microservicios.inventario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponseDTO {

    private String type = "inventarios";
    private Long id;
    private Attributes attributes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attributes {
        private Long productoId;
        private Integer cantidad;
    }
}
