package chat.flirtbackend.mapper;

import chat.flirtbackend.dto.CharacterChatItemDTO;
import chat.flirtbackend.service.MinioService;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

public class CharacterChatItemMapper implements RowMapper<CharacterChatItemDTO> {

    @Override
    @SneakyThrows
    public CharacterChatItemDTO mapRow(ResultSet resultSet, int rowNum) {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String text = resultSet.getString("text");
        String avatarPath = resultSet.getString("avatarPath");
        Long characterImageId = resultSet.getLong("characterImageId");
//        String avatarUrl = minioService.getObjectFromCharacterBucket(avatarPath);
        CharacterChatItemDTO characterChatItem = new CharacterChatItemDTO();
        characterChatItem.setId(id);
        characterChatItem.setName(name);
        characterChatItem.setText(text);
        characterChatItem.setAvatarPath(avatarPath);
        characterChatItem.setCharacterImageId(characterImageId);
        return characterChatItem;
    }
}
