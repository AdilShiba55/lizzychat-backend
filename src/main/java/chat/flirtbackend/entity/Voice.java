package chat.flirtbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "voice")
public class Voice {
    @Id
    private String id;
    private Date dtCreate;
}
