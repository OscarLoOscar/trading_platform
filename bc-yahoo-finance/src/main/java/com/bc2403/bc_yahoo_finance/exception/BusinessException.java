package com.bc2403.bc_yahoo_finance.exception;

import com.bc2403.bc_yahoo_finance.exception.exceptionEnum.Syscode;
import lombok.Getter;

@Getter
public class BusinessException extends Exception {
  private Syscode Syscode;

  public BusinessException(Syscode Syscode) {
    super(Syscode.getMessage());
    this.Syscode = Syscode;
  }
}
