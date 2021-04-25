package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.config.MessageConfig;
import dev.turkmall.onlineshopserver.entity.*;
import dev.turkmall.onlineshopserver.entity.*;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.ProductDto;
import dev.turkmall.onlineshopserver.payload.ValueDto;
import dev.turkmall.onlineshopserver.repository.*;
import dev.turkmall.onlineshopserver.utils.ResponseUtils;
import dev.turkmall.onlineshopserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MessageConfig messageConfig;
    @Autowired
    CheckUser checkUser;
    @Autowired
    RandomSerialNum randomSerialNum;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    DetailRepository detailRepository;
    @Autowired
    ValueRepository valueRepository;


    public ApiResponse getListPage(int page, int size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Product> productPage = productRepository.findAll(pageRequest);

            return new ApiResponse(
                    productPage.getContent().stream().map(this::getProductDto),
                    page,
                    size,
                    productPage.getTotalPages(),
                    productPage.getTotalElements()
            );
        } catch (Exception e) {
            return ResponseUtils.errorPageable();
        }
    }


    public ApiResponse getById(UUID productId) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                return ResponseUtils.success(getProductDto(optionalProduct.get()));
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse deleteById(UUID id) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                valueRepository.deleteAllByProductId(product.getId());
                productRepository.deleteById(id);
            }
            return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.deleted"));
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
        }
    }

    public ApiResponse addProduct(User user, ProductDto productDto) {
        if (checkUser.isAdmin(user)) {
            Product product = new Product();

            return makeProduct(product, productDto);
        } else {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("not.permission"));
        }
    }

    public ApiResponse editProduct(UUID id, User user, ProductDto productDto) {
        if (checkUser.isAdmin(user)) {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isPresent()) {
                return makeProduct(optionalProduct.get(), productDto);
            } else {
                return ResponseUtils.success(messageConfig.getMessageByLanguage("not.found"));
            }
        } else {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("not.permission"));
        }
    }

    public ApiResponse deleteValue(UUID valueId) {
        try {
            valueRepository.deleteById(valueId);
            return ResponseUtils.error(messageConfig.getMessageByLanguage("successfully.deleted"));
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
        }
    }

    public ProductDto getProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.isSale(),
                product.getSalePrice(),
                product.getLeftOver(),
                product.getViewCount(),
                product.isTrend(),
                product.getSerialNumber(),
                product.getCategory().getId(),
                product.getCategory().getNameUz(),
                product.getCategory().getNameRu(),
                product.getMainAttachment().getId(),
                getAttachmentId(product.getAttachmentList()),
                getValueDto(valueRepository.findAllByProductId(product.getId()))
        );
    }

    public List<ValueDto> getValueDto(List<Value> valueList) {
        List<ValueDto> valueDtoList = new ArrayList<>();
        if (!valueList.isEmpty()) {
            for (Value value : valueList) {
                valueDtoList.add(getValueDto(value));
            }
        }
        return valueDtoList;
    }

    public ValueDto getValueDto(Value value) {
        return new ValueDto(
                value.getId(),
                value.getName(),
                value.getDetail().getId(),
                value.getDetail().getNameUz(),
                value.getDetail().getNameRu()
        );
    }

    public List<UUID> getAttachmentId(List<Attachment> attachmentList) {
        List<UUID> uuidList = new ArrayList<>();
        for (Attachment attachment : attachmentList) {
            uuidList.add(attachment.getId());
        }
        return uuidList;
    }

    public ApiResponse makeProduct(Product product, ProductDto productDto) {
        try {
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setSale(productDto.isSale());
            product.setSalePrice(productDto.getSalePrice());
            product.setLeftOver(productDto.getLeftOver());
            product.setTrend(productDto.isTrend());
            product.setSerialNumber(randomSerialNum.generateSerialNumber());

            if (productDto.getCategoryId() != null) {
                Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
                if (optionalCategory.isPresent()) {
                    product.setCategory(optionalCategory.get());
                } else {
                    return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
                }
            }

            if (productDto.getPhotoId() != null) {
                Optional<Attachment> optionalAttachment = attachmentRepository.findById(productDto.getPhotoId());
                if (optionalAttachment.isPresent()) {
                    product.setMainAttachment(optionalAttachment.get());
                } else {
                    return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
                }
            }

            if (!productDto.getPhotoIdList().isEmpty()) {
                product.setAttachmentList(attachmentRepository.findAllById(productDto.getPhotoIdList()));
            }

            productRepository.save(product);

            if (!productDto.getValues().isEmpty()) {
                makeValue(product, productDto.getValues());
            }

            return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.saved"));
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse addValueToProduct(User user, ProductDto productDto) {
        try {
            if (checkUser.isAdmin(user)) {
                Optional<Product> optionalProduct = productRepository.findById(productDto.getId());

                if (optionalProduct.isPresent()) {
                    if (!productDto.getValues().isEmpty()) {
                        return makeValue(optionalProduct.get(), productDto.getValues());
                    } else {
                        return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found.input"));
                    }
                } else {
                    return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
                }
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.permission"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse makeValue(Product product, List<ValueDto> valueDtoList) {
        List<Value> values = new ArrayList<>();

        for (ValueDto valueDto : valueDtoList) {
            Optional<Detail> optionalDetail = detailRepository.findById(valueDto.getDetailId());
            if (optionalDetail.isPresent()) {
                values.add(
                        new Value(
                                valueDto.getName(),
                                optionalDetail.get(),
                                product
                        ));
            }
        }
        valueRepository.saveAll(values);
        return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.saved"));
    }
}
