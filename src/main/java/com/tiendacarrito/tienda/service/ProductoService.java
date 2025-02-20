package com.tiendacarrito.tienda.service;


import com.tiendacarrito.tienda.models.Producto;
import com.tiendacarrito.tienda.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService implements IProductoService{

    @Autowired
    private IProductoRepository productoRepository;

    @Override
    public List<Producto> getAllProducts(){
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> getProductById(Long id){
        return productoRepository.findById(id);
    }

    @Override
    public Producto save(Producto producto){
        return productoRepository.save(producto);
    }

    @Override
    public void deleteProduct(Long id){
        productoRepository.deleteById(id);
    }

    @Override
    public Producto updateProduct(Producto producto, Long id){
        Producto productoToUpdate = productoRepository.findById(id).get();
        productoToUpdate.setNombre(producto.getNombre());
        productoToUpdate.setDescripcion(producto.getDescripcion());
        productoToUpdate.setPrecio(producto.getPrecio());
        productoToUpdate.setStock(producto.getStock());
        productoToUpdate.setImagenUrl(producto.getImagenUrl());
        return productoRepository.save(productoToUpdate);
    }

}
