package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    public static final Sort SORT_PRICE = Sort.by(Sort.Order.asc("price"));
    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "")
    public List<Product> show(@Param("min") Integer min, @Param("max") Integer max) {
        if (min != null && max != null) {
            return productRepository.findByPriceBetween(min, max, SORT_PRICE);
        } else {
            if (min != null) {
                return productRepository.findByPriceGreaterThanEqual(min, SORT_PRICE);
            }
            if (max != null) {
                return productRepository.findByPriceLessThanEqual(max, SORT_PRICE);
            }
        }

        return productRepository.findAll(SORT_PRICE);
    }

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
