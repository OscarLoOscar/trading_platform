package com.bc2403.bc_yahoo_finance.service;

import java.util.List;
import com.bc2403.bc_yahoo_finance.entity.StockQuoteYahoo;
import com.bc2403.bc_yahoo_finance.model.apiResponse.YahooApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface YahooFinanceServiceImpl {
  public YahooApiResponse callYahooFinanceAPI(String symbol)throws JsonProcessingException;

  public StockQuoteYahoo saveInDB(StockQuoteYahoo stockQuoteYahoo);

   public List<YahooApiResponse>  getLatest5MinData(String symbol);
}
