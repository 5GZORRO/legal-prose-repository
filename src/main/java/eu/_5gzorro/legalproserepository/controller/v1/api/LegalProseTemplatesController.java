package eu._5gzorro.legalproserepository.controller.v1.api;

import eu._5gzorro.legalproserepository.controller.v1.request.ProposeTemplateRequest;
import eu._5gzorro.legalproserepository.controller.v1.request.constraint.ValidAccordTemplate;
import eu._5gzorro.legalproserepository.controller.v1.response.PagedTemplateResponse;
import eu._5gzorro.legalproserepository.dto.ApiErrorResponse;
import eu._5gzorro.legalproserepository.dto.LegalProseTemplateDetailDto;
import eu._5gzorro.legalproserepository.dto.identityPermissions.DIDStateDto;
import eu._5gzorro.legalproserepository.model.PageableOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/api/v1/legal-prose-templates")
@Validated
public interface LegalProseTemplatesController {

    @Operation(description = "Retrieve a paged collection of APPROVED Legal Prose Templates (without template file data) according to paging and filter parameters", tags= { "trader", "admin" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A Paged List of Approved Legal Prose Templates",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PagedTemplateResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid page or filter parameters provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    @PageableOperation
    ResponseEntity<PagedTemplateResponse> getLegalProseTemplates(
            final @Parameter(hidden = true) Pageable pageable,
            @RequestParam(required = false) final String filterText);

    @Operation(description = "Get a Legal Prose Template by id or DID", tags= { "trader", "admin" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A Legal Prose Template (including template file) matching the provided id",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LegalProseTemplateDetailDto.class, name = "Legal Prose Template")) }),
            @ApiResponse(responseCode = "400", description = "Invalid id was provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "A prose template couldn't be found with the provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("{identifier}")
    ResponseEntity<LegalProseTemplateDetailDto> getLegalProseTemplate(@PathVariable final String identifier);

//    @Operation(description = "Propose a new Legal Prose Template. The request will be subject to a governance process before becoming available in the marketplace. Proposal ID is returned in the response", tags= { "trader", "admin" })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "A Legal Prose Template was created successfully. The temporary id of the newly created template is returned.",
//                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
//            @ApiResponse(responseCode = "400", description = "Invalid Prose Template definition provided",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
//    })
//    @RequestBody(content = @Content(encoding = @Encoding(name = "proposeTemplateRequest", contentType = "application/json")))
    @PostMapping
    ResponseEntity<UUID> proposeNewLegalProseTemplate(
            @Valid @RequestBody ProposeTemplateRequest proposeTemplateRequest);


//    @Operation(description = "Propose a new Legal Prose Template. The request will be subject to a governance process before becoming available in the marketplace. Proposal ID is returned in the response", tags= { "trader", "admin" })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "A Legal Prose Template was created successfully. The temporary id of the newly created template is returned.",
//                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
//            @ApiResponse(responseCode = "400", description = "Invalid Prose Template definition provided",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
//    })
//    @RequestBody(content = @Content(encoding = @Encoding(name = "proposeTemplateRequest", contentType = "application/json")))
//    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    ResponseEntity<UUID> proposeNewLegalProseTemplate(
//            @Valid @RequestPart(value ="proposeTemplateRequest") final ProposeTemplateRequest proposeTemplateRequest,
//            @Valid @RequestParam(value = "templateFile") @ValidAccordTemplate final MultipartFile templateFile);


    @Operation(description = "Approve/Reject a newly proposed template or proposal to remove a template", tags= { "admin" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The prose template status was updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "A prose template couldn't be found with the provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("{did}/approve/{accept}")
    ResponseEntity<Void> setLegalStatementTemplateApprovalStatus(@PathVariable final String did, @PathVariable final boolean accept);

    @Operation(description = "Complete the creation of a template", tags= { "admin" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The template's DID was assigned and a governance proposal generated to manage the template's approval"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "A prose template couldn't be found with the provided id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("{id}/identity}")
    ResponseEntity<Void> updateTemplateIdentity(@PathVariable final UUID id, @Valid @RequestBody final DIDStateDto state);


    @Operation(description = "Propose the removal of a Legal Prose Template by DID. The request will be subject to a governance process before becoming available in the marketplace", tags= { "trader", "admin" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The prose template status was marked for deletion successfully. Subject to governance decision. Proposal ID is returned in the response"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "A prose template couldn't be found with the provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("{did}")
    ResponseEntity<String> removeLegalProseTemplate(@PathVariable String did);
}
