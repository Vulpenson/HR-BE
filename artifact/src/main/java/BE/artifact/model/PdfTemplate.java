package BE.artifact.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PdfTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = true, length = 100000000)
    private byte[] content;
}
