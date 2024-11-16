package chat.flirtbackend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "usr_password_reset")
public class UserPasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Long userId;
    @Column(updatable = false)
    private Date dtCreate;
    private Date dtConfirmed;

    public UserPasswordReset(String code, Long userId, Date dtCreate) {
        this.code = code;
        this.userId = userId;
        this.dtCreate = dtCreate;
    }
}
