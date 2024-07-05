package com.bc2403.bc_yahoo_finance.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import com.bc2403.bc_yahoo_finance.exception.ApiResp;
import com.bc2403.bc_yahoo_finance.exception.BusinessException;
import com.bc2403.bc_yahoo_finance.exception.RestClientException;
import com.bc2403.bc_yahoo_finance.exception.exceptionEnum.Syscode;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // @ExceptionHandler(value = JsonProcessingException.class)
  // @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE) //503
  // public ApiResp<Void> bc2311ExceptionHandler(JsonProcessingException e) {
  //   return ApiResp.<Void>builder() //
  //       .syscode(Syscode.REDIS_ERROR.getSyscode()) //
  //       .message(e.getMessage())//
  //       .build();
  // }

  // @ExceptionHandler(value = HttpClientErrorException.Unauthorized.class)
  // @ResponseStatus(value = HttpStatus.UNAUTHORIZED) //401
  // public ApiResp<Void> bc2311ExceptionHandler(HttpClientErrorException.Unauthorized e) {
  //   return ApiResp.<Void>builder() //
  //       .syscode(Syscode.UNAUTHORIZED.getSyscode()) //
  //       .message(e.getMessage())//
  //       .build();
  // }

  // @ExceptionHandler(value = RestClientException.class)
  // @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE) //503
  // public ApiResp<Void> bc2311ExceptionHandler(RestClientException e) {
  //   return ApiResp.<Void>builder() //
  //       .syscode(e.getSyscode().getSyscode()) //
  //       .message(e.getMessage())//
  //       .build();
  // }
  
  // @ExceptionHandler(value = JacksonException.class)
  // @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  // public ApiResp<Void> bc2311ExceptionHandler(JacksonException e) {
  //   return ApiResp.<Void>builder() //
  //       .syscode(Syscode.REDIS_CONVERT_DATA_ERROR.getSyscode()) //
  //       .message(e.getMessage())//
  //       // .data(null) //
  //       .build();
  // }

  // @ExceptionHandler(value = BusinessException.class)
  // @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  // public ApiResp<Void> bc2311ExceptionHandler(BusinessException e) {
  //   return ApiResp.<Void>builder() //
  //       .syscode(e.getSyscode().getSyscode()) //
  //       .message(e.getMessage())//
  //       // .data(null) //
  //       .build();
  // }
  // @ExceptionHandler(value = NullPointerException.class)
  // @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  // public ApiResp<Void> runtimeExceptionHandler(NullPointerException e) {
  //   return ApiResp.<Void>builder() //
  //       .syscode(Syscode.NULL_POINTER.getSyscode()) //
  //       .message(e.getMessage())//
  //       // .data(null) //
  //       .build();
  // }

  // @ExceptionHandler(value = RuntimeException.class)
  // @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  // public ApiResp<Void> runtimeExceptionHandler(RuntimeException e) {
  //   return ApiResp.<Void>builder() //
  //       .syscode(Syscode.INVALID_OPERATION.getSyscode()) //
  //       .message(e.getMessage())//
  //       // .data(null) //
  //       .build();
  // }

  // @ExceptionHandler(value = Exception.class)
  // @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  // public ApiResp<Void> exceptionHandler(Exception e) {
  //   return ApiResp.<Void>builder() //
  //       .syscode(Syscode.INVALID_OPERATION.getSyscode()) //
  //       .message(e.getMessage())//
  //       // .data(null) //
  //       .build();
  // }

  private static Syscode getRespCode(Exception e) {

    if (e instanceof IllegalArgumentException) {
      return Syscode.INVALID_INPUT;
    }
    // ...
    // return null;
    return Syscode.INVALID_OPERATION;
  }
}
