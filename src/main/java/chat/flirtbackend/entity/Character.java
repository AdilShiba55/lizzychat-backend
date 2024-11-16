package chat.flirtbackend.entity;

import chat.flirtbackend.dto.Archiveable;
import chat.flirtbackend.dto.Nameable;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "character")
public class Character implements Nameable, Archiveable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long age;
    private Long cost;
    private Long typeId;
    @Column(updatable = false)
    private Date dtCreate;
    private Date dtArchive;
    private Long genderId;
    private Long hairColorId;
    private Long eyesColorId;
    private Long breastSizeId;
    private Long ethnicityId;
    private Long hairStyleId;
    private Long ageLookId;
    private Long bodyTypeId;
    private Long raceId;
    private Long occupationId;
    private Long personalityId;
    private Long userId;
    private Boolean pregnant;
    private Boolean withGlasses;
    private Long clothesColorId;
    private Long dayTimeId;
    private Date dtReference;
}
