package chat.flirtbackend.service;

import chat.flirtbackend.dto.UserPlanInfoDTO;
import chat.flirtbackend.entity.Plan;
import chat.flirtbackend.entity.UserPlan;
import chat.flirtbackend.mapper.UserPlanInfoMapper;
import chat.flirtbackend.repository.PlanRepository;
import chat.flirtbackend.repository.UserPlanRepository;
import chat.flirtbackend.util.UtPlan;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserPlanService {

    private final PlanRepository planRepository;
    private final UserPlanRepository userPlanRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserPlanService(PlanRepository planRepository, UserPlanRepository userPlanRepository, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.planRepository = planRepository;
        this.userPlanRepository = userPlanRepository;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    public void create(Long userId, Long planId, Integer monthCount, Long myPaymentId) {
        Date dtStart = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dtStart);
        calendar.add(Calendar.MONTH, monthCount);
        Date dtEnd = calendar.getTime();
        UserPlan userPlan = new UserPlan(userId, planId, dtStart, dtEnd, myPaymentId);
        userPlanRepository.finishPreviousPlans(userId);
        userPlanRepository.save(userPlan);
    }

    public UserPlanInfoDTO getCurrentPlan(Long userId) {
        String sql = "" +
                "select plan.id as planId, plan.name as planName, up.user_id as userId,\n" +
                "up.dt_start as dtStart, up.dt_end as dtEnd, up.payment_id as paymentId,\n" +
                "plan.text_message_limit as textMessageLimit, plan.image_message_limit as imageMessageLimit\n" +
                "from usr_plan up\n" +
                "join plan on plan.id = up.plan_id\n" +
                "where up.user_id = :userId and up.dt_end >= now()\n" +
                "order by up.id desc limit 1\n";

        List<UserPlanInfoDTO> userPlansInfo = namedParameterJdbcTemplate.query(sql, Map.of("userId", userId), new UserPlanInfoMapper());

        if(!userPlansInfo.isEmpty()) {
            return userPlansInfo.get(0);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            Plan basePlan = planRepository.findById(UtPlan.BASE).get();
            return UserPlanInfoDTO.builder()
                    .planId(basePlan.getId())
                    .planName(basePlan.getName())
                    .userId(userId)
                    .textMessageLimit(basePlan.getTextMessageLimit())
                    .imageMessageLimit(basePlan.getImageMessageLimit())
                    .dtStart(calendar.getTime())
                    .dtEnd(new Date())
                    .build();
        }
    }

    public boolean isPremium(Long userId) {
        return getCurrentPlan(userId).getPlanId().equals(UtPlan.PREMIUM);
    }

}
