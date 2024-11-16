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
@Table(name = "location")
public class Location implements Nameable, Archiveable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date dtCreate;
    private Date dtArchive;
}
