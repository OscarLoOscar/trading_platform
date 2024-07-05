package com.bc2403.bc_yahoo_finance.exception;

import com.bc2403.bc_yahoo_finance.exception.exceptionEnum.Syscode;
import lombok.Getter;

@Getter
public class BusinessRuntimeException extends RuntimeException {
  private Syscode Syscode;

  public BusinessRuntimeException(Syscode Syscode) {
    super(Syscode.getMessage());
    this.Syscode = Syscode;
  }

}
