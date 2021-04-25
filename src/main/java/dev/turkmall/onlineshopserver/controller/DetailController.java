package dev.turkmall.onlineshopserver.controller;

import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.DetailDto;
import dev.turkmall.onlineshopserver.security.CurrentUser;
import dev.turkmall.onlineshopserver.service.DetailService;
import dev.turkmall.onlineshopserver.utils.AppConstants;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/detail")
public class DetailController {

    @Autowired
    DetailService detailService;

    @ApiOperation(value = "Hamma detallarni olish")
    @GetMapping("/all")
    public HttpEntity<?> getListPage(
            @CurrentUser User user,
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "active") String active) {

        ApiResponse resPageable = detailService.getListPage(page, size, user, active);
        return ResponseEntity.ok(resPageable);
    }

    @ApiOperation(value = "Hamma detallarni category bo'yicha olish")
    @GetMapping("/all/category/{id}")
    public HttpEntity<?> getListPageByCategory(
            @PathVariable Integer id,
            @CurrentUser User user,
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "active") String active) {

        ApiResponse resPageable = detailService.getListPageByCategory(page, size, user, active, id);
        return ResponseEntity.ok(resPageable);
    }

    @ApiOperation(value = "Bitta detalni olsih")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id) {
        ApiResponse apiResponse = detailService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Detalni o'chirish")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(@PathVariable Integer id) {
        ApiResponse apiResponse = detailService.deleteById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Detal qo'shish")
    @PostMapping("/add")
    public HttpEntity<?> addDetail(
            @RequestBody DetailDto detailDto
    ) {
        ApiResponse apiResponse = detailService.addDetail(detailDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Detalni tahrirlash")
    @PutMapping("/edit")
    public HttpEntity<?> editDetail(
            @RequestBody DetailDto detailDto
    ) {
        ApiResponse apiResponse = detailService.editDetail(detailDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Detalni activligini o'zgartirish")
    @GetMapping("/active/{id}")
    public HttpEntity<?> activeDetail(
            @PathVariable Integer id,
            @RequestParam(name = "active") boolean active
    ) {
        ApiResponse apiResponse = detailService.activeDetail(id, active);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }
}
