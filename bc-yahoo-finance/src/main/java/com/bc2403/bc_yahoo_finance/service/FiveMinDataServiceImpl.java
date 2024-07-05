package com.bc2403.bc_yahoo_finance.service;

import java.util.List;
import com.bc2403.bc_yahoo_finance.model.apiResponse.StockQuoteYahooRedis;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface FiveMinDataServiceImpl {
  List<StockQuoteYahooRedis.QuoteResult.RedisData> getFiveMinutesData(
      String symbol) throws JsonProcessingException;
}
