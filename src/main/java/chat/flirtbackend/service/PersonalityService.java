package chat.flirtbackend.service;

import chat.flirtbackend.entity.CharacterImage;
import chat.flirtbackend.entity.Personality;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.PersonalityRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonalityService {

    private final PersonalityRepository personalityRepository;

    public PersonalityService(PersonalityRepository personalityRepository) {
        this.personalityRepository = personalityRepository;
    }

    public Personality findById(Long id) {
        return personalityRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Personality.class, id));
    }
}
