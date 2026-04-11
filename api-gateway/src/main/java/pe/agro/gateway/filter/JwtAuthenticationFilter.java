package pe.agro.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pe.agro.gateway.client.UserServiceClient;
import pe.agro.gateway.dto.LoginRequestDTO;
import pe.agro.gateway.dto.LoginResponseDTO;
import pe.agro.gateway.security.JwtUtil;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceClient userServiceClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String LOGIN_PATH = "/api/v1/auth/login";
    private static final String REGISTER_PATH = "/api/v1/users/save";

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String requestId = UUID.randomUUID().toString();
            String path = exchange.getRequest().getURI().getPath();

            log.info("[{}] === NUEVA PETICIÓN ===", requestId);
            log.info("[{}] Path: {}", requestId, path);

            // RUTA DE LOGIN
            if (LOGIN_PATH.equals(path)) {
                log.info("[{}] Procesando login...", requestId);
                return handleLogin(exchange, chain, requestId);
            }

            // RUTAS PÚBLICAS
            if (path.startsWith(REGISTER_PATH)) {
                log.info("[{}] Ruta pública: {}", requestId, path);
                return chain.filter(exchange);
            }

            // RUTAS PROTEGIDAS
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("[{}] Token no encontrado para: {}", requestId, path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                log.warn("[{}] Token inválido para: {}", requestId, path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            log.info("[{}] Token válido para: {}", requestId, path);
            return chain.filter(exchange);
        };
    }

    private Mono<Void> handleLogin(ServerWebExchange exchange, GatewayFilterChain chain, String requestId) {
        log.info("[{}] Iniciando proceso de login", requestId);

        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    try {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);

                        String bodyStr = new String(bytes, StandardCharsets.UTF_8);
                        LoginRequestDTO loginRequest = objectMapper.readValue(bodyStr, LoginRequestDTO.class);
                        String username = loginRequest.getUsername();

                        log.info("[{}] Intento de login para usuario: {}", requestId, username);

                        // ✅ CORREGIDO: Llamar con 2 parámetros (sin requestId)
                        return userServiceClient.validateCredentials(username, loginRequest.getPassword())
                                .flatMap(valid -> {
                                    if (!valid) {
                                        log.warn("[{}] Credenciales inválidas", requestId);
                                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                        return exchange.getResponse().setComplete();
                                    }

                                    log.info("[{}] Credenciales válidas", requestId);

                                    // ✅ CORREGIDO: Llamar con 1 parámetro (sin requestId)
                                    return userServiceClient.getUserByUsername(username)
                                            .flatMap(user -> {
                                                try {
                                                    log.info("[{}] Usuario: {}, Roles: {}", requestId, user.getUsername(), user.getRoles());

                                                    String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());
                                                    log.info("[{}] Token generado", requestId);

                                                    LoginResponseDTO response = LoginResponseDTO.builder()
                                                            .token(token)
                                                            .type("Bearer")
                                                            .username(user.getUsername())
                                                            .roles(user.getRoles())
                                                            .build();

                                                    String jsonResponse = objectMapper.writeValueAsString(response);
                                                    DataBuffer buffer = exchange.getResponse()
                                                            .bufferFactory()
                                                            .wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));

                                                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                                    exchange.getResponse().setStatusCode(HttpStatus.OK);

                                                    log.info("[{}] Login exitoso", requestId);
                                                    return exchange.getResponse().writeWith(Mono.just(buffer));

                                                } catch (JsonProcessingException e) {
                                                    log.error("[{}] Error serializando: {}", requestId, e.getMessage());
                                                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                                                    return exchange.getResponse().setComplete();
                                                }
                                            });
                                });

                    } catch (Exception e) {
                        log.error("[{}] Error en login: {}", requestId, e.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                        return exchange.getResponse().setComplete();
                    }
                });
    }

    public static class Config {
    }
}