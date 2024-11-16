package chat.flirtbackend.service;

import chat.flirtbackend.entity.DayTime;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.DayTimeRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class DayTimeService {

    private final DayTimeRepository dayTimeRepository;
    private final ConcurrentHashMap<Long, DayTime> dayTimeCache;

    public DayTimeService(DayTimeRepository dayTimeRepository, ConcurrentHashMap<Long, DayTime> dayTimeCache) {
        this.dayTimeRepository = dayTimeRepository;
        this.dayTimeCache = dayTimeCache;
    }

    public DayTime findById(Long id) {
        if(dayTimeCache.containsKey(id)) {
            return dayTimeCache.get(id);
        }
        return dayTimeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(DayTime.class, id));
    }
}
