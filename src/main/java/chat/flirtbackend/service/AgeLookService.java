package chat.flirtbackend.service;

import chat.flirtbackend.entity.AgeLook;
import chat.flirtbackend.entity.Ethnicity;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.AgeLookRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class AgeLookService {

    private final AgeLookRepository ageLookRepository;
    private final ConcurrentHashMap<Long, AgeLook> ageLookCache;

    public AgeLookService(AgeLookRepository ageLookRepository, ConcurrentHashMap<Long, AgeLook> ageLookCache) {
        this.ageLookRepository = ageLookRepository;
        this.ageLookCache = ageLookCache;
    }

    public AgeLook findById(Long id) {
        if(ageLookCache.containsKey(id)) {
            return ageLookCache.get(id);
        }
        return ageLookRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(AgeLook.class, id));
    }
}
