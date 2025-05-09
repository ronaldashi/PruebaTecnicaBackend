package com.daniel.pineros.controllers;

import com.daniel.pineros.entities.Message;
import com.daniel.pineros.entities.Product;
import com.daniel.pineros.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/{product_id}")
    public ResponseEntity<Object> getProductById(@PathVariable("product_id")String productId){
        Optional<Product> productOptional = this.productService.getProductById(productId);
        if (productOptional.isEmpty())
            return new ResponseEntity<>(new Message("No encontrado"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(productOptional.get(),HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<Object> getAllProducts(){
        return new ResponseEntity<>(this.productService.getAllProducts(),HttpStatus.OK);
    }
    @GetMapping("/best")
    public ResponseEntity<List<Product>> getBestProducts(){
        return new ResponseEntity<>(this.productService.getBestPriceProducts(),HttpStatus.OK);
    }
    @GetMapping("/related/{category}/{product_id}")
    public ResponseEntity<Object> getRelatedProduct(@PathVariable("category") String category,
                                                    @PathVariable("product_id") String productId) {
        try {
            List<Product> relatedProducts = this.productService.getRelatedProducts(category, productId);

            if (relatedProducts.isEmpty()) {
                return new ResponseEntity<>(new Message("No hay productos relacionados"), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(relatedProducts, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            return new ResponseEntity<>(new Message("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Message> createProduct(@Valid @RequestBody Product product, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise los campos"),HttpStatus.BAD_REQUEST);
        this.productService.saveProduct(product);
        return new ResponseEntity<>(new Message("Creado correctamente"),HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<Message> updateProduct(@Valid @RequestBody Product product, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise los campos"),HttpStatus.BAD_REQUEST);
        this.productService.saveProduct(product);
        return new ResponseEntity<>(new Message("Actualizado correctamente"),HttpStatus.OK);
    }
}
