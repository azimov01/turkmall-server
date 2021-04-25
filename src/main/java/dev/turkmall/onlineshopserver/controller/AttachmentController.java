package dev.turkmall.onlineshopserver.controller;

import dev.turkmall.onlineshopserver.service.AttachmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {
    @Autowired
    AttachmentService attachmentService;

    @ApiOperation(value = "File upload")
    @PostMapping("/upload")
    public HttpEntity<?> uploadFile(
            MultipartHttpServletRequest request
    ) {
        List<UUID> uuids = attachmentService.uploadFile(request);
        return
                ResponseEntity
                        .ok(uuids);
    }

    @ApiOperation(value = "File download")
    @GetMapping("/{id}")
    public HttpEntity<?> getFile(
            @PathVariable UUID id
    ) {
        return attachmentService.getFile(id);
    }
}
