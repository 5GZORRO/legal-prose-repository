package eu._5gzorro.legalproserepository.model.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="legal_prose_template_file", indexes = {
    @Index(name = "ix_template", unique = true, columnList = "legal_prose_template_id")
})
public class LegalProseTemplateFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name= "legal_prose_template_id", nullable = false)
    private LegalProseTemplate legalProseTemplate;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] data;

    public LegalProseTemplateFile() {}

    public long getId() {
        return id;
    }

    public LegalProseTemplate getLegalProseTemplate() {
        return legalProseTemplate;
    }

    public void setLegalProseTemplate(LegalProseTemplate legalProseTemplate) {
        this.legalProseTemplate = legalProseTemplate;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegalProseTemplateFile that = (LegalProseTemplateFile) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LegalProseTemplateFile{" +
                "id=" + id +
                '}';
    }
}
