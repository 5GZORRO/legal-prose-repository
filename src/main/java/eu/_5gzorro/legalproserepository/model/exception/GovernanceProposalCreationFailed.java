package eu._5gzorro.legalproserepository.model.exception;

public class GovernanceProposalCreationFailed extends RuntimeException {

    public GovernanceProposalCreationFailed() {
    }

    @Override
    public String getMessage() {
        return String.format("An error occurred when creating associated governance proposal");
    }
}
