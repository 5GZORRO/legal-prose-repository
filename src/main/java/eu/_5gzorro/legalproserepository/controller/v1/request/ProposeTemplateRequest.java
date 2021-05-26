package eu._5gzorro.legalproserepository.controller.v1.request;

import eu._5gzorro.legalproserepository.model.enumureration.TemplateCategory;

import java.util.Objects;

public class ProposeTemplateRequest {

    private String name;
    private String description;
    private TemplateCategory category;

    public ProposeTemplateRequest() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposeTemplateRequest that = (ProposeTemplateRequest) o;
        return name.equals(that.name) && description.equals(that.description) && category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, category);
    }

    @Override
    public String toString() {
        return "ProposeTemplateRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                '}';
    }
}
