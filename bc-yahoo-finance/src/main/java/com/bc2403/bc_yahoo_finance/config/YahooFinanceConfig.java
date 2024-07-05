package com.bc2403.bc_yahoo_finance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.bc2403.bc_yahoo_finance.cookie.FinanceService;
import com.bc2403.bc_yahoo_finance.infra.ApiUtil;
import com.bc2403.bc_yahoo_finance.infra.UriScheme;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class YahooFinanceConfig {

  @Autowired
  private FinanceService financeService;

  @Value("${yahoo.finance.api.url}")
  private String yahooFinanceApiUrl;

  public UriComponents yahooFinanceUriBuilder(String symbol) throws JsonProcessingException {
    String yahooFinanceApiCrumb = financeService.getCrumb();
    UriComponentsBuilder builder =
        ApiUtil.uriBuilder(UriScheme.HTTPS, yahooFinanceApiUrl);
    builder.queryParam("symbols", symbol);
    builder.queryParam("crumb", yahooFinanceApiCrumb);
    log.info("YahooFinanceConfig : " + symbol + " : "
        + builder.build(false).toUriString());
    return builder.build(false);
  }
}
