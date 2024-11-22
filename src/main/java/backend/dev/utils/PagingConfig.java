package backend.dev.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Configuration
public class PagingConfig {

    @Bean
    public Pageable defaultPageable() {
        return PageRequest.of(0, 5);
    }
}
