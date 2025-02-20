package com.tiendacarrito.tienda.controller;


import com.tiendacarrito.tienda.models.Producto;
import com.tiendacarrito.tienda.service.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
// Indica que esta clase es un controlador en una aplicación REST y que los métodos devuelven directamente los datos al cliente (en formato JSON, por ejemplo).
@RequestMapping("/api/productos") // Define la ruta base para los endpoints de este controlador: "/api/auth".
@RequiredArgsConstructor
@CrossOrigin
public class ProductoController {


    @Autowired
    private IProductoService productoService;

    @GetMapping("/all")
    public ResponseEntity<List<Producto>> getAllProducts(){
        return ResponseEntity.ok(productoService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Producto>> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productoService.getProductById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<Producto> save(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("stock") Integer stock,
            @RequestParam("imagen") MultipartFile imagen) {

        try {
            String imageUrl = uploadImage(imagen);

            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setImagenUrl(imageUrl);

            return ResponseEntity.ok(productoService.save(producto));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String uploadImage(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get("uploads/");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);

        }

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        } else {
            extension = ".jpg";
        }
        fileName += extension;

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/api/uploads/" + fileName;
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        Optional<Producto> productoExistente = productoService.getProductById(id);

        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);

            if (imagen != null && !imagen.isEmpty()) {
                try {
                    String imageUrl = uploadImage(imagen);
                    producto.setImagenUrl(imageUrl);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }

            Producto productoActualizado = productoService.save(producto);
            return ResponseEntity.ok(productoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productoService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }



}
