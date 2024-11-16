package chat.flirtbackend.controller;

import chat.flirtbackend.entity.Ethnicity;
import chat.flirtbackend.service.EthnicityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ethnicity")
public class EthnicityController {
    private final EthnicityService ethnicityService;

    public EthnicityController(EthnicityService ethnicityService) {
        this.ethnicityService = ethnicityService;
    }
}
