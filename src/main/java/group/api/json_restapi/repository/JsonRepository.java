package group.api.json_restapi.repository;

import group.api.json_restapi.model.Json;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JsonRepository extends JpaRepository<Json, Long> {
}
