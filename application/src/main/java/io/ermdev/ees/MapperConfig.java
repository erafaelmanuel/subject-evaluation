package io.ermdev.ees;

import io.ermdev.mapfierj.core.ModelMapper;
import io.ermdev.mapfierj.core.SimpleMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public SimpleMapper simpleMapper() {
        return new SimpleMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
