package BE.artifact.model;

import BE.artifact.utils.ProfanityCheck;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private FeedbackType feedbackType;

    @Column(length = 1000)
    @NotNull
    @ProfanityCheck(message = "Please refrain from using inappropriate language!")
    private String feedback;

    @NotNull
    private Date dateSubmitted;
}
