package chat.flirtbackend.controller;

import chat.flirtbackend.dto.SendContactRequestDTO;
import chat.flirtbackend.service.ContactRequestService;
import chat.flirtbackend.service.SystemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/contact_request")
public class ContactRequestController {

    private final SystemService systemService;
    private final ContactRequestService contactRequestService;

    public ContactRequestController(SystemService systemService, ContactRequestService contactRequestService) {
        this.systemService = systemService;
        this.contactRequestService = contactRequestService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid SendContactRequestDTO sendContactRequest) {
        Long id = systemService.getTokenInfo().getId();
        contactRequestService.create(sendContactRequest.getText(), id);
        return ResponseEntity.ok().build();
    }
}
