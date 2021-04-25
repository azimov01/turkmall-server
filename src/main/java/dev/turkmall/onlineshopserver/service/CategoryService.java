package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.config.MessageConfig;
import dev.turkmall.onlineshopserver.entity.Category;
import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.CategoryDto;
import dev.turkmall.onlineshopserver.repository.CategoryRepository;
import dev.turkmall.onlineshopserver.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    MessageConfig messageConfig;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CheckUser checkUser;


    public ApiResponse getListPage(int page, int size, User user, String active) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            Page<Category> categoryPage;

            if (checkUser.isAdmin(user) && active.toLowerCase().equals("true")) {
                categoryPage = categoryRepository.findAllFatherCategory(pageRequest, true);
            } else if (checkUser.isAdmin(user) && active.toLowerCase().equals("false")) {
                categoryPage = categoryRepository.findAllFatherCategory(pageRequest, false);
            } else if (checkUser.isAdmin(user)) {
                categoryPage = categoryRepository.findAllFatherCategory(pageRequest);
            } else {
                categoryPage = categoryRepository.findAllFatherCategory(pageRequest, true);
            }

            return new ApiResponse(
                    categoryPage.getContent().stream().map(this::getCategoryDto),
                    page,
                    size,
                    categoryPage.getTotalPages(),
                    categoryPage.getTotalElements()
            );
        } catch (Exception e) {
            return ResponseUtils.errorPageable();
        }
    }


    public ApiResponse getChildListPage(int page, int size, int id, User user, String active) {
        try {

            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));
            Page<Category> categoryPage;

            if (checkUser.isAdmin(user) && active.toLowerCase().equals("true")) {
                categoryPage = categoryRepository.findAllChildCategory(id, true, pageRequest);
            } else if (checkUser.isAdmin(user) && active.toLowerCase().equals("false")) {
                categoryPage = categoryRepository.findAllChildCategory(id, false, pageRequest);
            } else if (checkUser.isAdmin(user)) {
                categoryPage = categoryRepository.findAllChildCategory(id, pageRequest);
            } else {
                categoryPage = categoryRepository.findAllChildCategory(id, true, pageRequest);
            }

            return new ApiResponse(
                    categoryPage.getContent().stream().map(this::getCategoryDto),
                    page,
                    size,
                    categoryPage.getTotalPages(),
                    categoryPage.getTotalElements()
            );
        } catch (Exception e) {
            return ResponseUtils.errorPageable();
        }
    }

    public ApiResponse getById(Integer productId) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(productId);
            if (optionalCategory.isPresent()) {

                Category category = optionalCategory.get();

                return ResponseUtils.success(getCategoryDto(category));
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse deleteById(Integer id) {
        try {
            categoryRepository.deleteById(id);
            return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.deleted"));
        } catch (Exception e) {
            return ResponseUtils.error("Buning bolasi bor. Bechorani otasiz qoldirmang 🤪");
        }
    }

    public ApiResponse addCategory(CategoryDto categoryDto) {
        try {
            ApiResponse apiResponse = makeCategory(new Category(), categoryDto);
            if (apiResponse.isSuccess()) {
                Category category = (Category) apiResponse.getObject();

                categoryRepository.save(category);
                return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.saved"));
            } else {
                return apiResponse;
            }

        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse editCategory(CategoryDto categoryDto) {
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(categoryDto.getId());
            if (categoryOptional.isPresent()) {
                ApiResponse apiResponse = makeCategory(categoryOptional.get(), categoryDto);
                if (apiResponse.isSuccess()) {
                    Category category = (Category) apiResponse.getObject();

                    categoryRepository.save(category);
                    return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.saved"));
                } else {
                    return apiResponse;
                }
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse makeCategory(Category category, CategoryDto categoryDto) {
        category.setNameUz(categoryDto.getNameUz());
        category.setNameRu(categoryDto.getNameRu());
        category.setActive(categoryDto.isActive());

        if (categoryDto.getCategoryId() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryDto.getCategoryId());
            if (optionalCategory.isPresent()) {
                category.setCategory(optionalCategory.get());
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        }
        return ResponseUtils.success(category);
    }

    public CategoryDto getCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getNameUz(),
                category.getNameRu(),
                category.isActive(),
                category.getCategory() != null ? category.getCategory().getId() : null
        );
    }

    public ApiResponse activeCategory(Integer id, boolean active) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isPresent()) {

                Category category = optionalCategory.get();
                category.setActive(active);

                categoryRepository.save(category);
                return ResponseUtils.success(messageConfig.getMessageByLanguage(active ? "active" : "not.active"));
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

}
