package chat.flirtbackend.service;

import chat.flirtbackend.entity.CharacterGender;
import chat.flirtbackend.entity.Location;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class LocationService implements DictArchiveableService<Location> {

    private final LocationRepository locationRepository;
    private final ConcurrentHashMap<Long, Location> locationCache;

    public LocationService(LocationRepository locationRepository, ConcurrentHashMap<Long, Location> locationCache) {
        this.locationRepository = locationRepository;
        this.locationCache = locationCache;
    }

    public Location findById(Long id) {
        if(locationCache.containsKey(id)) {
            return locationCache.get(id);
        }
        return locationRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Location.class, id));
    }

    @Override
    public List<Location> findNotArchived() {
        return locationCache.values().stream()
                .filter(item -> item.getDtArchive() == null)
                .collect(Collectors.toList());
    }
}
