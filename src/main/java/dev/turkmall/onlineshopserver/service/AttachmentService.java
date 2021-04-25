package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.entity.Attachment;
import dev.turkmall.onlineshopserver.entity.AttachmentContent;
import dev.turkmall.onlineshopserver.repository.AttachmentContentRepository;
import dev.turkmall.onlineshopserver.repository.AttachmentRepository;
import dev.turkmall.onlineshopserver.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.*;

@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    public List<UUID> uploadFile(MultipartHttpServletRequest request) {
        try {
            Iterator<String> fileNames = request.getFileNames();
            List<UUID> attachmentIdList = new ArrayList<>();
            while (fileNames.hasNext()) {
                MultipartFile file = request.getFile(fileNames.next());
                assert file != null;
                Attachment attachment = new Attachment(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize());
                Attachment savedAttachment = attachmentRepository.save(attachment);

                AttachmentContent attachmentContent = new AttachmentContent(
                        file.getBytes(),
                        savedAttachment
                );
                attachmentContentRepository.save(attachmentContent);
                attachmentIdList.add(savedAttachment.getId());
            }
            return attachmentIdList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpEntity<?> getFile(UUID id) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();

            AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(id);
            return ResponseEntity.ok().contentType(MediaType.valueOf(attachment.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + attachment.getName() + "\"")
                    .body(attachmentContent.getBytes());
        } else {
            return ResponseEntity.status(409).body(ResponseUtils.error());
        }
    }
}
