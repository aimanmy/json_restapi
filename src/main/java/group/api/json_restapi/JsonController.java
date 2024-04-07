package group.api.json_restapi;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.api.json_restapi.model.Json;
import group.api.json_restapi.respository.JsonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class JsonController {

    private final JsonRepository jsonRepository;
    private final RestTemplate restJson;

    private static final String JSON_PLACEHOLDER_URL = "https://jsonplaceholder.typicode.com/posts";
    private static final Logger LOG = LoggerFactory.getLogger(JsonController.class);

    private void getLatestData() {
        Json[] jsonRes = restJson.getForObject(JSON_PLACEHOLDER_URL, Json[].class);
        if (jsonRes != null) {
            jsonRepository.deleteAll();
            jsonRepository.saveAll(Arrays.asList(jsonRes));
            LOG.info("---Fetch Latest Data Success---.");
        } else {
            LOG.error("---Error JSON_PLACEHOLDER_URL---");
        }
    }

    @Autowired
    public JsonController(JsonRepository jsonRepository, RestTemplate restJson) {
        this.jsonRepository = jsonRepository;
        this.restJson = restJson;
    }

    @GetMapping("/jsons")
    public List<Json> getFormatJson() {
        getLatestData();
        Optional<Json> jsonLongestTitle = jsonRepository.findAll().stream().max(Comparator.comparing(json -> json.getTitle().length()));
        jsonLongestTitle.ifPresent(longestTitle -> LOG.info("Longest Title --> Title: {}, Body: {}", longestTitle.getTitle(), longestTitle.getBody()));

        List<Json> formatJsonRes = jsonRepository.findAll();
        formatJsonRes.forEach(json -> json.setTitleLength(json.getTitle().length()));

        return formatJsonRes;
    }

    @GetMapping("/jsons/longesttitle")
    public Json getJsonLongestTitle() {
        List<Json> jsonRes = jsonRepository.findAll();
        Json longestTitle = jsonRes.stream().max(Comparator.comparingInt(json -> json.getTitle().length())).orElse(null);
        LOG.info("Longest Title --> Title: {}, Body: {}", longestTitle.getTitle(), longestTitle.getBody());
        return longestTitle;
    }
}
