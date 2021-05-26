package eu._5gzorro.legalproserepository.dto;

import eu._5gzorro.legalproserepository.model.enumureration.TemplateCategory;
import eu._5gzorro.legalproserepository.model.enumureration.TemplateStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class LegalProseTemplateDto {

    private String id;
    private String name;
    private String description;
    private TemplateCategory category;
    private TemplateStatus status;
    private LocalDateTime created;
    private LocalDateTime statusUpdated;
    private LocalDateTime archived;

    public LegalProseTemplateDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TemplateCategory getCategory() {
        return category;
    }

    public void setCategory(TemplateCategory category) {
        this.category = category;
    }

    public TemplateStatus getStatus() {
        return status;
    }

    public void setStatus(TemplateStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getStatusUpdated() {
        return statusUpdated;
    }

    public void setStatusUpdated(LocalDateTime statusUpdated) {
        this.statusUpdated = statusUpdated;
    }

    public LocalDateTime getArchived() {
        return archived;
    }

    public void setArchived(LocalDateTime archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegalProseTemplateDto that = (LegalProseTemplateDto) o;
        return id.equals(that.id) && name.equals(that.name) && category == that.category && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, status);
    }

    @Override
    public String toString() {
        return "LegalProseTemplateDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", status=" + status +
                ", created=" + created +
                ", statusUpdated=" + statusUpdated +
                ", archived=" + archived +
                '}';
    }
}
