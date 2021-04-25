package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.config.MessageConfig;
import dev.turkmall.onlineshopserver.entity.Attachment;
import dev.turkmall.onlineshopserver.entity.Brand;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.BrandDto;
import dev.turkmall.onlineshopserver.repository.AttachmentRepository;
import dev.turkmall.onlineshopserver.repository.BrandRepository;
import dev.turkmall.onlineshopserver.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrandService {
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    MessageConfig messageConfig;

    public ApiResponse getListPage(int page, int size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));
            Page<Brand> brandPage = brandRepository.findAll(pageRequest);

            return new ApiResponse(
                    brandPage.getContent().stream().map(this::getBrandDto),
                    page,
                    size,
                    brandPage.getTotalPages(),
                    brandPage.getTotalElements()
            );

        } catch (Exception e) {
            return ResponseUtils.errorPageable();
        }
    }


    public ApiResponse getById(Integer id) {
        try {
            Optional<Brand> optionalBrand = brandRepository.findById(id);
            if (optionalBrand.isPresent()) {
                Brand brand = optionalBrand.get();

                return ResponseUtils.success(getBrandDto(brand));
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse deleteById(Integer id) {
        try {
            brandRepository.deleteById(id);
            return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.deleted"));
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
        }
    }

    public BrandDto getBrandDto(Brand brand) {
        return new BrandDto(
                brand.getId(),
                brand.getNameUz(),
                brand.getNameRu(),
                brand.isActive(),
                brand.getAttachment() != null ? brand.getAttachment().getId() : null
        );
    }

    public ApiResponse addBrand(BrandDto brandDto) {
        try {
            ApiResponse apiResponse = makeBrand(new Brand(), brandDto);
            if (apiResponse.isSuccess()) {
                Brand brand = (Brand) apiResponse.getObject();

                brandRepository.save(brand);
                return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.saved"));
            } else {
                return apiResponse;
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse editBrand(BrandDto brandDto) {
        try {
            Optional<Brand> optionalBrand = brandRepository.findById(brandDto.getId());
            if (optionalBrand.isPresent()) {
                ApiResponse apiResponse = makeBrand(optionalBrand.get(), brandDto);
                Brand brand = (Brand) apiResponse.getObject();

                brandRepository.save(brand);
                return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.edited"));
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse makeBrand(Brand brand, BrandDto brandDto) {
        brand.setNameUz(brandDto.getNameUz());
        brand.setNameRu(brandDto.getNameRu());
        brand.setActive(brandDto.isActive());

        if (brandDto.getAttachmentId() != null) {
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(brandDto.getAttachmentId());
            if (optionalAttachment.isPresent()) {
                Attachment attachment = optionalAttachment.get();
                brand.setAttachment(attachment);
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        }
        return new ApiResponse(true, brand);
    }

    public ApiResponse activeBrand(Integer id, boolean active) {
        try {
            Optional<Brand> optionalBrand = brandRepository.findById(id);
            if (optionalBrand.isPresent()) {

                Brand brand = optionalBrand.get();
                brand.setActive(active);

                brandRepository.save(brand);
                return ResponseUtils.success(messageConfig.getMessageByLanguage(active ? "active" : "not.active"));
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }
}
