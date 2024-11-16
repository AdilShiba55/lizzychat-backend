package chat.flirtbackend.service;

import chat.flirtbackend.entity.BreastSize;
import chat.flirtbackend.entity.HairColor;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.BreastSizeRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class BreastSizeService {

    private final BreastSizeRepository breastSizeRepository;
    private final ConcurrentHashMap<Long, BreastSize> breastSizeCache;

    public BreastSizeService(BreastSizeRepository breastSizeRepository, ConcurrentHashMap<Long, BreastSize> breastSizeCache) {
        this.breastSizeRepository = breastSizeRepository;
        this.breastSizeCache = breastSizeCache;
    }

    public BreastSize findById(Long id) {
        if(breastSizeCache.containsKey(id)) {
            return breastSizeCache.get(id);
        }
        return breastSizeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(BreastSize.class, id));
    }
}
