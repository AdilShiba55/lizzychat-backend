package chat.flirtbackend.controller;

import chat.flirtbackend.dto.GetDictsDTO;
import chat.flirtbackend.exception.DictNotFoundException;
import chat.flirtbackend.service.ArchiveableService;
import chat.flirtbackend.service.DictArchiveableService;
import chat.flirtbackend.service.DictFindeableByIdService;
import chat.flirtbackend.util.UtError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dict")
public class DictController {

    private final Map<String, DictArchiveableService<?>> dictArchiveableServices;
    private final Map<String, DictFindeableByIdService<?>> dictFindeableByIdServices;

    public DictController(Map<String, DictArchiveableService<?>> dictArchiveableServices, Map<String, DictFindeableByIdService<?>> dictFindeableByIdServices) {
        this.dictArchiveableServices = dictArchiveableServices;
        this.dictFindeableByIdServices = dictFindeableByIdServices;
    }

    @GetMapping("/{dictName}/{id}")
    public ResponseEntity<?> findById(@PathVariable String dictName, @PathVariable Long id) {
        DictFindeableByIdService<?> dictFindeableByIdService = getDictFindeableService(dictName);
        Object item = dictFindeableByIdService.findById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/notArchived")
    public ResponseEntity<Map<String, List<?>>> findNotArchived(@ModelAttribute GetDictsDTO getDicts) {
        Map<String, List<?>> result = new HashMap<>();
        for(String dictName : getDicts.getDictNames()) {
            DictArchiveableService<?> dictArchiveableService = getDictArchiveableService(dictName);
            List<?> items = dictArchiveableService.findNotArchived();
            result.put(dictName, items);
        }
        return ResponseEntity.ok(result);
    }
    
    private DictArchiveableService<?> getDictArchiveableService(String dictName) {
        String key = dictName + "Service";
        UtError.throwIf(!dictArchiveableServices.containsKey(key), new DictNotFoundException(dictName));
        return dictArchiveableServices.get(key);
    }

    private DictFindeableByIdService<?> getDictFindeableService(String dictName) {
        String key = dictName + "Service";
        UtError.throwIf(!dictArchiveableServices.containsKey(key), new DictNotFoundException(dictName));
        return dictFindeableByIdServices.get(key);
    }
}
