package chat.flirtbackend.service;

import chat.flirtbackend.entity.AgeLook;
import chat.flirtbackend.entity.ClothesColor;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.ClothesColorRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClothesColorService {

    private final ClothesColorRepository clothesColorRepository;
    private final ConcurrentHashMap<Long, ClothesColor> clothesColorCache;

    public ClothesColorService(ClothesColorRepository clothesColorRepository, ConcurrentHashMap<Long, ClothesColor> clothesColorCache) {
        this.clothesColorRepository = clothesColorRepository;
        this.clothesColorCache = clothesColorCache;
    }

    public ClothesColor findById(Long id) {
        if(clothesColorCache.containsKey(id)) {
            return clothesColorCache.get(id);
        }
        return clothesColorRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(ClothesColor.class, id));
    }
}
