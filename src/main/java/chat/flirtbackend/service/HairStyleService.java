package chat.flirtbackend.service;

import chat.flirtbackend.entity.HairColor;
import chat.flirtbackend.entity.HairStyle;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.HairStyleRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class HairStyleService {

    private final HairStyleRepository hairStyleRepository;
    private final ConcurrentHashMap<Long, HairStyle> hairStyleCache;

    public HairStyleService(HairStyleRepository hairStyleRepository, ConcurrentHashMap<Long, HairStyle> hairStyleCache) {
        this.hairStyleRepository = hairStyleRepository;
        this.hairStyleCache = hairStyleCache;
    }

    public HairStyle findById(Long id) {
        if(hairStyleCache.containsKey(id)) {
            return hairStyleCache.get(id);
        }
        return hairStyleRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(HairStyle.class, id));
    }
}
