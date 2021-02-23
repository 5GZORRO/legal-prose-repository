package eu._5gzorro.legalproserepository.service.integration.governance.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ActionParamsDto {

    private String entityIdentityId;

    private String evidence;

    public ActionParamsDto() {
    }

    public String getEntityIdentityId() {
        return entityIdentityId;
    }

    public void setEntityIdentityId(String entityIdentityId) {
        this.entityIdentityId = entityIdentityId;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionParamsDto that = (ActionParamsDto) o;
        return entityIdentityId.equals(that.entityIdentityId) && Objects.equals(evidence, that.evidence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityIdentityId, evidence);
    }

    @Override
    public String toString() {
        return "ActionParamsDto{" +
                "entityIdentityId='" + entityIdentityId + '\'' +
                ", evidence='" + evidence + '\'' +
                '}';
    }
}
