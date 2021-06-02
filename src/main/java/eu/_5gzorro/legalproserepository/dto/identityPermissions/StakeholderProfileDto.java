package eu._5gzorro.legalproserepository.dto.identityPermissions;

import eu._5gzorro.legalproserepository.dto.EmailNotificationDto;
import eu._5gzorro.legalproserepository.model.NotificationMethodBase;

import java.util.Objects;

public class StakeholderProfileDto {

    private String name;
    private String ledgerIdentity;
    private String address;
    private NotificationMethodBase notificationMethod;

    public StakeholderProfileDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLedgerIdentity() {
        return ledgerIdentity;
    }

    public void setLedgerIdentity(String ledgerIdentity) {
        this.ledgerIdentity = ledgerIdentity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public NotificationMethodBase getNotificationMethod() {
        return notificationMethod;
    }

    public void setNotificationMethod(NotificationMethodBase notificationMethod) {
        this.notificationMethod = notificationMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StakeholderProfileDto that = (StakeholderProfileDto) o;
        return name.equals(that.name) && ledgerIdentity.equals(that.ledgerIdentity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ledgerIdentity);
    }

    @Override
    public String toString() {
        return "StakeholderProfileDto{" +
                "name='" + name + '\'' +
                ", ledgerIdentity='" + ledgerIdentity + '\'' +
                ", address='" + address + '\'' +
                ", notificationMethod=" + notificationMethod +
                '}';
    }
}
