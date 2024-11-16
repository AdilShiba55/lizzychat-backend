package chat.flirtbackend.entity;

import chat.flirtbackend.dto.Archiveable;
import chat.flirtbackend.dto.Nameable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "image_generation")
public class ImageGeneration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long seed;
    private Double cost;
    @Column(updatable = false)
    private Date dtCreate;
    private String prompt;
}
