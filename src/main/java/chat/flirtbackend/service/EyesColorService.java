package chat.flirtbackend.service;

import chat.flirtbackend.entity.EyesColor;
import chat.flirtbackend.entity.HairColor;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.EyesColorRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class EyesColorService {

    private final EyesColorRepository eyesColorRepository;
    private final ConcurrentHashMap<Long, EyesColor> eyesColorCache;

    public EyesColorService(EyesColorRepository eyesColorRepository, ConcurrentHashMap<Long, EyesColor> eyesColorCache) {
        this.eyesColorRepository = eyesColorRepository;
        this.eyesColorCache = eyesColorCache;
    }

    public EyesColor findById(Long id) {
        if(eyesColorCache.containsKey(id)) {
            return eyesColorCache.get(id);
        }
        return eyesColorRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(EyesColor.class, id));
    }
}
