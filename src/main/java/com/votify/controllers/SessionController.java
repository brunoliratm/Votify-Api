package com.votify.controllers;

import com.votify.dtos.requests.SessionRequestDto;
import com.votify.dtos.requests.SessionRequestPutDto;
import com.votify.dtos.responses.ApiResponseDto;
import com.votify.dtos.responses.SessionResponseDto;
import com.votify.enums.SortSession;
import com.votify.facades.SessionFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/${api.version}/sessions")
public class SessionController {
    private final SessionFacade sessionFacade;

    public SessionController(SessionFacade sessionFacade) {
        this.sessionFacade = sessionFacade;
    }

    @Operation(summary = "Create a new session", description = "Create a new session", responses = {
            @ApiResponse(responseCode = "201", description = "Session created"),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Validation error\", \"errors\": [\"Session title can't be null\"]}")
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "UnauthorizedAccess", value = "{\"message\": \"Unauthorized access. Authentication required.\"}")
                    )),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "AccessDenied", value = "{\"message\": \"You do not have permission to access this resource\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Organizer not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "User Not Found", value = "{\"message\": \"Organizer not found\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"An unknown error occurred\"}")
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid SessionRequestDto sessionRequestDto,
            BindingResult bindingResult
    ) {
        this.sessionFacade.create(sessionRequestDto, bindingResult);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Get all sessions", description = "Get all sessions", responses = {
        @ApiResponse(responseCode = "200", description = "List of sessions"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "UnauthorizedAccess", value = "{\"message\": \"Unauthorized access. Authentication required.\"}")
                    )),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"message\": \"An unknown error occurred\"}")
            )
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<SessionResponseDto>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "id") SortSession sort,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction direction
    ) {
        ApiResponseDto<SessionResponseDto> response = this.sessionFacade.getAll(page, sort, direction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get a session by id", description = "Get a session by id", responses = {
        @ApiResponse(responseCode = "200", description = "Session found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "UnauthorizedAccess", value = "{\"message\": \"Unauthorized access. Authentication required.\"}")
                    )),
        @ApiResponse(responseCode = "404", description = "Session not found",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "SessionNotFound", value = "{\"message\": \"Session not found\"}")
                })
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"message\": \"An unknown error occurred\"}")
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SessionResponseDto> getById(@PathVariable Long id) {
        SessionResponseDto response = this.sessionFacade.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update a session", description = "Update a session", responses = {
        @ApiResponse(responseCode = "200", description = "Session updated"),
        @ApiResponse(responseCode = "400", description = "Invalid data",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"message\": \"Validation error\", \"errors\": [\"Session title can't be null\"]}")
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized access",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(name = "UnauthorizedAccess", value = "{\"message\": \"Unauthorized access. Authentication required.\"}")
            )),
        @ApiResponse(responseCode = "403", description = "Access denied",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(name = "AccessDenied", value = "{\"message\": \"You do not have permission to access this resource\"}")
             )),
        @ApiResponse(responseCode = "404", description = "Session not found",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "SessionNotFound", value = "{\"message\": \"Session not found\"}")
                })
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"message\": \"An unknown error occurred\"}")
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SessionResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid SessionRequestPutDto sessionRequestPutDto,
            BindingResult bindingResult
    ) {
        SessionResponseDto updatedSession = this.sessionFacade.update(id, sessionRequestPutDto, bindingResult);
        return new ResponseEntity<>(updatedSession, HttpStatus.OK);
    }

    @Operation(summary = "Delete a session", description = "Delete a session", responses = {
        @ApiResponse(responseCode = "204", description = "Session deleted"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(name = "UnauthorizedAccess", value = "{\"message\": \"Unauthorized access. Authentication required.\"}")
                )),
        @ApiResponse(responseCode = "403", description = "Access denied",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(name = "AccessDenied", value = "{\"message\": \"You do not have permission to access this resource\"}")
                )),
        @ApiResponse(responseCode = "404", description = "Session not found",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "SessionNotFound", value = "{\"message\": \"Session not found\"}")
                })
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"message\": \"An unknown error occurred\"}")
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.sessionFacade.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}