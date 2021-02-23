package eu._5gzorro.legalproserepository.controller.v1.response;

import java.util.Objects;

public class ProposalResponse {
    private String entityId;
    private String createdProposalId;

    public ProposalResponse(String entityId, String createdProposalId) {
        this.entityId = entityId;
        this.createdProposalId = createdProposalId;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getCreatedProposalId() {
        return createdProposalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalResponse that = (ProposalResponse) o;
        return entityId.equals(that.entityId) && createdProposalId.equals(that.createdProposalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, createdProposalId);
    }

    @Override
    public String toString() {
        return "ProposalResponse{" +
                "entityId='" + entityId + '\'' +
                ", createdProposalId='" + createdProposalId + '\'' +
                '}';
    }
}
