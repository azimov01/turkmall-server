package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.config.MessageConfig;
import dev.turkmall.onlineshopserver.entity.Commentary;
import dev.turkmall.onlineshopserver.entity.Product;
import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.CommentaryDto;
import dev.turkmall.onlineshopserver.repository.CommentaryRepository;
import dev.turkmall.onlineshopserver.repository.ProductRepository;
import dev.turkmall.onlineshopserver.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentaryService {
    @Autowired
    MessageConfig messageConfig;
    @Autowired
    CommentaryRepository commentaryRepository;
    @Autowired
    ProductRepository productRepository;

    public ApiResponse getListPage(int page, int size, UUID productId) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Commentary> commentaryPage = commentaryRepository.findAllByProductId(productId, pageRequest);

            return new ApiResponse(
                    getCommentaryDtoList(commentaryPage),
                    page,
                    size,
                    commentaryPage.getTotalPages(),
                    commentaryPage.getTotalElements()
            );
        } catch (Exception e) {
            return ResponseUtils.errorPageable();
        }
    }

    public ApiResponse getListPageBySubCommentId(int page, int size, UUID commentId) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Commentary> commentaryPage = commentaryRepository.findAllByReplyId(commentId, pageRequest);

            return new ApiResponse(
                    getCommentaryDtoList(commentaryPage),
                    page,
                    size,
                    commentaryPage.getTotalPages(),
                    commentaryPage.getTotalElements()
            );
        } catch (Exception e) {
            return ResponseUtils.errorPageable();
        }
    }

    public ApiResponse writeComment(User user, CommentaryDto commentaryDto) {
        try {
            if (user == null || commentaryDto.getProductId() != null) {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
            Commentary commentary = new Commentary();

            Optional<Product> optionalProduct = productRepository.findById(commentaryDto.getProductId());
            if (optionalProduct.isPresent()) {
                commentary.setProduct(optionalProduct.get());
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }

            commentary.setUser(user);
            commentary.setMessage(commentaryDto.getMessage());

            if (commentaryDto.getCommentId() != null) {
                Optional<Commentary> optionalCommentary = commentaryRepository.findById(commentaryDto.getCommentId());
                if (optionalCommentary.isPresent()) {
                    commentary.setReply(optionalCommentary.get());
                } else {
                    return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
                }
            }

            commentaryRepository.save(commentary);
            return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.saved"));
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public List<CommentaryDto> getCommentaryDtoList(Page<Commentary> commentaryPage) {
        List<CommentaryDto> commentaryDtoList = new ArrayList<>();
        for (Commentary commentary : commentaryPage) {
            commentaryDtoList.add(getCommentaryDto(commentary));
        }
        return commentaryDtoList;
    }

    public CommentaryDto getCommentaryDto(Commentary commentary) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Timestamp createdAt = commentary.getCreatedAt();
        String formattedDate = simpleDateFormat.format(createdAt.getDate());
        return new CommentaryDto(
                commentary.getId(),
                commentary.getMessage(),
                commentary.getUser().getFirstName() + " " + commentary.getUser().getLastName(),
                formattedDate
        );
    }
}
