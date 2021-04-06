package eu._5gzorro.legalproserepository.model.entity;

import eu._5gzorro.legalproserepository.model.enumureration.TemplateStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="legal_prose_template", indexes = {
        @Index(name = "ix_handle", unique = true, columnList = "handle")
})
public class LegalProseTemplate {
    @Id
    private String id;

    @Column(name="handle", nullable = false)
    private UUID handle;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="created", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(name="status", nullable = false)
    private TemplateStatus status;

    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private LegalProseTemplateFile file;

    @Column(name="status_updated")
    private LocalDateTime statusUpdated;
    private LocalDateTime archived;

    public LegalProseTemplate() {}

    public String getId() {
        return id;
    }

    public LegalProseTemplate id(String id) {
        this.id = id;
        return this;
    }

    public UUID getHandle() {
        return handle;
    }

    public LegalProseTemplate handle(UUID handle) {
        this.handle = handle;
        return this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegalProseTemplate that = (LegalProseTemplate) o;
        return id.equals(that.id) && handle.equals(that.handle) && name.equals(that.name) && Objects.equals(description, that.description) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, handle, name, description, status);
    }

    @Override
    public String toString() {
        return "LegalProseTemplate{" +
                "id='" + id + '\'' +
                ", handle=" + handle +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", status=" + status +
                ", file=" + file +
                ", statusUpdated=" + statusUpdated +
                ", archived=" + archived +
                '}';
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
}
