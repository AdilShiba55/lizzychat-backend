package chat.flirtbackend.controller;

import chat.flirtbackend.dto.FaqAdminSearchDTO;
import chat.flirtbackend.dto.FaqEditDTO;
import chat.flirtbackend.dto.FaqSearchDTO;
import chat.flirtbackend.dto.SearchResponse;
import chat.flirtbackend.entity.Faq;
import chat.flirtbackend.service.FaqSearchService;
import chat.flirtbackend.service.FaqService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/faq")
public class FaqAdminController {

    private final FaqService faqService;
    private final FaqSearchService faqSearchService;

    public FaqAdminController(FaqService faqService, FaqSearchService faqSearchService) {
        this.faqService = faqService;
        this.faqSearchService = faqSearchService;
    }

    @GetMapping("/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<Faq> findById(@PathVariable Long id) {
        Faq faq = faqService.findById(id);
        return ResponseEntity.ok(faq);
    }

    @GetMapping("/search")
    @Secured("ROLE_admin")
    public ResponseEntity<SearchResponse<Faq>> search(@ModelAttribute FaqAdminSearchDTO filter) {
        SearchResponse<Faq> result = faqSearchService.search(filter);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Secured("ROLE_admin")
    public ResponseEntity<HttpStatus> save(@RequestBody @Valid FaqEditDTO form) {
        faqService.save(form);
        return ResponseEntity.ok().build();
    }
}
