package chat.flirtbackend.service;

import chat.flirtbackend.dto.FaqEditDTO;
import chat.flirtbackend.entity.Faq;
import chat.flirtbackend.exception.RecordNotFoundException;
import chat.flirtbackend.repository.FaqRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FaqService {

    private final FaqRepository faqRepository;

    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public Faq findById(Long id) {
        return faqRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Faq.class, id));
    }

    public void save(FaqEditDTO dto) {
        Faq faq = new Faq();
        Long id = dto.getId();
        if(id != null) {
            faq = findById(id);
        }
        faq.setName(dto.getName());
        faq.setDescription(dto.getDescription());
        faq.setDtCreate(new Date());
        Boolean archived = dto.getArchived();
        if(archived && faq.getDtArchive() == null) {
            faq.setDtArchive(new Date());
        } else if(!archived) {
            faq.setDtArchive(null);
        }
        faqRepository.save(faq);
    }
}
