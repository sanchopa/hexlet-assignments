package exercise.controller;

import java.util.List;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.ProductMapper;
import exercise.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<ProductDTO> index() {
        var products = productRepository.findAll();
        var result = products.stream()
                .map(productMapper::map)
                .toList();

        return result;
    }

    @PostMapping("")
    ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductCreateDTO productData) {
        var product = productMapper.map(productData);
        if (product.getCategory() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.map(productRepository.save(product)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ProductDTO show(@PathVariable Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        return productMapper.map(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ProductDTO update(@RequestBody @Valid ProductUpdateDTO productData, @PathVariable Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        //var category =  categoryRepository.findById(productData.getCategoryId().get())
                //.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        productMapper.update(productData, product);
        //product.setCategory(category);
        productRepository.save(product);
        return productMapper.map(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
