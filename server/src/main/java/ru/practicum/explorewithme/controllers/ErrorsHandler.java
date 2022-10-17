package ru.practicum.explorewithme.controllers;

import ru.practicum.explorewithme.dto.errors.ApiError;
import ru.practicum.explorewithme.exceptions.ElementNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;
import ru.practicum.explorewithme.utils.errors.ErrorResponseEntityFactory;
import ru.practicum.explorewithme.utils.errors.ErrorsMessages;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorsHandler {
    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<ApiError> handleElementNorFoundExceptions(ElementNotFoundException ex) {
        logWarn(ex);

        return ErrorResponseEntityFactory.createNotFoundResponseEntity(ex);
    }

    @ExceptionHandler(ru.practicum.explorewithme.exceptions.DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleServicesDataIntegrityViolationExceptions(
            ru.practicum.explorewithme.exceptions.DataIntegrityViolationException ex) {
        logWarn(ex);

        return ErrorResponseEntityFactory.createConflictResponseEntity(ex);
    }

    @ExceptionHandler(OperationConditionViolationException.class)
    public ResponseEntity<ApiError> handleOperationConditionViolationExceptions(OperationConditionViolationException ex) {
        logWarn(ex);

        return ErrorResponseEntityFactory.createForbiddenResponseEntity(ex);
    }

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ApiError> handleBadRequestParametersExceptions(MissingRequestValueException ex) {
        logWarn(ex);

        return ErrorResponseEntityFactory.createBadRequestResponseEntity(ErrorsMessages.REQUEST_PARAM_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleWrongRequestParameterValuesException(ConstraintViolationException ex) {
        logWarn(ex);

        return ErrorResponseEntityFactory.createBadRequestResponseEntity(ErrorsMessages.WRONG_REQUEST_PARAM_VALUE_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleWrongRequestBodyException(MethodArgumentNotValidException ex) {
        logWarn(ex);

        return ErrorResponseEntityFactory.createBadRequestResponseEntity(ErrorsMessages.WRONG_REQUEST_BODY_VALUE_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleRepositoryDataIntegrityViolationException(MethodArgumentNotValidException ex) {
        logWarn(ex);

        return ErrorResponseEntityFactory.createConflictResponseEntity(ErrorsMessages.DATA_INTEGRITY_VIOLATION_ERROR);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleOtherException(Throwable ex) {
        logError(ex);

        return ErrorResponseEntityFactory.createInternalServerErrorResponseEntity(ErrorsMessages.INTERNAL_SERVER_ERROR);
    }

    private void logWarn(Throwable ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        var errMsg = ex.getMessage() != null ? ex.getMessage() : ErrorsMessages.UNKNOWN_ERROR;

        log.warn("{}::{}.{} : {}", ex.getClass().getName(), stackTrace[0].getClassName(),
                stackTrace[0].getMethodName(), errMsg);
    }

    private void logError(Throwable ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        var errMsg = ex.getMessage() != null ? ex.getMessage() : ErrorsMessages.UNKNOWN_ERROR;

        log.error("{}::{}.{} : {}", ex.getClass().getName(), stackTrace[0].getClassName(),
                stackTrace[0].getMethodName(), errMsg);
    }
}
