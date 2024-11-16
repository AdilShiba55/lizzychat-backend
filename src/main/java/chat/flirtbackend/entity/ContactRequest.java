package chat.flirtbackend.entity;

import chat.flirtbackend.dto.Archiveable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "contact_request")
public class ContactRequest implements Archiveable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private Long userId;
    @Column(updatable = false)
    private Date dtCreate;
    private Date dtArchive;

    public ContactRequest(String text, Long userId, Date dtCreate) {
        this.text = text;
        this.userId = userId;
        this.dtCreate = dtCreate;
    }
}
