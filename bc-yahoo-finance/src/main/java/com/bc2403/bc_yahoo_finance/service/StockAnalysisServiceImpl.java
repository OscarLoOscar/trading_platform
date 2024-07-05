package com.bc2403.bc_yahoo_finance.service;

import java.util.List;
import com.bc2403.bc_yahoo_finance.model.CandleStick;
import com.bc2403.bc_yahoo_finance.model.EMAData;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface StockAnalysisServiceImpl {
  List<EMAData> EMA(String symbol, String period) throws IllegalAccessException;

  public List<CandleStick> convertToOtherTimeFrameCandleStick(String symbol,
      String period) throws JsonProcessingException;
}
