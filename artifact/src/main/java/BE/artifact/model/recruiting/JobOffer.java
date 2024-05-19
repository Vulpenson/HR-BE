package BE.artifact.model.recruiting;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private String requirements;

    @OneToMany(mappedBy = "jobOffer")
    private List<Recommendation> recommendations;

    private Date creationDate;
    private boolean active;
}
