package pe.agro.userservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.agro.userservice.dto.UserRequestDTO;
import pe.agro.userservice.dto.UserResponseDTO;
import pe.agro.userservice.dto.UserUpdateDTO;
import pe.agro.userservice.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRest {

    private final UserService userService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO request) {
        return userService.save(request);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> update(@Valid @RequestBody UserUpdateDTO request) {
        return userService.update(request);
    }

    @PatchMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Map<String, Object>> delete(@PathVariable Long id) {
        return userService.deleteById(id)
                .then(Mono.just(Map.of(
                        "message", "User deactivated successfully",
                        "id", id,
                        "status", "SUCCESS"
                )));
    }

    @PatchMapping("/restore/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> restore(@PathVariable Long id) {
        return userService.restore(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserResponseDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/status/active")
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserResponseDTO> findAllActive() {
        return userService.findAllActive();
    }

    @GetMapping("/status/inactive")
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserResponseDTO> findAllInactive() {
        return userService.findAllInactive();
    }

    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @PostMapping("/verify/{token}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> verifyEmail(@PathVariable String token) {
        return userService.verifyEmail(token);
    }

    @GetMapping("/check-email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> isEmailAvailable(@PathVariable String email) {
        return userService.isEmailAvailable(email);
    }

    @GetMapping("/check-phone/{phone}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> isPhoneAvailable(@PathVariable String phone) {
        return userService.isPhoneAvailable(phone);
    }

    @PutMapping("/change-role/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> changeRole(@PathVariable Long userId, @RequestParam String role) {
        return userService.changeRole(userId, role);
    }
}