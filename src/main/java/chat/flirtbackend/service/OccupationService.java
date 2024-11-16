package chat.flirtbackend.service;

import chat.flirtbackend.entity.Occupation;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.OccupationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class OccupationService implements DictArchiveableService<Occupation>, DictFindeableByIdService<Occupation> {

    private final OccupationRepository occupationRepository;
    private final ConcurrentHashMap<Long, Occupation> occupationCache;

    public OccupationService(OccupationRepository occupationRepository, ConcurrentHashMap<Long, Occupation> occupationCache) {
        this.occupationRepository = occupationRepository;
        this.occupationCache = occupationCache;
    }

    @Override
    public Occupation findById(Long id) {
        if (occupationCache.containsKey(id)) {
            return occupationCache.get(id);
        }
        return occupationRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Occupation.class, id));
    }

    @Override
    public List<Occupation> findNotArchived() {
        return new ArrayList<>(occupationCache.values()).stream()
                .filter(item -> item.getDtArchive() == null)
                .collect(Collectors.toList());

    }
}
