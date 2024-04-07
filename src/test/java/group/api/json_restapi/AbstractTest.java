package group.api.json_restapi;

import java.io.IOException;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JsonRestApiApplication.class)
@WebAppConfiguration
public abstract class AbstractTest {
    protected MockMvc jsonMvc;
    @Autowired
    WebApplicationContext webJsonContext;

    protected void setUp() {
        jsonMvc = MockMvcBuilders.webAppContextSetup(webJsonContext).build();
    }

    protected <T> T mapJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.readValue(json, clazz);
    }
}
