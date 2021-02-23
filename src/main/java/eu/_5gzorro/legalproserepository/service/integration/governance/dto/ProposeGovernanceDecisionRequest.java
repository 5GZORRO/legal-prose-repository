package eu._5gzorro.legalproserepository.service.integration.governance.dto;

import eu._5gzorro.legalproserepository.service.integration.governance.enumeration.GovernanceActionType;

import java.util.Objects;

public class ProposeGovernanceDecisionRequest {

    private GovernanceActionType actionType;
    private ActionParamsDto actionParams;

    public GovernanceActionType getActionType() {
        return actionType;
    }

    public void setActionType(GovernanceActionType actionType) {
        this.actionType = actionType;
    }

    public ActionParamsDto getActionParams() {
        return actionParams;
    }

    public void setActionParams(ActionParamsDto actionParams) {
        this.actionParams = actionParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposeGovernanceDecisionRequest that = (ProposeGovernanceDecisionRequest) o;
        return actionType == that.actionType && actionParams.equals(that.actionParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionType, actionParams);
    }

    @Override
    public String toString() {
        return "ProposeGovernanceDecisionRequest{" +
                "actionType=" + actionType +
                ", actionParams=" + actionParams +
                '}';
    }
}
