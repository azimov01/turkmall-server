package dev.turkmall.onlineshopserver.controller;

import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.CategoryDto;
import dev.turkmall.onlineshopserver.security.CurrentUser;
import dev.turkmall.onlineshopserver.service.CategoryService;
import dev.turkmall.onlineshopserver.utils.AppConstants;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @ApiOperation(value = "Hammasini olish activ bo'yicha")
    @GetMapping("/all")
    public HttpEntity<?> getListPage(
            @CurrentUser User user,
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "active") String active) {

        ApiResponse resPageable = categoryService.getListPage(page, size, user, active);
        return ResponseEntity.ok(resPageable);
    }

    @ApiOperation(value = "Hammasini olish activ bo'yicha va otasi bo'yicha")
    @GetMapping("/all/{id}")
    public HttpEntity<?> getChildListPage(
            @CurrentUser User user,
            @PathVariable Integer id,
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "active") String active) {

        ApiResponse resPageable = categoryService.getChildListPage(page, size, id, user, active);
        return ResponseEntity.ok(resPageable);
    }

    @ApiOperation(value = "Bittasini olish")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(
            @PathVariable Integer id
    ) {
        ApiResponse apiResponse = categoryService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "O'chirish")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(
            @PathVariable Integer id
    ) {
        ApiResponse apiResponse = categoryService.deleteById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Qo'shish")
    @PostMapping("/add")
    public HttpEntity<?> addCategory(
            @RequestBody CategoryDto categoryDto
    ) {
        ApiResponse apiResponse = categoryService.addCategory(categoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Tahrirlash")
    @PutMapping("/edit")
    public HttpEntity<?> editCategory(
            @RequestBody CategoryDto categoryDto
    ) {
        ApiResponse apiResponse = categoryService.editCategory(categoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Aktivni o'chirish yoki aksi")
    @GetMapping("/active/{id}")
    public HttpEntity<?> activeCategory(
            @PathVariable Integer id,
            @RequestParam(name = "active") boolean active
    ) {
        ApiResponse apiResponse = categoryService.activeCategory(id, active);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

}
