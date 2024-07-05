package com.bc2403.bc_yahoo_finance.controller.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bc2403.bc_yahoo_finance.controller.FiveMinDataControllerImpl;
import com.bc2403.bc_yahoo_finance.model.apiResponse.StockQuoteYahooRedis;
import com.bc2403.bc_yahoo_finance.service.FiveMinDataServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/5min")
public class FiveMinDataController implements FiveMinDataControllerImpl {

  @Autowired
  private FiveMinDataServiceImpl fiveMinDataService;

  @Override
  public List<StockQuoteYahooRedis.QuoteResult.RedisData> getFiveMinutesData(
      String symbol) throws JsonProcessingException {
    
    return fiveMinDataService.getFiveMinutesData(symbol.toUpperCase());
  }


}
