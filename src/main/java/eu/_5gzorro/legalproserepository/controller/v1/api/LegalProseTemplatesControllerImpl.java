package eu._5gzorro.legalproserepository.controller.v1.api;

import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.dto.ApiErrorResponse;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDto;
import eu._5gzorro.legalproserepository.model.PageableOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class LegalProseTemplatesControllerImpl implements LegalProseTemplatesController {

    public ResponseEntity<Page<LegalProseTemplateDto>> getLegalProseTemplates(final Pageable pageable, final Optional<String> filterText) {

        // TODO: Implementation
        return ResponseEntity
                .ok()
                .body(null);
    }

    public ResponseEntity<LegalProseTemplateDto> getLegalProseTemplate(final String id) {

        // TODO: Implementation
        return ResponseEntity
                .ok()
                .body(null);
    }

    public ResponseEntity<String> proposeNewLegalProseTemplate(final ProposeTemplateRequest proposeTemplateRequest, final MultipartFile file) {

        // TODO: Implementation
        return ResponseEntity
                .ok()
                .body("DID");
    }

    public ResponseEntity<Void> setLegalStatementTemplateApprovalStatus(final String id, final boolean accept) {

        // TODO: Implementation
        return ResponseEntity
                .ok()
                .body(null);
    }

    public ResponseEntity<Void> removeLegalProseTemplate(String id) {

        // TODO: Implementation
        return ResponseEntity
                .ok()
                .body(null);
    }
}