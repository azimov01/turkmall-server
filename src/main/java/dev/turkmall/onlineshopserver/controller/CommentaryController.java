package dev.turkmall.onlineshopserver.controller;

import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.CommentaryDto;
import dev.turkmall.onlineshopserver.security.CurrentUser;
import dev.turkmall.onlineshopserver.service.CommentaryService;
import dev.turkmall.onlineshopserver.utils.AppConstants;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentaryController {
    @Autowired
    CommentaryService commentaryService;

    @ApiOperation(value = "Productga tegishli hamma commentlarni olish")
    @GetMapping("/all/by/{productId}")
    public HttpEntity<?> getListPage(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_SIZE) Integer size,
            @PathVariable UUID productId
    ) {
        ApiResponse resPageable = commentaryService.getListPage(page, size, productId);
        return ResponseEntity.ok(resPageable);
    }

    @ApiOperation(value = "Productga tegishli hamma sub commentlarni olish")
    @GetMapping("/all/sub/{id}")
    public HttpEntity<?> getListPage(
            @PathVariable UUID id,
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_SIZE) Integer size
    ) {
        ApiResponse resPageable = commentaryService.getListPageBySubCommentId(page, size, id);
        return ResponseEntity.ok(resPageable);
    }

    @ApiOperation(value = "Productga comment yozish")
    @PostMapping
    public HttpEntity<?> writeComment(
            @CurrentUser User user,
            @RequestBody CommentaryDto commentaryDto
    ) {
        ApiResponse apiResponse = commentaryService.writeComment(user, commentaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

}
