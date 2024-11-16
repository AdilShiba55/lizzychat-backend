package chat.flirtbackend.mapper;

import chat.flirtbackend.dto.UserPlanInfoDTO;
import chat.flirtbackend.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserPlanInfoMapper implements RowMapper<UserPlanInfoDTO> {
    @Override
    public UserPlanInfoDTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long planId = resultSet.getLong("planId");
        String planName = resultSet.getString("planName");
        Long textMessageLimit = resultSet.getLong("textMessageLimit");
        Long imageMessageLimit = resultSet.getLong("imageMessageLimit");
        Long userId = resultSet.getLong("userId");
        Date dtStart = resultSet.getDate("dtStart");
        Date dtEnd = resultSet.getDate("dtEnd");
        Long paymentId = resultSet.getLong("paymentId");
        return UserPlanInfoDTO.builder()
                .planId(planId)
                .planName(planName)
                .userId(userId)
                .dtStart(dtStart)
                .dtEnd(dtEnd)
                .paymentId(paymentId)
                .textMessageLimit(textMessageLimit)
                .imageMessageLimit(imageMessageLimit)
                .build();
    }
}
