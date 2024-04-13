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
    private byte[] content;
}
