package dev.turkmall.onlineshopserver.controller;

import dev.turkmall.onlineshopserver.entity.User;
import dev.turkmall.onlineshopserver.payload.ApiResponse;
import dev.turkmall.onlineshopserver.payload.ResToken;
import dev.turkmall.onlineshopserver.payload.UserDto;
import dev.turkmall.onlineshopserver.repository.UserRepository;
import dev.turkmall.onlineshopserver.security.CurrentUser;
import dev.turkmall.onlineshopserver.security.JwtTokenProvider;
import dev.turkmall.onlineshopserver.service.AuthService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;


    @ApiOperation(value = "Tizimga kirish uchun")
    @PostMapping("/signIn")
    public HttpEntity<?> login(@RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDto.getPhoneNumber(),
                userDto.getPassword()
        ));
        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new ResToken(token));
    }

    @ApiOperation(value = "Ro'yxatdan o'tish uchun")
    @PostMapping("/signUp")
    public HttpEntity<?> register(@RequestBody UserDto userDto) {
        ApiResponse apiResponse = authService.register(userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "ADMIN sotuvchini ro'yxatdan o'tkazadi")
    @PostMapping("/registerSeller")
    public HttpEntity<?> registerSeller(@CurrentUser User user, @RequestBody UserDto userDto) {
        ApiResponse apiResponse = authService.registerSeller(user, userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Token bilan request kelsa agar token haqiqiy bo'lsa user qatadi")
    @GetMapping("/user-me")
    public HttpEntity<?> userMe(@CurrentUser User user) {
        UserDto userDto = new UserDto();
        if (user != null) {
            userDto = authService.userMe(user);
        }
        return ResponseEntity.status(user != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(userDto);
    }

    @ApiOperation(value = "ADMIN userlarni olishi mumkin {sort} user yoki seller bo'lishi mumkin agar berilmasa hammasi olinadi")
    @GetMapping("/get-users")
    public HttpEntity<?> getAllUsers(
            @CurrentUser User user,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", defaultValue = "all") String sort) {
        ApiResponse allUsers = authService.getAllUsers(page, size, sort, user);
        return ResponseEntity.ok(allUsers);
    }

    @ApiOperation(value = "ADMIN userlarni qidirishi mumkin {status} user yoki seller bo'lishi mumkin agar berilmasa hammasi olinadi")
    @GetMapping("/search-users")
    public HttpEntity<?> search(
            @CurrentUser User user,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "str") String str,
            @RequestParam(name = "status") String status) {
        ApiResponse searchUser = authService.searchUser(page, size, str, status, user);
        return ResponseEntity.ok(searchUser);
    }

    @ApiOperation(value = "User o'zini tahrirlashi mumkin")
    @PutMapping("/edit/{id}")
    public HttpEntity<?> editUser(
            @PathVariable UUID id,
            @RequestBody UserDto userDto
    ) {
        ApiResponse apiResponse = authService.editUser(id, userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "User o'zini parolini tahrirlashi mumkin")
    @PutMapping("/edit/password/{id}")
    public HttpEntity<?> editPassword(
            @PathVariable UUID id,
            @RequestBody UserDto userDto
    ) {
        ApiResponse apiResponse = authService.editPassword(id, userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @ApiOperation(value = "Register jarayonida telefon raqam bazada bor yoki yo'qligini tekshiradi")
    @GetMapping("/checkPhone")
    public HttpEntity<?> checkUserName(@RequestParam String phone) {
        boolean existPhone = userRepository.existsByPhoneNumber(phone);
        return ResponseEntity.ok(existPhone);
    }

    @ApiOperation(value = "ADMIN userni o'chirib qo'yishi mumkin yoki aksi")
    @GetMapping("/active/{id}")
    public HttpEntity<?> enabledUser(
            @CurrentUser User user,
            @PathVariable UUID id,
            @RequestParam(name = "enabled") boolean enabled
    ) {
        ApiResponse apiResponse = authService.enabledUser(id, enabled, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(apiResponse);
    }
}
