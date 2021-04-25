package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.config.MessageConfig;
import dev.turkmall.onlineshopserver.entity.Message;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.MessageDto;
import dev.turkmall.onlineshopserver.repository.MessageRepository;
import dev.turkmall.onlineshopserver.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageConfig messageConfig;


    public ApiResponse getListPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt"));
        Page<Message> messages = messageRepository.findAllByRead(false, pageRequest);

        return new ApiResponse(
                messages.getContent().stream().map(this::getMessageDto),
                page,
                size,
                messages.getTotalPages(),
                messages.getTotalElements()
        );
    }


    public ApiResponse getById(UUID productId) {
        Optional<Message> message = messageRepository.findById(productId);
        if (!message.isPresent()) return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
        return ResponseUtils.success(getMessageDto(message.get()));
    }

    public ApiResponse deleteById(UUID id) {
        messageRepository.deleteById(id);
        return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.deleted"));
    }

    private MessageDto getMessageDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getFullName(),
                message.getPhoneNumber(),
                message.getMessageText(),
                message.isRead()
        );
    }

    public ApiResponse addMessage(MessageDto messageDto) {
        try {
            Message message = new Message();
            message.setFullName(messageDto.getFullName());
            message.setMessageText(messageDto.getMessageText());
            message.setPhoneNumber(messageDto.getPhoneNumber());
            message.setRead(false);
            messageRepository.save(message);
            return ResponseUtils.success("Xabaringiz adminga yuborildi! Tez orada siz bilan bog'lanadi");
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse readMessage(UUID id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (!optionalMessage.isPresent()) return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
        Message message = optionalMessage.get();
        message.setRead(true);

        messageRepository.save(message);
        return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.edited"));
    }

    public ApiResponse countUnreadMessages() {
        try {
            Long countUnreadMessages = messageRepository.countAllByRead(false);

            return ResponseUtils.success(countUnreadMessages);
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }
}
