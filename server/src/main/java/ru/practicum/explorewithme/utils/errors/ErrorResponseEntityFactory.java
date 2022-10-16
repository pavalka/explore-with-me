package ru.practicum.explorewithme.utils.errors;

import ru.practicum.explorewithme.dto.errors.ApiError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseEntityFactory {
    public static ResponseEntity<ApiError> createNotFoundResponseEntity(@NonNull Throwable ex) {
        var errMsg = ex.getMessage() != null ? ex.getMessage() : ErrorsMessages.UNKNOWN_ERROR;

        return createNotFoundResponseEntity(errMsg);
    }

    public static ResponseEntity<ApiError> createNotFoundResponseEntity(@NonNull String errMsg) {
        var apiError = new ApiError(List.of(), errMsg, ErrorsReasons.ELEMENT_NOT_FOUND, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<ApiError> createBadRequestResponseEntity(@NonNull Throwable ex) {
        var errMsg = ex.getMessage() != null ? ex.getMessage() : ErrorsMessages.UNKNOWN_ERROR;

        return createBadRequestResponseEntity(errMsg);
    }

    public static ResponseEntity<ApiError> createBadRequestResponseEntity(@NonNull String errMsg) {
        var apiError = new ApiError(List.of(), errMsg, ErrorsReasons.BAD_REQUEST, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ApiError> createConflictResponseEntity(@NonNull Throwable ex) {
        var errMsg = ex.getMessage() != null ? ex.getMessage() : ErrorsMessages.UNKNOWN_ERROR;

        return createConflictResponseEntity(errMsg);
    }

    public static ResponseEntity<ApiError> createConflictResponseEntity(@NonNull String errMsg) {
        var apiError = new ApiError(List.of(), errMsg, ErrorsReasons.DATA_INTEGRITY_VIOLATION, HttpStatus.CONFLICT);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ApiError> createInternalServerErrorResponseEntity(@NonNull Throwable ex) {
        var errMsg = ex.getMessage() != null ? ex.getMessage() : ErrorsMessages.UNKNOWN_ERROR;

        return createInternalServerErrorResponseEntity(errMsg);
    }

    public static ResponseEntity<ApiError> createInternalServerErrorResponseEntity(@NonNull String errMsg) {
        var apiError = new ApiError(List.of(), errMsg, ErrorsReasons.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<ApiError> createForbiddenResponseEntity(@NonNull Throwable ex) {
        var errMsg = ex.getMessage() != null ? ex.getMessage() : ErrorsMessages.UNKNOWN_ERROR;

        return createForbiddenResponseEntity(errMsg);
    }

    public static ResponseEntity<ApiError> createForbiddenResponseEntity(@NonNull String errMsg) {
        var apiError = new ApiError(List.of(), errMsg, ErrorsReasons.OPERATION_CONDITIONS_VIOLATION,
                HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
}
