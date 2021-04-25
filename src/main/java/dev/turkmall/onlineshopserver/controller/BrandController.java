package dev.turkmall.onlineshopserver.controller;

import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.BrandDto;
import dev.turkmall.onlineshopserver.service.BrandService;
import dev.turkmall.onlineshopserver.utils.AppConstants;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    @ApiOperation(value = "Hammasini olish")
    @GetMapping("/all")
    public HttpEntity<?> getListPage(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_SIZE) Integer size) {
        ApiResponse resPageable = brandService.getListPage(page, size);
        return ResponseEntity.ok(resPageable);
    }

    @ApiOperation(value = "Bittasini olish")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(
            @PathVariable Integer id
    ) {
        ApiResponse apiResponse = brandService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "O'chirish")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(
            @PathVariable Integer id
    ) {
        ApiResponse apiResponse = brandService.deleteById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Yangi qo'shish")
    @PostMapping("/add")
    public HttpEntity<?> addBrand(
            @RequestBody BrandDto brandDto
    ) {
        ApiResponse apiResponse = brandService.addBrand(brandDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Tahrirlash")
    @PutMapping("/edit")
    public HttpEntity<?> editBrand(
            @RequestBody BrandDto brandDto
    ) {
        ApiResponse apiResponse = brandService.editBrand(brandDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Aktivligini o'chirib qo'yish yoki aksi")
    @GetMapping("/active/{id}")
    public HttpEntity<?> activeBrand(
            @PathVariable Integer id,
            @RequestParam(name = "active") boolean active
    ) {
        ApiResponse apiResponse = brandService.activeBrand(id, active);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }
}
