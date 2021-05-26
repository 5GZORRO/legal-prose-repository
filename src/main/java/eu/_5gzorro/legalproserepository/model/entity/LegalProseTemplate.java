package eu._5gzorro.legalproserepository.model.entity;

import eu._5gzorro.legalproserepository.model.enumureration.TemplateCategory;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateStatus;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="legal_prose_template", indexes = {
        @Index(name = "ix_did", unique = true, columnList = "did")
})
public class LegalProseTemplate {
    @Id
    private UUID id;

    @NaturalId(mutable=true) //mutable to allow null -> did
    @Column(name="did", unique = true)
    private String did;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="category")
    private TemplateCategory category;

    @Column(name="created", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(name="status", nullable = false)
    private TemplateStatus status = TemplateStatus.CREATING;

    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private LegalProseTemplateFile file;

    @Column(name="status_updated")
    private LocalDateTime statusUpdated;
    private LocalDateTime archived;

    public LegalProseTemplate() {}

    public UUID getId() {
        return id;
    }

    public LegalProseTemplate id(UUID id) {
        this.id = id;
        return this;
    }

    public String getDid() {
        return did;
    }

    public LegalProseTemplate did(String did) {
        this.did = did;
        return this;
    }

    public boolean didAssigned() {
        return Strings.isNotEmpty(did);
    }

    public String getName() {
        return name;
    }

    public LegalProseTemplate name(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LegalProseTemplate description(String description) {
        this.description = description;
        return this;
    }

    public TemplateCategory getCategory() {
        return category;
    }



    public LocalDateTime getCreated() {
        return created;
    }

    public TemplateStatus getStatus() {
        return status;
    }

    public LegalProseTemplate status(TemplateStatus status) {
        LocalDateTime now = LocalDateTime.now();

        this.status = status;
        this.statusUpdated = now;

        if(status == TemplateStatus.ARCHIVED)
            this.archived = now;

        return this;
    }

    public LocalDateTime getStatusUpdated() {
        return statusUpdated;
    }

    public LocalDateTime getArchived() {
        return archived;
    }

    public LegalProseTemplateFile getFile() {
        return file;
    }

    public void addFile(LegalProseTemplateFile file) {
        this.file = file;
        file.setLegalProseTemplate(this);
    }

    public void removeFile(LegalProseTemplateFile file) {
        if (file != null) {
            file.setLegalProseTemplate(null);
        }
        this.file = null;
    }

    public LegalProseTemplate created(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public LegalProseTemplate file(LegalProseTemplateFile file) {
        this.file = file;
        return this;
    }

    public LegalProseTemplate statusUpdated(LocalDateTime statusUpdated) {
        this.statusUpdated = statusUpdated;
        return this;
    }

    public LegalProseTemplate archived(LocalDateTime archived) {
        this.archived = archived;
        return this;
    }

    public LegalProseTemplate category(TemplateCategory category) {
        this.category = category;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegalProseTemplate that = (LegalProseTemplate) o;
        return id.equals(that.id) && Objects.equals(did, that.did) && name.equals(that.name) && Objects.equals(description, that.description) && category == that.category && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, did, name, description, category, status);
    }

    @Override
    public String toString() {
        return "LegalProseTemplate{" +
                "id=" + id +
                ", did='" + did + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", created=" + created +
                ", status=" + status +
                ", file=" + file +
                ", statusUpdated=" + statusUpdated +
                ", archived=" + archived +
                '}';
    }
}
