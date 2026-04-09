package pe.agro.userservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO request) {
        return userService.save(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> update(@Valid @RequestBody UserUpdateDTO request) {
        return userService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return userService.deleteById(id);
    }

    @PatchMapping("/{id}/restore")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> restore(@PathVariable Long id) {
        return userService.restore(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserResponseDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserResponseDTO> findAllActive() {
        return userService.findAllActive();
    }

    @GetMapping("/inactive")
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserResponseDTO> findAllInactive() {
        return userService.findAllInactive();
    }

    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
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

    @PutMapping("/{id}/role")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDTO> changeRole(@PathVariable Long id, @RequestParam String role) {
        return userService.changeRole(id, role);
    }
}