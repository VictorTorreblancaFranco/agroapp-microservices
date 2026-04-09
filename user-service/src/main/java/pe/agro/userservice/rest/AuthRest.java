package pe.agro.userservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.agro.userservice.dto.AuthRequestDTO;
import pe.agro.userservice.dto.AuthResponseDTO;
import pe.agro.userservice.service.AuthService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRest {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/logout/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> logout(@PathVariable Long userId) {
        return authService.logout(userId);
    }
}