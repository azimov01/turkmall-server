package dev.turkmall.onlineshopserver.controller;

import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.service.WishlistService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

public class WishlistController {
    @Autowired
    WishlistService wishlistService;

    @ApiOperation(value = "Userga tegishli hamma wishlistni olish")
    @GetMapping
    public HttpEntity<?> getByUserId(User user, Integer page, Integer size) {
        return ResponseEntity.ok().body(wishlistService.getByUserId(user,page,size));
    }

    @ApiOperation(value = "Wishlistga yangi item qo'shish")
    @PostMapping
    public HttpEntity<?> addWishlist(User user, UUID productId) {
        ApiResponse apiResponse= wishlistService.addWishlist(user,productId);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @ApiOperation(value = "O'chirish")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteWishlist(User user, UUID id) {
        return ResponseEntity.ok(wishlistService.deleteWishlist(user,id));
    }
}
