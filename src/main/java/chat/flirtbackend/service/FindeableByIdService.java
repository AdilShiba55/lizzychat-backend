package chat.flirtbackend.service;

public interface FindeableByIdService<T> {
    T findById(Long id);
}
