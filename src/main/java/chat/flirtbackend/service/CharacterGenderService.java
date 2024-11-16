package chat.flirtbackend.service;

import chat.flirtbackend.entity.CharacterGender;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.CharacterGenderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CharacterGenderService {

    private final CharacterGenderRepository characterGenderRepository;
    private final ConcurrentHashMap<Long, CharacterGender> characterGenderCache;

    public CharacterGenderService(CharacterGenderRepository characterGenderRepository, ConcurrentHashMap<Long, CharacterGender> characterGenderCache) {
        this.characterGenderRepository = characterGenderRepository;
        this.characterGenderCache = characterGenderCache;
    }

    public CharacterGender findById(Long id) {
        if(characterGenderCache.containsKey(id)) {
            return characterGenderCache.get(id);
        }
        return characterGenderRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(CharacterGender.class, id));
    }
}
