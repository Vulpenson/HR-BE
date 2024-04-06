package BE.artifact.model.absence;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AbsenceDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String mimeType;

    @Lob
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "absence_id")
    private Absence absence;

    // Constructors, getters, and setters
}
