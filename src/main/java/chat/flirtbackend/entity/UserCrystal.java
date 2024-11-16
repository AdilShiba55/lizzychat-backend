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
@Table(name = "usr_crystal")
public class UserCrystal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long count;
    private Long paymentId;
    @Column(updatable = false)
    private Date dtCreate;

    public UserCrystal(Long userId, Long count, Long paymentId, Date dtCreate) {
        this.userId = userId;
        this.count = count;
        this.paymentId = paymentId;
        this.dtCreate = dtCreate;
    }
}
