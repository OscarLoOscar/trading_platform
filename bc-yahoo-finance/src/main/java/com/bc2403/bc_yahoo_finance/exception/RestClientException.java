package com.bc2403.bc_yahoo_finance.exception;

import com.bc2403.bc_yahoo_finance.exception.exceptionEnum.Syscode;
import lombok.Getter;

@Getter
public class RestClientException extends BusinessException {
  private Syscode Syscode;

  public RestClientException(Syscode Syscode) {
    super(Syscode);
    this.Syscode = Syscode;
  }
}
