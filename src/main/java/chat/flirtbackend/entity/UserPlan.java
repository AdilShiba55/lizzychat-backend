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
@Table(name = "usr_plan")
public class UserPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long planId;
    private Date dtStart;
    private Date dtEnd;
    private Long paymentId;

    public UserPlan(Long userId, Long planId, Date dtStart, Date dtEnd, Long paymentId) {
        this.userId = userId;
        this.planId = planId;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
        this.paymentId = paymentId;
    }
}
