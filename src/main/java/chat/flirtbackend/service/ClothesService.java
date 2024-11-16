package chat.flirtbackend.service;

import chat.flirtbackend.entity.Clothes;
import chat.flirtbackend.entity.Location;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.ClothesRepository;
import chat.flirtbackend.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class ClothesService {

    private final ClothesRepository clothesRepository;

    public ClothesService(ClothesRepository clothesRepository) {
        this.clothesRepository = clothesRepository;
    }

    public Clothes findById(Long id) {
        return clothesRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Clothes.class, id));
    }
}
