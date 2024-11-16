package chat.flirtbackend.service;

import chat.flirtbackend.entity.CharacterImageType;
import chat.flirtbackend.entity.CharacterType;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.CharacterImageRepository;
import chat.flirtbackend.repository.CharacterImageTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class CharacterImageTypeService {

    private final CharacterImageTypeRepository characterImageTypeRepository;

    public CharacterImageTypeService(CharacterImageTypeRepository characterImageTypeRepository) {
        this.characterImageTypeRepository = characterImageTypeRepository;
    }

    public CharacterImageType findById(Long id) {
        return characterImageTypeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(CharacterImageType.class, id));
    }
}
