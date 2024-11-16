package chat.flirtbackend.dto;

import chat.flirtbackend.entity.Character;
import chat.flirtbackend.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMessagesResponseDTO {

    private CharacterPublicDTO character;
    private SearchResponse<MessageChatItem> content;
}
