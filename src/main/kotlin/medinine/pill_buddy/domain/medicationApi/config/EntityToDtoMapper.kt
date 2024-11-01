package medinine.pill_buddy.domain.medicationApi.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EntityToDtoMapper {
    @Bean
    fun modelMapper() : ModelMapper {
        return ModelMapper()
    }
}