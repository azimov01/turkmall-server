package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.config.MessageConfig;
import dev.turkmall.onlineshopserver.entity.Category;
import dev.turkmall.onlineshopserver.entity.Detail;
import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.DetailDto;
import dev.turkmall.onlineshopserver.repository.AttachmentRepository;
import dev.turkmall.onlineshopserver.repository.CategoryRepository;
import dev.turkmall.onlineshopserver.repository.DetailRepository;
import dev.turkmall.onlineshopserver.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DetailService {
    @Autowired
    DetailRepository detailRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    MessageConfig messageConfig;
    @Autowired
    CheckUser checkUser;
    @Autowired
    CategoryRepository categoryRepository;

    public ApiResponse getListPage(int page, int size, User user, String active) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));
            Page<Detail> detailPage;
            if (checkUser.isAdmin(user) && active.toLowerCase().equals("false")) {
                detailPage = detailRepository.findAllByActive(false, pageRequest);
            } else if (checkUser.isAdmin(user) && active.toLowerCase().equals("true")) {
                detailPage = detailRepository.findAllByActive(true, pageRequest);
            } else if (checkUser.isAdmin(user)) {
                detailPage = detailRepository.findAll(pageRequest);
            } else {
                detailPage = detailRepository.findAllByActive(true, pageRequest);
            }
            return new ApiResponse(
                    detailPage.getContent().stream().map(this::getDetailDto),
                    page,
                    size,
                    detailPage.getTotalPages(),
                    detailPage.getTotalElements()
            );

        } catch (Exception e) {
            return ResponseUtils.errorPageable();
        }
    }

    public ApiResponse getListPageByCategory(int page, int size, User user, String active, Integer categoryId) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));
            Page<Detail> detailPage;
            if (checkUser.isAdmin(user) && active.toLowerCase().equals("false")) {
                detailPage = detailRepository.findAllByActiveAndCategoryId(false, categoryId, pageRequest);
            } else if (checkUser.isAdmin(user) && active.toLowerCase().equals("true")) {
                detailPage = detailRepository.findAllByActiveAndCategoryId(true, categoryId, pageRequest);
            } else if (checkUser.isAdmin(user)) {
                detailPage = detailRepository.findAllByCategoryId(categoryId, pageRequest);
            } else {
                detailPage = detailRepository.findAllByActiveAndCategoryId(true, categoryId, pageRequest);
            }
            return new ApiResponse(
                    detailPage.getContent().stream().map(this::getDetailDto),
                    page,
                    size,
                    detailPage.getTotalPages(),
                    detailPage.getTotalElements()
            );

        } catch (Exception e) {
            return ResponseUtils.errorPageable();
        }
    }

    public ApiResponse getById(Integer id) {
        try {
            Optional<Detail> optionalDetail = detailRepository.findById(id);
            if (optionalDetail.isPresent()) {
                Detail detail = optionalDetail.get();

                return new ApiResponse("success", true, getDetailDto(detail));
            } else {
                return new ApiResponse(messageConfig.getMessageByLanguage("not.found"), false);
            }
        } catch (Exception e) {
            return new ApiResponse(messageConfig.getMessageByLanguage("server.error"), false);
        }
    }

    public ApiResponse deleteById(Integer id) {
        try {
            detailRepository.deleteById(id);
            return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.deleted"));
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
        }
    }

    public DetailDto getDetailDto(Detail detail) {
        return new DetailDto(
                detail.getId(),
                detail.getNameUz(),
                detail.getNameRu(),
                detail.isActive(),
                detail.getCategory() != null ? detail.getCategory().getId() : null
        );
    }

    public ApiResponse addDetail(DetailDto detailDto) {
        try {

            if (!detailDto.getNames().isEmpty()) {
                for (String name : detailDto.getNames()) {

                    detailDto.setNameUz(name);
                    makeDetail(new Detail(), detailDto);
                }
                return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.saved"));
            }
            return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));

        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse editDetail(DetailDto detailDto) {
        try {
            Optional<Detail> optionalDetail = detailRepository.findById(detailDto.getId());
            if (optionalDetail.isPresent()) {
                return makeDetail(optionalDetail.get(), detailDto);
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse makeDetail(Detail detail, DetailDto detailDto) {
        detail.setNameRu(detailDto.getNameRu());
        detail.setActive(detailDto.isActive());
        detail.setNameUz(detailDto.getNameUz());

        if (detailDto.getCategoryId() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(detailDto.getCategoryId());
            if (optionalCategory.isPresent()) {
                detail.setCategory(optionalCategory.get());
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        }

        detailRepository.save(detail);
        return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.saved"));
    }

    public ApiResponse activeDetail(Integer id, boolean active) {
        try {
            Optional<Detail> optionalDetail = detailRepository.findById(id);
            if (optionalDetail.isPresent()) {

                Detail detail = optionalDetail.get();
                detail.setActive(active);

                detailRepository.save(detail);
                return ResponseUtils.success(messageConfig.getMessageByLanguage(active ? "active" : "not.active"));
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }
}
