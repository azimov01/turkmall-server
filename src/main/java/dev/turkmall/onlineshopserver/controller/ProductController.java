package dev.turkmall.onlineshopserver.controller;

import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.ProductDto;
import dev.turkmall.onlineshopserver.security.CurrentUser;
import dev.turkmall.onlineshopserver.service.ProductService;
import dev.turkmall.onlineshopserver.utils.AppConstants;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @ApiOperation(value = "Hammasini olish")
    @GetMapping("/all")
    public HttpEntity<?> getListPage(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_SIZE) Integer size
    ) {
        ApiResponse resPageable = productService.getListPage(page, size);
        return ResponseEntity.ok(resPageable);
    }

    @ApiOperation(value = "Bittasini olish")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = productService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "O'chirish")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(@PathVariable UUID id) {
        ApiResponse apiResponse = productService.deleteById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Qo'shish")
    @PostMapping("/add")
    public HttpEntity<?> addProduct(
            @CurrentUser User user,
            @RequestBody ProductDto productDto
    ) {
        ApiResponse apiResponse = productService.addProduct(user, productDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Tahrirlash")
    @PutMapping("/edit/{id}")
    public HttpEntity<?> editProduct(
            @CurrentUser User user,
            @PathVariable UUID id,
            @RequestBody ProductDto productDto
    ) {
        ApiResponse apiResponse = productService.editProduct(id, user, productDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }


    @ApiOperation(value = "Mahsulotga value qo'shish")
    @PostMapping("/add/value")
    public HttpEntity<?> addValueToProduct(
            @CurrentUser User user,
            @RequestBody ProductDto productDto
    ) {
        ApiResponse apiResponse = productService.addValueToProduct(user, productDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Mahsulotdan valueni o'chirish")
    @DeleteMapping("/value/{id}")
    public HttpEntity<?> deleteValue(
            @PathVariable UUID id
    ) {
        ApiResponse apiResponse = productService.deleteValue(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

}
