package chat.flirtbackend.mapper;

import chat.flirtbackend.dto.MessageTextTypeIdDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class MessageTextTypeIdMapper implements RowMapper<MessageTextTypeIdDTO> {
    @Override
    public MessageTextTypeIdDTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        String text = resultSet.getString("text");
        Long typeId = resultSet.getLong("typeId");
        return new MessageTextTypeIdDTO(text, typeId);
    }
}
