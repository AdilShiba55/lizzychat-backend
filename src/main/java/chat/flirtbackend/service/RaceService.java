package chat.flirtbackend.service;

import chat.flirtbackend.entity.BreastSize;
import chat.flirtbackend.entity.Race;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.RaceRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RaceService {

    private final RaceRepository raceRepository;
    private final ConcurrentHashMap<Long, Race> raceCache;

    public RaceService(RaceRepository raceRepository, ConcurrentHashMap<Long, Race> raceCache) {
        this.raceRepository = raceRepository;
        this.raceCache = raceCache;
    }

    public Race findById(Long id) {
        if(raceCache.containsKey(id)) {
            return raceCache.get(id);
        }
        return raceRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Race.class, id));
    }
}
