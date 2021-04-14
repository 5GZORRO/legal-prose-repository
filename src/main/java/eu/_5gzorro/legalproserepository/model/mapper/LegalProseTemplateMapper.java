package eu._5gzorro.legalproserepository.model.mapper;

import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDetailDto;
import eu._5gzorro.legalproserepository.model.entity.LegalProseTemplate;

public class LegalProseTemplateMapper {

    public static LegalProseTemplateDetailDto toLegalProseTemplateDetailDto(LegalProseTemplate template) {
        LegalProseTemplateDetailDto dto = new LegalProseTemplateDetailDto();

        dto.setId(template.didAssigned() ? template.getDid() : template.getId().toString());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setStatus(template.getStatus());
        dto.setStatusUpdated(template.getStatusUpdated());
        dto.setArchived(template.getArchived());
        dto.setCreated(template.getCreated());
        dto.setTemplateFileData(template.getFile().getData());

        return dto;
    }
}
