package eu._5gzorro.legalproserepository.dto;

public class LegalProseTemplateDetailDto extends LegalProseTemplateDto {
    private byte[] templateFileData;

    public LegalProseTemplateDetailDto() {
        super();
    }

    public byte[] getTemplateFileData() {
        return templateFileData;
    }

    public void setTemplateFileData(byte[] templateFileData) {
        this.templateFileData = templateFileData;
    }
}
