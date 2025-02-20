package com.daniel.pineros.services;

import com.daniel.pineros.entities.Product;
import com.daniel.pineros.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public List<Product> getRelatedProducts(String category, String productId) {
        List<Product> productList = this.productRepository.findByCategoryAndIdNot(category, productId);
        List<Product> randomProducts = new ArrayList<>();
        Random random = new Random();

        if (productList.isEmpty()) {
            return randomProducts; // Devuelve lista vacía si no hay productos relacionados.
        }

        int count = Math.min(2, productList.size()); // No intentar seleccionar más de lo disponible.

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(productList.size()); // Genera un índice aleatorio válido.
            randomProducts.add(productList.get(randomIndex));
            productList.remove(randomIndex); // Evita seleccionar el mismo producto nuevamente.
        }

        return randomProducts;
    }

    public void saveProduct(Product product){
        this.productRepository.save(product);
    }
   public List<Product> getAllProducts(){
        return this.productRepository.findAll();
   }

   public Optional<Product> getProductById(String id){
        return this.productRepository.findById(id);
   }
   public List<Product> getBestPriceProducts(){
        return this.productRepository.findFirst4ByOrderByPriceAsc();
   }
}
