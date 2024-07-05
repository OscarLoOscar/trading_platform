package com.bc2403.bc_yahoo_finance.controller.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bc2403.bc_yahoo_finance.controller.StockAnalysisControllerImpl;
import com.bc2403.bc_yahoo_finance.model.CandleStick;
import com.bc2403.bc_yahoo_finance.model.EMAData;
import com.bc2403.bc_yahoo_finance.service.impl.StockAnalysisService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/ema")
public class StockAnalysisController implements StockAnalysisControllerImpl {
  @Autowired
  private StockAnalysisService stockAnalysisService;

  @Override
  public List<EMAData> EMA(String symbol, String period)
      throws IllegalAccessException {
    return stockAnalysisService.EMA(symbol, period);
  }

  @Override
  public List<CandleStick> convertToOtherTimeFrameCandleStick(String symbol,
      String period) throws JsonProcessingException{
    return stockAnalysisService.convertToOtherTimeFrameCandleStick(symbol,
        period);
  }
}
