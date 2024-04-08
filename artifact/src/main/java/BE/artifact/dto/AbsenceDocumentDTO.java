package BE.artifact.dto;

import lombok.Data;

@Data
public class AbsenceDocumentDTO {
    private Long id;
    private String name;
    private String mimeType;

    public AbsenceDocumentDTO(Long id, String name, String mimeType) {
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
    }
}