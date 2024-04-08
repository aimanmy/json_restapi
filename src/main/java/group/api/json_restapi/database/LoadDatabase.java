package group.api.json_restapi.database;

import group.api.json_restapi.repository.JsonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class LoadDatabase {
    private final JsonRepository jsonRepository;

    public LoadDatabase(JsonRepository jsonRepository) {
        this.jsonRepository = jsonRepository;
    }

    @Bean
    public RestTemplate restJson() {
        return new RestTemplate();
    }

}
