package be.ucll.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.model.FAQ;
import be.ucll.service.FAQService;

@RestController
@RequestMapping("/FAQ")
@CrossOrigin(origins = "http://localhost:8000")
public class FAQRestController {
    
    private final FAQService faqService;

    public FAQRestController(FAQService faqService) {
        this.faqService = faqService;
    }

    @GetMapping
    public List<FAQ> getAllFAQs() {
        return faqService.getAllFAQs();
    }

    @PostMapping
    public FAQ createFAQ(@RequestBody FAQ faq) {
        return faqService.createFAQ(faq);
    }

    @DeleteMapping("/{id}")
    public void deleteFAQ(@PathVariable Long id) {
        faqService.deleteFAQ(id);
    }
}
