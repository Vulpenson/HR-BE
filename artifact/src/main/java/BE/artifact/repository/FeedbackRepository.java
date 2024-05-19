package BE.artifact.repository;

import BE.artifact.model.Feedback;
import BE.artifact.model.FeedbackType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByFeedbackType(FeedbackType feedbackType, Pageable pageable);
}
