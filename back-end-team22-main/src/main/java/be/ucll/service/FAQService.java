package be.ucll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.FAQ;
import be.ucll.repository.FAQRepository;

@Service
public class FAQService {
    private final FAQRepository faqRepository;

    public FAQService(FAQRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public List<FAQ> getAllFAQs() {
        return faqRepository.findAll();
    }

    public FAQ createFAQ(FAQ faq) {
        FAQ existing = faqRepository.findByQuestion(faq.getQuestion());
        if (existing != null) {
            throw new Error("FAQ with the same question already exists.");
        }

        FAQ newFAQ = new FAQ(faq.getQuestion(), faq.getAnswer());
        return faqRepository.save(newFAQ);
}
    public void deleteFAQ(Long id) {
        faqRepository.deleteById(id);
    }

}
