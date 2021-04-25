package dev.turkmall.onlineshopserver.controller;

import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.MessageDto;
import dev.turkmall.onlineshopserver.service.MessageService;
import dev.turkmall.onlineshopserver.utils.AppConstants;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    @ApiOperation(value = "Hamma messageni olish")
    @GetMapping("/all")
    public HttpEntity<?> getListPage(@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE) Integer page,
                                     @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_SIZE) Integer size) {
        return ResponseEntity.ok().body(messageService.getListPage(page, size));
    }

    @ApiOperation(value = "Bitta messageni olish")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(messageService.getById(id));
    }

    @ApiOperation(value = "Message yuborish")
    @PostMapping("/sendMessage")
    public HttpEntity<?> addMessage(@RequestBody MessageDto messageDto) {
        ApiResponse apiResponse = messageService.addMessage(messageDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @ApiOperation(value = "Messageni o'qish")
    @GetMapping("/readMessage/{id}")
    public HttpEntity<?> readMessage(@PathVariable UUID id) {
        ApiResponse apiResponse = messageService.readMessage(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @ApiOperation(value = "Messageni o'chirish")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(messageService.deleteById(id));
    }

    @ApiOperation(value = "Yangi messagelarni sonini olish")
    @GetMapping("/count")
    public HttpEntity<?> countUnreadMessages() {
        ApiResponse apiResponse = messageService.countUnreadMessages();
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }
}
