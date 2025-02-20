package com.tiendacarrito.tienda.service;

import com.tiendacarrito.tienda.models.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

    List<Producto> getAllProducts();
    void deleteProduct(Long id);

    Producto save(Producto producto);
    Producto updateProduct(Producto producto, Long id);
    Optional<Producto> getProductById(Long id);

}
