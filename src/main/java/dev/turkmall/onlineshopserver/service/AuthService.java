package dev.turkmall.onlineshopserver.service;

import dev.turkmall.onlineshopserver.config.MessageConfig;
import dev.turkmall.onlineshopserver.entity.Attachment;
import dev.turkmall.onlineshopserver.entity.Role;
import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.entity.enums.RoleName;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.UserDto;
import dev.turkmall.onlineshopserver.repository.AttachmentRepository;
import dev.turkmall.onlineshopserver.repository.RoleRepository;
import dev.turkmall.onlineshopserver.repository.UserRepository;
import dev.turkmall.onlineshopserver.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MessageConfig messageConfig;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    CheckUser checkUser;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }

    public ApiResponse register(UserDto userDto) {
        try {
            if (userDto.getPassword().equals(userDto.getPrePassword())) {
                if (!userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
                    User user = new User();
                    user.setRoles(Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_USER)));
                    user.setPhoneNumber(userDto.getPhoneNumber());
                    user.setPassword(passwordEncoder.encode(userDto.getPassword()));

                    user.setEnabled(true);
                    userRepository.save(user);
                    return ResponseUtils.success(messageConfig.getMessageByLanguage("auth.success"));
                } else {
                    return ResponseUtils.success(messageConfig.getMessageByLanguage("phone.number.error"));
                }
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("password.not.equals"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse registerSeller(User currentUser, UserDto userDto) {
        try {
            if (checkUser.isAdmin(currentUser)) {
                if (userDto.getPassword().equals(userDto.getPrePassword())) {
                    if (!userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
                        User user = new User();

                        user.setFirstName(userDto.getFirstName());
                        user.setLastName(userDto.getLastName());
                        user.setEmail(userDto.getEmail());
                        user.setRoles(Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_SELLER)));
                        user.setPhoneNumber(userDto.getPhoneNumber());
                        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                        user.setEnabled(true);

                        userRepository.save(user);
                        return ResponseUtils.success(messageConfig.getMessageByLanguage("auth.success"));
                    } else {
                        return ResponseUtils.error(messageConfig.getMessageByLanguage("phone.number.error"));
                    }
                } else {
                    return ResponseUtils.error(messageConfig.getMessageByLanguage("password.not.equals"));
                }
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.permission"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse getAllUsers(Integer page, Integer size, String role, User user) {
        try {
            if (checkUser.isAdmin(user)) {
                Page<User> userPage = null;
                PageRequest pageRequest = PageRequest.of(page, size, Sort.by("first_name"));

                switch (role.toLowerCase()) {
                    case "user":
                        userPage = userRepository.findAllByRoleName(pageRequest, "ROLE_USER");
                        break;
                    case "seller":
                        userPage = userRepository.findAllByRoleName(pageRequest, "ROLE_SELLER");
                        break;
                    default:
                        userPage = userRepository.findAll(pageRequest, "ROLE_ADMIN");
                        break;
                }

                return new ApiResponse(
                        userPage.getContent().stream().map(this::userMe),
                        page,
                        size,
                        userPage.getTotalPages(),
                        userPage.getTotalElements()
                );
            } else {
                return ResponseUtils.errorPageable();
            }
        } catch (Exception e) {
            return ResponseUtils.errorPageable();
        }
    }

    public UserDto userMe(User user) {
        String roleName = "";
        List<Role> roles = user.getRoles();

        if (!user.getRoles().isEmpty()) {
            for (Role role : roles) {

                switch (role.getRoleName()) {
                    case ROLE_USER:
                        roleName = "USER";
                        break;
                    case ROLE_ADMIN:
                        roleName = "ADMIN";
                        break;
                    case ROLE_SELLER:
                        roleName = "SELLER";
                        break;
                }
            }
        }
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                roleName,
                user.getPhoto() != null ? user.getPhoto().getId() : null,
                user.isEnabled()
        );
    }

    public ApiResponse editPassword(UUID id, UserDto userDto) {
        try {
            if (userDto.getNewPassword().equals(userDto.getPrePassword())) {
                Optional<User> optionalUser = userRepository.findById(id);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                        user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
                        userRepository.save(user);
                        return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.edited"));
                    } else {
                        return ResponseUtils.error(messageConfig.getMessageByLanguage("password.wrong"));
                    }
                } else {
                    return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
                }
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("password.not.equals"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse editUser(UUID id, UserDto userDto) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (userDto.getPhotoId() != null) {
                    Optional<Attachment> optionalAttachment = attachmentRepository.findById(userDto.getPhotoId());
                    optionalAttachment.ifPresent(user::setPhoto);
                }
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                user.setEmail(userDto.getEmail());

                userRepository.save(user);
                return ResponseUtils.success(messageConfig.getMessageByLanguage("successfully.edited"));
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }

    public ApiResponse searchUser(Integer page, Integer size, String search, String status, User user) {
        try {
            if (checkUser.isAdmin(user)) {
                Page<User> userPage = null;
                PageRequest pageRequest = PageRequest.of(page, size);

                String searchWord = '%' + (search.toLowerCase()) + '%';
                status = status.toLowerCase();

                switch (status) {
                    case "user":
                        userPage = userRepository.search(pageRequest, searchWord, "ROLE_USER");
                        break;
                    case "seller":
                        userPage = userRepository.search(pageRequest, searchWord, "ROLE_SELLER");
                        break;
                    default:
                        userPage = userRepository.searchWithOutAdmin(pageRequest, searchWord, "ROLE_ADMIN");
                }

                return new ApiResponse(
                        userPage.getContent().stream().map(this::userMe),
                        page,
                        size,
                        userPage.getTotalPages(),
                        userPage.getTotalElements()
                );
            } else {
                return ResponseUtils.errorPageable();
            }
        } catch (Exception e) {
            return ResponseUtils.errorPageable();
        }
    }

    public ApiResponse enabledUser(UUID userId, boolean enabled, User currentUser) {
        try {
            if (checkUser.isAdmin(currentUser)) {
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    user.setEnabled(enabled);

                    userRepository.save(user);
                    return ResponseUtils.success(messageConfig.getMessageByLanguage(enabled ? "active" : "not.active"));
                } else {
                    return ResponseUtils.error(messageConfig.getMessageByLanguage("not.found"));
                }
            } else {
                return ResponseUtils.error(messageConfig.getMessageByLanguage("not.permission"));
            }
        } catch (Exception e) {
            return ResponseUtils.error(messageConfig.getMessageByLanguage("server.error"));
        }
    }
}
