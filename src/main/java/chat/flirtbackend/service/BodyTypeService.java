package chat.flirtbackend.service;

import chat.flirtbackend.entity.BodyType;
import chat.flirtbackend.entity.PlaygroundImage;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.BodyTypeRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class BodyTypeService {

    private final BodyTypeRepository bodyTypeRepository;
    private final ConcurrentHashMap<Long, BodyType> bodyTypeCache;

    public BodyTypeService(BodyTypeRepository bodyTypeRepository, ConcurrentHashMap<Long, BodyType> bodyTypeCache) {
        this.bodyTypeRepository = bodyTypeRepository;
        this.bodyTypeCache = bodyTypeCache;
    }

    public BodyType findById(Long id) {
        if(bodyTypeCache.containsKey(id)) {
            return bodyTypeCache.get(id);
        }
        return bodyTypeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(BodyType.class, id));
    }
}
