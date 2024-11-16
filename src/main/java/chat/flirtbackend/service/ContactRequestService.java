package chat.flirtbackend.service;

import chat.flirtbackend.dto.ContactRequestInfoDTO;
import chat.flirtbackend.dto.ContactRequestSearchDTO;
import chat.flirtbackend.dto.SearchResponse;
import chat.flirtbackend.entity.ContactRequest;
import chat.flirtbackend.entity.User;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.ContactRequestRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;
    private final ContactRequestSearchService contactRequestSearchService;
    private final UserService userService;

    public ContactRequestService(ContactRequestRepository contactRequestRepository, ContactRequestSearchService contactRequestSearchService, UserService userService) {
        this.contactRequestRepository = contactRequestRepository;
        this.contactRequestSearchService = contactRequestSearchService;
        this.userService = userService;
    }

    public ContactRequest findById(Long id) {
        return contactRequestRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(ContactRequest.class, id));
    }

    public ContactRequestInfoDTO getInfo(Long id) {
        ContactRequest contactRequest = findById(id);
        User user = userService.findById(contactRequest.getUserId());
        ContactRequestInfoDTO info = new ContactRequestInfoDTO();
        info.setId(contactRequest.getId());
        info.setText(contactRequest.getText());
        info.setDtCreate(contactRequest.getDtCreate());
        info.setDtArchive(contactRequest.getDtArchive());
        info.setUserId(user.getId());
        info.setUsername(user.getUsername());
        info.setUserEmail(user.getEmail());
        return info;
    }

    public SearchResponse<ContactRequest> search(ContactRequestSearchDTO filter) {
        return contactRequestSearchService.search(filter);
    }

    public void create(String text, Long userId) {
        ContactRequest contactRequest = new ContactRequest(text, userId, new Date());
        contactRequestRepository.save(contactRequest);
    }

    @Transactional
    public void archive(Long contactRequestId) {
        contactRequestRepository.archive(contactRequestId);
    }

}
