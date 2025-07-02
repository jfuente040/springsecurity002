package com.jfuente040.springsecurity001.controller;

import com.jfuente040.springsecurity001.persistence.entity.ProductEntity;
import com.jfuente040.springsecurity001.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_CONTENT') or hasRole('ADMIN')")
    public List<ProductEntity> getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_CONTENT') or hasRole('ADMIN')")
    public ResponseEntity<ProductEntity> getById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_CONTENT') or hasRole('ADMIN')")
    public ProductEntity create(@RequestBody ProductEntity product) {
        return productService.save(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_CONTENT') or hasRole('ADMIN')")
    public ResponseEntity<ProductEntity> update(@PathVariable Long id, @RequestBody ProductEntity product) {
        return productService.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setDescription(product.getDescription());
                    existing.setPrice(product.getPrice());
                    return ResponseEntity.ok(productService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_CONTENT') or hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productService.findById(id).isPresent()) {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
