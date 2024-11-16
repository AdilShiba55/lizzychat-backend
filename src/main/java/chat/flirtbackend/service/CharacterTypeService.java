package chat.flirtbackend.service;

import chat.flirtbackend.entity.CharacterType;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.CharacterTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CharacterTypeService implements DictArchiveableService<CharacterType>, DictFindeableByIdService<CharacterType> {

    private final CharacterTypeRepository characterTypeRepository;
    private final ConcurrentHashMap<Long, CharacterType> characterTypeCache;

    public CharacterTypeService(CharacterTypeRepository characterTypeRepository, ConcurrentHashMap<Long, CharacterType> characterTypeCache) {
        this.characterTypeRepository = characterTypeRepository;
        this.characterTypeCache = characterTypeCache;
    }

    @Override
    public CharacterType findById(Long id) {
        if (characterTypeCache.containsKey(id)) {
            return characterTypeCache.get(id);
        }
        return characterTypeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(CharacterType.class, id));
    }

    @Override
    public List<CharacterType> findNotArchived() {
        return characterTypeCache.values().stream()
                .filter(item -> item.getDtArchive() == null)
                .collect(Collectors.toList());
    }
}
