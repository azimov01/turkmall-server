package dev.turkmall.onlineshopserver.service;


import dev.turkmall.onlineshopserver.entity.Product;
import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.entity.Wishlist;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.repository.ProductRepository;
import dev.turkmall.onlineshopserver.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WishlistService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    WishlistRepository wishlistRepository;

    public ApiResponse getByUserId(User user, Integer page, Integer size) {
        List<Product> allByUserId = wishlistRepository.findAllByUserId(user.getId(), page, size);
        return new ApiResponse(true, allByUserId);
    }

    public ApiResponse addWishlist(User user, UUID productId) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                Wishlist wishlist = new Wishlist();
                wishlist.setUser(user);
                wishlist.setProduct(product);

                wishlistRepository.save(wishlist);
                return new ApiResponse("Saqlandi", true);
            } else {
                return new ApiResponse("Avval saqlagansiz!", false);
            }

        } catch (Exception e) {
            return new ApiResponse("Avval saqlagansiz!", false);
        }
    }

    public ApiResponse deleteWishlist(User user, UUID productId) {
        wishlistRepository.deleteByUserIdAndId(user.getId(), productId);
        return new ApiResponse("O'chirildi", true);
    }
}
