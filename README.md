# Rest API Using Spring Boot

1. [Objectives](#objectives)
2. [How To Build Spring Boot?](#how-to-build-spring-boot)
3. [PLEASE TAKE NOTE](#please-take-note)
4. [Coding Rest API Spring Boot](#coding-rest-api-spring-boot)
5. [Run the Rest API Spring Boot](#run-the-rest-api-spring-boot)
6. [Spring Boot JSON REST API Unit Tests](#spring-boot-json-rest-api-unit-tests)
7. [Project Summary](#project-summary)

## Objectives:
1. Create a Spring Boot REST API that connects to the JSONPlaceholder API (https://jsonplaceholder.typicode.com/) and retrieves a list of "posts" from the endpoint "https://jsonplaceholder.typicode.com/posts".
2. Implement a logic in the code to find the post with the longest title and display that post's title and body in the console.
3. Implement a feature to change the format of the JSON response of all the posts. The new format should have an additional field "titleLength" that displays the length of the title for each post.
4. Write unit tests for all the implemented features
5. Each time the application URL is accessed, the latest data must be served in the response.

**The application is created using Intellij java SDK 22 (Java 17)**
## How To Build Spring Boot?

Before we start, this was the key elements that needed to prepare:- 
* Intellij IDE
* Oracle JDK 22 (Use Java 17)

Create Project Spring Boot rest_api.
1. For Intellij, directly create new project within the IDE and select Spring Initilizr
    * Select `Java 17` of JDK Oracle `OpenJDK 22`
    * Select `Language: Java` & `Type: Maven` for this project
    * `Name`, `Group` or `Pachage `name created based on `json_restapi `(follow what you prefer)
    * Then create the project
  
![1](https://github.com/aimanmy/json_restapi/assets/77477805/4187536c-0bcc-45a4-926d-5a991398e068)

2. Use Spring Boot version `3.2.4` and search the dependency and add this required dependency:-
   * `Spring Web`
   * `Spring Data JPA`
   * `H2 Database`
   
If you want to create the project by import, can go the official website [Spring Initializr](https://start.spring.io) to create one.
     
![2](https://github.com/aimanmy/json_restapi/assets/77477805/e4f9ace4-0ce3-4266-be65-902d6c5ae23b)


3. Now we create a **Package Structure**

The project follows a structured package organization:

- **group.api.json_restapi**: This is the Root package.
- **../model**: Contains the entity class representing JSON data.
- **../repository**: Holds the repository interface for CRUD operations on JSON entities.
- **../database**: Contains configuration related to database setup.
- **../controller**: Houses the REST controller responsible for handling HTTP requests.

![3](https://github.com/aimanmy/json_restapi/assets/77477805/436d6e6f-b5f6-42b0-82f2-9bea54d63c4b)

It will look like this, now next we will start the coding for the rest of the package.

## PLEASE TAKE NOTE
*Before starting the coding, from the Github file, make sure to `copy` the content of `pom.xml` to load maven dependencies (can be found within the root folder) & `copy` the content of `application.properties` (can be found in resources).* 

## Coding Rest API Spring Boot
This project implements a JSON REST API using Spring Boot, fetching data from the JsonPlaceholde/post, processing it, and providing endpoints to retrieve formatted JSON data and find entries with the longest titles.

### `JsonRestApiApplication.java`
- Create the class within the `root` package.
- This class serves as the entry point for the Spring Boot application.
- It starts the Spring application context and initializes the embedded Tomcat server.

```java
package group.api.json_restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JsonRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonRestApiApplication.class, args);
    }

}
```

### `Json.java`
- Create the class within the `model` package.
- Represents JSON data retrieved from an external API.
- Provides methods for accessing and manipulating JSON properties.
- Calculates the length of the title and exposes it through a custom property `titleLength`.

```java
package group.api.json_restapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Json {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private Long userId;
   private String title;
   private String body;

   private int titleLength;

   public Json() {
   }

   public Json(Long userId, String title, String body) {
      this.userId = userId;
      this.title = title;
      this.body = body;
   }

   public Long getId() {
      return this.id;
   }

   public Long getUserId() {
      return this.userId;
   }

   public String getTitle() {
      return this.title;
   }

   public String getBody() {
      return this.body;
   }

   @JsonProperty("titleLength")
   public int getTitleLength() {
      return this.title != null ? this.title.length() : 0;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setBody(String body) {
      this.body = body;
   }

   public void setTitleLength(int titleLength) {
      this.titleLength = titleLength;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Json)) return false;
      Json json = (Json) o;
      return Objects.equals(this.id, json.id) && Objects.equals(this.userId, json.userId) && Objects.equals(this.title, json.title) && Objects.equals(this.body, json.body);
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.id, this.userId, this.title, this.body);
   }
}

```


### `JsonRepository.java`
- Create the class within the `repository` package.
- A class interface extending `JpaRepository` to handle custom CRUD operations for `Json` entities.
```java
package group.api.json_restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JsonRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonRestApiApplication.class, args);
    }

}
```

### `LoadDatabase.java`
- Create the class within the `database` package.
- Configuration class defining a bean of type `RestTemplate` for HTTP communication.
- Injects `JsonRepository` to interact with the database.

```java
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
```
### `JsonController.java`
- Create the class within the `controller` package.
- REST controller handling HTTP requests related to JSON data.
- Fetches JSON data from an external API and stores it in the database.
- Provides endpoints to retrieve formatted JSON data and find entries with the longest titles.
-  `getLatestData` is function that fetch a new data when url is accessed again.
-  `getFormatJson` is an update that format the json received with a new feature response with `titleLength`.
-  `getJsonLongestTitle` is a logic to find the longest title.

```java
package group.api.json_restapi.controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import group.api.json_restapi.model.Json;
import group.api.json_restapi.repository.JsonRepository;
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
```
## Run the Rest API Spring Boot
* Build the `json_restapi` project and then run `JsonRestApiApplication` class, if you follow all the instruction, it should be a success and the Spring Boot console will log like this:-

![4](https://github.com/aimanmy/json_restapi/assets/77477805/dc67dd3a-566a-478b-9d58-dd8574f3ad07)

* Now we have a RESTAPI that runs locally within our computer, to check the JSON response, access this URL: http://localhost:8080/jsons

* You will see it like this, the JSON response from `JsonPlaceholder/post` will be logged with a `formatted JSON` that include `titleLength`:-

![5](https://github.com/aimanmy/json_restapi/assets/77477805/ca22d02b-246c-4f6b-be00-9b45a6911d4f)

* Alongside of that, within the console, a log of `title` and `body` will be prompted to show the longest title based on the `JSON` response.

![6](https://github.com/aimanmy/json_restapi/assets/77477805/e6d778a1-cc2d-4513-8651-cca135448ba4)

* If you reload the page, the id will be in incremented as it update the `Json` response with the latest data.
* The log of `Fetch Latest Data Success` is a logging for everytime the URL: (http://localhost:8080/jsons) has been accessed as it will put the latest data in `JSON` response.
* Same goes to the `Longest Title` it will log at the console based on latest data

![7](https://github.com/aimanmy/json_restapi/assets/77477805/d2ca8d06-f559-4faf-a3e7-16f40f4006e0)

![8](https://github.com/aimanmy/json_restapi/assets/77477805/13651aca-22b2-42f6-8ec7-e1ff8b743f60)

* Based on the URL, if add `/longesttitle` after the `/jsons`, a  `JSON` response of the longestitle will be logged. URL: http://localhost:8080/jsons/longesttile.
* The `Longest Title` will display their `JSON` based on the latest data.

![9](https://github.com/aimanmy/json_restapi/assets/77477805/f4f9c78d-62b8-4853-9cb4-f6da2cdef7fb)

# Spring Boot JSON REST API Unit Tests

This project includes unit tests for the JSON REST API implemented using Spring Boot.

## AbstractTest Class

### Functionality

- `AbstractTest` is an abstract class serving as the base class for unit tests.
- It provides setup methods and utility functions to facilitate unit testing.
- The `setUp` method initializes the environment for testing by setting up a `MockMvc` instance.
```java
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
```

## JsonControllerTest Class

### Functionality

- `JsonControllerTest` contains unit tests for the `JsonController` class.
- It tests various functionalities of the JSON REST API endpoints.

```java
package group.api.json_restapi;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.api.json_restapi.controller.JsonController;
import group.api.json_restapi.repository.JsonRepository;
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
```

### Test Functions
* To test the code of the unit test, make sure to run java class file `JsonControllerTest`, then the unit test will run the functions.
* The console will do the unit test, then it will log like this:-

![10](https://github.com/aimanmy/json_restapi/assets/77477805/b51d0472-e522-42ee-b5c6-2de9b9059797)

### The Console Cross-Check Unit Test 
1. `testConnection`
   - Tests the connection to the API endpoint.
   - Sends a GET request to the `/jsons` endpoint and checks if the response status is 200 (OK).
   - Logs success message if the connection is established.

![11](https://github.com/aimanmy/json_restapi/assets/77477805/ac698d33-bd46-43cb-8b53-513f3ce32946)

2. `testLongestTitle`
   - Tests the logic to find the entry with the longest title.
   - Creates mock JSON data with different title lengths.
   - Mocks the behavior of the `JsonRepository` to return the mock data.
   - Calls the `getJsonLongestTitle` method of `JsonController` and asserts that the longest title is returned.

![12](https://github.com/aimanmy/json_restapi/assets/77477805/a42c77e0-71b1-4720-a643-86f388375ea1)

3. `testGetFormatJson`
   - Tests the feature to retrieve formatted JSON data.
   - Sends a GET request to the `/jsons` endpoint and checks if the returned JSON data is formatted correctly.
   - Asserts that the title length of each JSON entry is calculated correctly.
   - Logs success message if the formatted JSON data is retrieved successfully.

![13](https://github.com/aimanmy/json_restapi/assets/77477805/767c991e-623c-47d8-b8a7-1af19af89f2e)

# Project Summary
The Spring Boot REST API project achieves the following objectives:

## Connects to JSONPlaceholder API:
* The application successfully connects to the JSONPlaceholder API endpoint (JSONPlaceholder API) and retrieves a list of "posts".

## Logic for Longest Title:
* The code implements logic to find the post with the longest title.
* It calculates the length of each post's title and finds the one with the maximum length.
* The title and body of the post with the longest title are displayed in the console.

## JSON Response Formatting:
* The API provides a feature to change the format of the JSON response for all the posts.
* A new field titleLength is added to the response, displaying the length of the title for each post.

## Unit Tests:
* Unit tests are implemented to ensure the correctness of the functionalities.
* The AbstractTest class provides setup methods and utility functions for unit testing.
* The JsonControllerTest class contains tests for the JsonController class.
* Three test functions (testConnection, testLongestTitle, and testGetFormatJson) are written to verify different aspects of the API's functionality.
* The tests verify the connection to the API endpoint, the logic for finding the longest title, and the formatting of the JSON response.

## Dynamic Data Retrieval:
* Each time the application URL is accessed, the latest data is served in the response.
* The application fetches the latest data from the JSONPlaceholder API endpoint to ensure the response contains up-to-date information.
* This project demonstrates the successful implementation of a Spring Boot REST API with functionalities to interact with JSON data effectively.
