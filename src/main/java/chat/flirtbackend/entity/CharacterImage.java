package chat.flirtbackend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "character_image")
public class CharacterImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long characterId;
    private Long typeId;
    private String path;
    private String bluredPath;
    private Long generationId;
    private Date dtCreate;
}
