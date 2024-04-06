package BE.artifact.model.absence;

import BE.artifact.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user; // Assuming you have a User entity for authentication

    private LocalDate startDate;
    private LocalDate endDate;
    private AbsenceType type; // e.g., VACATION, SICK_LEAVE, etc.

    @OneToMany(mappedBy = "absence", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AbsenceDocument> documents = new ArrayList<>();
}
