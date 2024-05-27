package BE.artifact.model.recruiting;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @OneToMany(mappedBy = "jobOffer")
    @JsonManagedReference
    private List<Recommendation> recommendations;

    private Date creationDate;
    private boolean active;
}
