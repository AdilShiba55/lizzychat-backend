package chat.flirtbackend.entity;


import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Getter
@Setter
@Entity
@Table(name = "memory")
@JsonInclude(JsonInclude.Include.NON_NULL)
@TypeDef(name = "json", typeClass = JsonType.class)
public class Memory {
    @Id
    private UUID embeddingId;
    @Type(type = "json")
    @Column(columnDefinition = "vector")
    private List<Float> embedding;
    private String text;
    private Long userId;
    private Long characterId;
}
