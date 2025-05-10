package com.microservicios.productos_service.repository;

import com.microservicios.productos_service.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // No se necesita agregar nada por ahora
}
