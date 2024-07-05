package com.bc2403.bc_yahoo_finance.exception.exceptionEnum;

import lombok.Getter;

@Getter
public enum Syscode {
  OK("000000", "OK"), //
  INVALID_INPUT("9", "Invalid input"), //
  INVALID_OPERATION("10", "Invalid operation"), //
  // Api error
  API_ERROR("100", "API error"), //
  COINGECKO_SERVICE_UNAVAILABLE("900000", "Coingecko service is unavailable"), //
  //
  NULL_POINTER("11", "Null Pointer Exception"), //
  //
  REDIS_ERROR("12", "Redis error"),
  REDIS_CONVERT_DATA_ERROR("13","Redis Mapper Error"),//
  UNAUTHORIZED("14","UNAUTHORIZED"),
  ; //

  private String syscode;
  private String message;

  private Syscode(String syscode, String message) {
    this.syscode = syscode;
    this.message = message;
  }

  public static Syscode fromCode(Syscode syscode) {
    for (Syscode c : Syscode.values()) {
      if (c.getSyscode().equals(syscode.getSyscode())) {
        return c;
      }
    }
    return null;
  }

}
