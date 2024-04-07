package group.api.json_restapi;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.api.json_restapi.respository.JsonRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import group.api.json_restapi.model.Json;

public class JsonControllerTest extends AbstractTest {

    private JsonRepository jsonRepositoryMock;
    private static final Logger LOG = LoggerFactory.getLogger(JsonController.class);

    @Override
    @Before
    public void setUp() {
        super.setUp();
        jsonRepositoryMock = mock(JsonRepository.class);
    }

    @Test
    public void testConnection() throws Exception {
        String uri = "/jsons";
        MvcResult jsonMvcResult = jsonMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = jsonMvcResult.getResponse().getStatus();
        assertEquals(200, status);
        LOG.info("###Test Connection To Response URL \"https://jsonplaceholder.typicode.com/posts\" Success###");
    }

    @Test
    public void testLongestTitle() throws JsonProcessingException {
        JsonRepository jsonRepositoryMock = mock(JsonRepository.class);

        Json json1 = new Json(1L, "Short title", "Body 1");
        Json json2 = new Json(2L, "Longer title", "Body 2");
        Json json3 = new Json(3L, "The longest title", "Body 3");

        List<Json> mockJson = new ArrayList<>();
        mockJson.add(json1);
        mockJson.add(json2);
        mockJson.add(json3);

        when(jsonRepositoryMock.findAll()).thenReturn(mockJson);
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        when(objectMapperMock.writeValueAsString(json3)).thenReturn("{\"id\":3,\"title\":\"The longest title\",\"body\":\"Body 3\"}");

        JsonController jsonController = new JsonController(jsonRepositoryMock, null);
        Json longestTitleJson = jsonController.getJsonLongestTitle();
        assertEquals(json3.getTitle(), longestTitleJson.getTitle());
        LOG.info("###Test Logic Longest Title Success###");
    }

    @Test
    public void testGetFormatJson() throws Exception {
        String uri = "/jsons";
        MvcResult jsonMvcResult = jsonMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        String jsonRes = jsonMvcResult.getResponse().getContentAsString();
        Json[] json = super.mapJson(jsonRes, Json[].class);

        //Assuming the json length is the same when testing
        assertEquals(74, json[0].getTitleLength());
        assertEquals(12, json[1].getTitleLength());
        LOG.info("###Test Get Format Json Success###");
    }
}
