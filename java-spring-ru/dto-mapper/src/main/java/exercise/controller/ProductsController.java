package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<ProductDTO> show() {
        var products = productRepository.findAll();

        return products.stream().map(p -> productMapper.map(p)).toList();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    ProductDTO create(@RequestBody ProductCreateDTO productData) {
        // Преобразование в сущность
        var product = productMapper.map(productData);
        productRepository.save(product);
        // Преобразование в DTO
        return productMapper.map(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ProductDTO update(@RequestBody ProductUpdateDTO productData, @PathVariable Long id) {
        var post = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        productMapper.update(productData, post);
        productRepository.save(post);
        var postDTO = productMapper.map(post);
        return postDTO;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ProductDTO show(@PathVariable Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        // Преобразование в DTO
        return productMapper.map(product);
    }
}
