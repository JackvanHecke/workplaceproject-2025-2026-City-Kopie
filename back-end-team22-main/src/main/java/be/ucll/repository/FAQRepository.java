package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import be.ucll.model.FAQ;

public interface FAQRepository extends JpaRepository<FAQ, Long> {

    FAQ findByQuestion(String question);

    
    
}
