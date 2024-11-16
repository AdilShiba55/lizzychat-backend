package chat.flirtbackend.controller;

import chat.flirtbackend.dto.FaqSearchDTO;
import chat.flirtbackend.dto.SearchResponse;
import chat.flirtbackend.entity.Faq;
import chat.flirtbackend.service.FaqSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/faq")
public class FaqController {

    private final FaqSearchService faqSearchService;

    public FaqController(FaqSearchService faqSearchService) {
        this.faqSearchService = faqSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse<Faq>> search(@ModelAttribute FaqSearchDTO filter) {
        SearchResponse<Faq> result = faqSearchService.search(filter);
        return ResponseEntity.ok(result);
    }
}
