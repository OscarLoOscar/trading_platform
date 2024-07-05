package com.bc2403.bc_yahoo_finance.infra;

import lombok.Getter;

@Getter
public enum UriScheme {
  HTTP("http"), //
  HTTPS("https")//
  ;

  private String protocol;

  UriScheme(String protocol) {
    this.protocol = protocol;
  }

}
