package dev.turkmall.onlineshopserver.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentaryDto {
    private UUID id;
    private String message;
    private String username;
    private UUID productId;
    private String date;

    private UUID commentId;

    public CommentaryDto(UUID id, String message, String username, String date) {
        this.id = id;
        this.message = message;
        this.username = username;
        this.date = date;
    }
}
