package com.salpreh.products.restapi.config;

import com.salpreh.products.logistics.exceptions.EanProcessingException;
import com.salpreh.products.products.exceptions.ProductNotFoundException;
import com.salpreh.products.restapi.models.ApiErrorResponse;
import javax.management.ServiceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(toApiErrorResponse(ex));
  }

  @ExceptionHandler(ServiceNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleServiceNotFoundException(ServiceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(toApiErrorResponse(ex));
  }

  @ExceptionHandler(EanProcessingException.class)
  public ResponseEntity<ApiErrorResponse> handleEanProcessingException(EanProcessingException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(toApiErrorResponse(ex));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(toApiErrorResponse(ex));
  }

  private ApiErrorResponse toApiErrorResponse(Throwable t) {
    return ApiErrorResponse.builder()
      .message(t.getMessage())
      .build();
  }
}
