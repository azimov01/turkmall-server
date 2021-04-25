package dev.turkmall.onlineshopserver.controller;

import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.ProductDto;
import dev.turkmall.onlineshopserver.security.CurrentUser;
import dev.turkmall.onlineshopserver.service.BasketService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

    @Autowired
    BasketService basketService;

    @ApiOperation(value = "Userga tegishli hammasini olish")
    @GetMapping
    public HttpEntity<?> getByUserId(User user, Integer page, Integer size) {
        return ResponseEntity.ok().body(basketService.getByUserId(user, page, size));
    }

    @ApiOperation(value = "Savatga qo'shish")
    @PostMapping
    public HttpEntity<?> addBasket(User user, List<ProductDto> productDtoList) {
        ApiResponse apiResponse = basketService.addBasket(user, productDtoList);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @ApiOperation(value = "Savatni tahrirlash")
    @PutMapping
    public HttpEntity<?> editBasket(User user, UUID id, Integer count) {
        ApiResponse apiResponse = basketService.editBasket(user, id, count);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @ApiOperation(value = "O'chirish")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteBasket(@CurrentUser User user, @PathVariable UUID id) {
        return ResponseEntity.ok(basketService.deleteBasket(user, id));
    }
}
