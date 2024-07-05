package com.bc2403.bc_yahoo_finance.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bc2403.bc_yahoo_finance.config.YahooFinanceConfig;
import com.bc2403.bc_yahoo_finance.cookie.FinanceService;
import com.bc2403.bc_yahoo_finance.entity.StockQuoteYahoo;
import com.bc2403.bc_yahoo_finance.model.apiResponse.YahooApiResponse;
import com.bc2403.bc_yahoo_finance.repository.StockQuoteYahooRepo;
import com.bc2403.bc_yahoo_finance.service.YahooFinanceServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class YahooFinanceService implements YahooFinanceServiceImpl {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private YahooFinanceConfig config;

  @Autowired
  private StockQuoteYahooRepo stockQuoteYahooRepo;

  @Autowired
  private FinanceService financeService;
  String cookie;

  public YahooApiResponse callYahooFinanceAPI(String symbol)
      throws JsonProcessingException {
    String url = config.yahooFinanceUriBuilder(symbol).toUriString();
    log.info("callYahooFinanceAPI url : " + url);
    HttpHeaders headers = new HttpHeaders();
    String cookie = financeService.getCookie();
    headers.set("Cookie",cookie);
    log.info("callYahooFinanceAPI set Cookie");

    HttpEntity<YahooApiResponse> entity = new HttpEntity<>(headers);
    ResponseEntity<YahooApiResponse> response = restTemplate.exchange(url,
        HttpMethod.GET, entity, YahooApiResponse.class);
    return response.getBody();
  }

  @Override
  public StockQuoteYahoo saveInDB(StockQuoteYahoo stockQuoteYahoo) {
    return stockQuoteYahooRepo.save(stockQuoteYahoo);
  }

  @Override
  public List<YahooApiResponse> getLatest5MinData(String symbol) {
    return stockQuoteYahooRepo
        .findTop5BySymbolOrderByTimestamp(symbol);
  }
}
