package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.config.MessageConfig;
import dev.turkmall.onlineshopserver.entity.Basket;
import dev.turkmall.onlineshopserver.entity.Product;
import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.ProductDto;
import dev.turkmall.onlineshopserver.repository.BaskedRepository;
import dev.turkmall.onlineshopserver.repository.ProductRepository;
import dev.turkmall.onlineshopserver.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class BasketService {
    @Autowired
    BaskedRepository baskedRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MessageConfig messageConfig;


    public ApiResponse getByUserId(User user, Integer page, Integer size) {
        List<Product> allByUserId = productRepository.findAllByUserId(user.getId(), page, size);
        return ResponseUtils.success(allByUserId);
    }

    public ApiResponse addBasket(User user, List<ProductDto> productDtoList) {
        try {
            List<Basket> baskets = new ArrayList<>();
            productDtoList.forEach(product -> {
                Optional<Basket> optionalBasket = baskedRepository.findByUserIdAndProductId(user.getId(), product.getId());
                if (optionalBasket.isPresent()) {
                    Basket basket = optionalBasket.get();
                    basket.setCount(product.getCount());

                    baskets.add(basket);
                } else {
                    Optional<Product> optionalProduct = productRepository.findById(product.getId());
                    if (optionalProduct.isPresent()) {
                        Basket basket = new Basket();
                        basket.setUser(user);
                        basket.setProduct(optionalProduct.get());
                        basket.setCount(product.getCount());
                        baskets.add(basket);
                    }
                }
            });

            baskedRepository.saveAll(baskets);
            return ResponseUtils.success("Savatchaga saqlandi");
        } catch (Exception e) {
            return ResponseUtils.error("Savatchaga avval saqlagansiz!");
        }
    }

    public ApiResponse editBasket(User user, UUID id, Integer count) {
        Optional<Basket> optionalBasket = baskedRepository.findById(id);
        if (optionalBasket.isPresent()) {
            Basket basket = optionalBasket.get();

            if (!basket.getUser().getId().equals(user.getId()))
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.permission"));

            basket.setCount(count);
            baskedRepository.save(basket);

            return ResponseUtils.success("O'zgartirildi");
        } else {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }

    }

    public ApiResponse deleteBasket(User user, UUID productId) {
        baskedRepository.deleteByUserIdAndId(user.getId(), productId);
        return ResponseUtils.success("O'chirildi");
    }
}
