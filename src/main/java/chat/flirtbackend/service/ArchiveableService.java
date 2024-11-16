package chat.flirtbackend.service;

import java.util.List;

public interface ArchiveableService<T> {
    List<T> findNotArchived();
}
