package chat.flirtbackend.dto;

import chat.flirtbackend.util.UtPlan;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
public class UserPlanInfoDTO {
    private Long planId;
    private String planName;
    private Long userId;
    private Date dtStart;
    private Date dtEnd;
    private Long paymentId;
    private Long textMessageLimit;
    private Long imageMessageLimit;

    public boolean isPremium() {
        return planId.equals(UtPlan.PREMIUM);
    }
}
