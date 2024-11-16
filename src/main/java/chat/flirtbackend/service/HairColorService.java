package chat.flirtbackend.service;

import chat.flirtbackend.entity.Character;
import chat.flirtbackend.entity.HairColor;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.HairColorRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class HairColorService {

    private final HairColorRepository hairColorRepository;
    private final ConcurrentHashMap<Long, HairColor> hairColorCache;

    public HairColorService(HairColorRepository hairColorRepository, ConcurrentHashMap<Long, HairColor> hairColorCache) {
        this.hairColorRepository = hairColorRepository;
        this.hairColorCache = hairColorCache;
    }

    public HairColor findById(Long id) {
        if(hairColorCache.containsKey(id)) {
            return hairColorCache.get(id);
        }
        return hairColorRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(HairColor.class, id));
    }
}
