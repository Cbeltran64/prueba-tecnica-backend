package com.microservicios.inventario_service.service.Impl;

import com.microservicios.inventario_service.dto.InventarioRequestDTO;
import com.microservicios.inventario_service.dto.InventarioResponseDTO;
import com.microservicios.inventario_service.exception.RecursoNoEncontradoException;
import com.microservicios.inventario_service.mapper.InventarioMapper;
import com.microservicios.inventario_service.model.Inventario;
import com.microservicios.inventario_service.repository.InventarioRepository;
import com.microservicios.inventario_service.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final RestTemplate restTemplate;

    @Value("${productos.api.key}")
    private String productosApiKey;

    @Override
    public InventarioResponseDTO consultarPorProductoId(Long productoId) {
        // Validar que el producto existe en productos-service vía HTTP
        String url = "http://productos-service:8080/api/v1/productos/" + productoId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", productosApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNoEncontradoException("❌ Producto no encontrado en productos-service");
        } catch (Exception e) {
            throw new RuntimeException("❌ Error comunicándose con productos-service: " + e.getMessage());
        }

        // Buscar el inventario correspondiente
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("❌ Inventario no encontrado"));

        return InventarioMapper.toResponseDTO(inventario);
    }

    @Override
    public InventarioResponseDTO actualizarCantidad(Long productoId, InventarioRequestDTO dto) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElse(Inventario.builder()
                        .productoId(productoId)
                        .cantidad(0)
                        .build());

        inventario.setCantidad(dto.getCantidad());
        inventarioRepository.save(inventario);

        return InventarioMapper.toResponseDTO(inventario);
    }
}
