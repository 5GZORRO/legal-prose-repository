package eu._5gzorro.legalproserepository.dto.identityPermissions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class StakeholderClaimDto {

    @JsonProperty("stakeholderDID")
    private String did;
    @JsonProperty("governanceBoardDID")
    private String governanceBoardId;
    private List<StakeholderServiceDto> stakeholderServices;
    private List<StakeholderRoleDto> stakeholderRoles;
    private StakeholderProfileDto stakeholderProfile;

    public StakeholderClaimDto() {
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getGovernanceBoardId() {
        return governanceBoardId;
    }

    public void setGovernanceBoardId(String governanceBoardId) {
        this.governanceBoardId = governanceBoardId;
    }

    public List<StakeholderServiceDto> getStakeholderServices() {
        return stakeholderServices;
    }

    public void setStakeholderServices(List<StakeholderServiceDto> stakeholderServices) {
        this.stakeholderServices = stakeholderServices;
    }

    public List<StakeholderRoleDto> getStakeholderRoles() {
        return stakeholderRoles;
    }

    public void setStakeholderRoles(List<StakeholderRoleDto> stakeholderRoles) {
        this.stakeholderRoles = stakeholderRoles;
    }

    public StakeholderProfileDto getStakeholderProfile() {
        return stakeholderProfile;
    }

    public void setStakeholderProfile(StakeholderProfileDto stakeholderProfile) {
        this.stakeholderProfile = stakeholderProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StakeholderClaimDto that = (StakeholderClaimDto) o;
        return did.equals(that.did) && Objects.equals(governanceBoardId, that.governanceBoardId) && Objects.equals(stakeholderServices, that.stakeholderServices) && Objects.equals(stakeholderRoles, that.stakeholderRoles) && stakeholderProfile.equals(that.stakeholderProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(did, governanceBoardId, stakeholderServices, stakeholderRoles, stakeholderProfile);
    }

    @Override
    public String toString() {
        return "StakeholderClaimDto{" +
                "did='" + did + '\'' +
                ", governanceBoardDid='" + governanceBoardId + '\'' +
                ", stakeholderServices=" + stakeholderServices +
                ", stakeholderRoles=" + stakeholderRoles +
                ", stakeholderProfile=" + stakeholderProfile +
                '}';
    }
}
