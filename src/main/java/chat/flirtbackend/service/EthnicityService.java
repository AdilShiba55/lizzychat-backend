package chat.flirtbackend.service;

import chat.flirtbackend.entity.Ethnicity;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.EthnicityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EthnicityService {
    private final EthnicityRepository ethnicityRepository;
    private final ConcurrentHashMap<Long, Ethnicity> ethnicityCache;

    public EthnicityService(EthnicityRepository ethnicityRepository, ConcurrentHashMap<Long, Ethnicity> ethnicityCache) {
        this.ethnicityRepository = ethnicityRepository;
        this.ethnicityCache = ethnicityCache;
    }

    public Ethnicity findById(Long id) {
        if(ethnicityCache.containsKey(id)) {
            return ethnicityCache.get(id);
        }
        return ethnicityRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Ethnicity.class, id));
    }
}
