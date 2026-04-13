package pe.agro.sowingservice.mapper;

import org.springframework.stereotype.Component;
import pe.agro.sowingservice.dto.CostRequestDTO;
import pe.agro.sowingservice.dto.CostResponseDTO;
import pe.agro.sowingservice.model.Cost;
import java.time.LocalDateTime;

@Component
public class CostMapper {

    public Cost toEntity(CostRequestDTO request) {
        return Cost.builder()
                .description(request.getDescription())
                .type(request.getType())
                .amount(request.getAmount())
                .date(request.getDate())
                .paymentMethod(request.getPaymentMethod())
                .sowingId(request.getSowingId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public CostResponseDTO toResponseDTO(Cost cost) {
        return CostResponseDTO.builder()
                .id(cost.getId())
                .description(cost.getDescription())
                .type(cost.getType())
                .amount(cost.getAmount())
                .date(cost.getDate())
                .paymentMethod(cost.getPaymentMethod())
                .sowingId(cost.getSowingId())
                .createdAt(cost.getCreatedAt())
                .build();
    }
}