package chat.flirtbackend.controller;

import chat.flirtbackend.dto.ContactRequestInfoDTO;
import chat.flirtbackend.dto.ContactRequestSearchDTO;
import chat.flirtbackend.dto.SearchResponse;
import chat.flirtbackend.entity.ContactRequest;
import chat.flirtbackend.service.ContactRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/contact_request")
public class ContactRequestAdminController {

    private final ContactRequestService contactRequestService;

    public ContactRequestAdminController(ContactRequestService contactRequestService) {
        this.contactRequestService = contactRequestService;
    }

    @GetMapping
    @Secured("ROLE_admin")
    public ResponseEntity<SearchResponse<ContactRequest>> search(@ModelAttribute ContactRequestSearchDTO filter) {
        SearchResponse<ContactRequest> response = contactRequestService.search(filter);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<ContactRequestInfoDTO> findById(@PathVariable Long id) {
        ContactRequestInfoDTO info = contactRequestService.getInfo(id);
        return ResponseEntity.ok(info);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<HttpStatus> archive(@PathVariable Long id) {
        contactRequestService.archive(id);
        return ResponseEntity.ok().build();
    }
}
