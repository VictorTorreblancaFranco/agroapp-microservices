package pe.agro.sowingservice.mapper;

import org.springframework.stereotype.Component;
import pe.agro.sowingservice.dto.LossRequestDTO;
import pe.agro.sowingservice.dto.LossResponseDTO;
import pe.agro.sowingservice.model.Loss;
import java.time.LocalDateTime;

@Component
public class LossMapper {

    public Loss toEntity(LossRequestDTO request) {
        return Loss.builder()
                .date(request.getDate())
                .quantity(request.getQuantity())
                .cause(request.getCause())
                .description(request.getDescription())
                .sowingId(request.getSowingId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public LossResponseDTO toResponseDTO(Loss loss) {
        return LossResponseDTO.builder()
                .id(loss.getId())
                .date(loss.getDate())
                .quantity(loss.getQuantity())
                .cause(loss.getCause())
                .description(loss.getDescription())
                .sowingId(loss.getSowingId())
                .createdAt(loss.getCreatedAt())
                .build();
    }
}