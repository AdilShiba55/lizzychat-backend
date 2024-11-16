package chat.flirtbackend.entity;

import chat.flirtbackend.dto.Archiveable;
import chat.flirtbackend.dto.Nameable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "faq")
public class Faq implements Nameable, Archiveable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(updatable = false)
    private Date dtCreate;
    private Date dtArchive;

    public Faq(String name, String description, Date dtCreate) {
        this.name = name;
        this.description = description;
        this.dtCreate = dtCreate;
    }
}
