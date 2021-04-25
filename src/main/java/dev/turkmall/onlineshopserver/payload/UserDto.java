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
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String prePassword;
    private String newPassword;
    private UUID attachmentId;
    private UUID photoId;
    private String role;
    private boolean enabled;


    public UserDto(UUID id, String firstName, String lastName, String email, String phoneNumber, String role, UUID photoId, boolean enabled) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.photoId = photoId;
        this.enabled = enabled;
    }
}
