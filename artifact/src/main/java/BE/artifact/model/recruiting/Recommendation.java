package BE.artifact.model.recruiting;

import BE.artifact.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user; // The recommending employee

    @ManyToOne
    @JoinColumn(name = "job_offer_id")
    @JsonBackReference
    private JobOffer jobOffer;

    private String questionnaireResponse;

    @Lob
    @Column(name = "cv_content", columnDefinition = "LONGBLOB")
    private byte[] cvContent;
}
