package com.bc2403.bc_yahoo_finance.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.bc2403.bc_yahoo_finance.controller.YahooControllerImpl;
import com.bc2403.bc_yahoo_finance.model.apiResponse.YahooApiResponse;
import com.bc2403.bc_yahoo_finance.redis.RedisHelper;
import com.bc2403.bc_yahoo_finance.service.impl.SystemDateService;
import com.bc2403.bc_yahoo_finance.service.impl.YahooFinanceService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class YahooController implements YahooControllerImpl {

  @Autowired
  private YahooFinanceService yahooFinanceService;

  @Autowired
  private SystemDateService systemDateService;

  @Autowired
  private RedisHelper redisHelper;

  public YahooApiResponse getStockData(String symbol)
      throws JsonProcessingException {
    return yahooFinanceService.callYahooFinanceAPI(symbol);

  }

  @Override
  public YahooApiResponse getLatest5MinData(String symbol)
      throws JsonProcessingException {
    if (redisHelper.get(symbol) instanceof YahooApiResponse) {
      return (YahooApiResponse) redisHelper.get(symbol);
    }
    return yahooFinanceService.callYahooFinanceAPI(symbol);
  }

  @Override
  public String getSystemDate(String symbol) {
    return systemDateService.getSystemDate(symbol);
  }
}

