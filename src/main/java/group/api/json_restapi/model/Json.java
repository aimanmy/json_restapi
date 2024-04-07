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
