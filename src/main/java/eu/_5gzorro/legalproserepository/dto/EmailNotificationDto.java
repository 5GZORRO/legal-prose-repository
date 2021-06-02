package eu._5gzorro.legalproserepository.dto;

import eu._5gzorro.legalproserepository.model.NotificationMethodBase;
import eu._5gzorro.legalproserepository.model.enumureration.NotificationType;

public class EmailNotificationDto extends NotificationMethodBase {

    private String distributionList;

    public EmailNotificationDto() {
        super(NotificationType.EMAIL);
    }

    public String getDistributionList() {
        return distributionList;
    }

    public EmailNotificationDto setDistributionList(String distributionList) {
        this.distributionList = distributionList;
        return this;
    }
}
