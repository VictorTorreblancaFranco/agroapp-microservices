package pe.agro.weatherservice.mapper;

import org.springframework.stereotype.Component;
import pe.agro.weatherservice.dto.WeatherRequestDTO;
import pe.agro.weatherservice.dto.WeatherResponseDTO;
import pe.agro.weatherservice.model.Weather;
import java.time.LocalDateTime;

@Component
public class WeatherMapper {

    public Weather toEntity(WeatherRequestDTO request) {
        return Weather.builder()
                .date(request.getDate())
                .temperature(request.getTemperature())
                .humidity(request.getHumidity())
                .rainfall(request.getRainfall())
                .wind(request.getWind())
                .plotId(request.getPlotId())
                .recordedAt(LocalDateTime.now())
                .build();
    }

    public WeatherResponseDTO toResponseDTO(Weather weather) {
        return WeatherResponseDTO.builder()
                .id(weather.getId())
                .date(weather.getDate())
                .temperature(weather.getTemperature())
                .humidity(weather.getHumidity())
                .rainfall(weather.getRainfall())
                .wind(weather.getWind())
                .plotId(weather.getPlotId())
                .recordedAt(weather.getRecordedAt())
                .build();
    }
}