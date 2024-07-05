package com.bc2403.bc_yahoo_finance.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bc2403.bc_yahoo_finance.entity.StockQuoteYahoo;
import com.bc2403.bc_yahoo_finance.infra.Mapper;
import com.bc2403.bc_yahoo_finance.model.CandleStick;
import com.bc2403.bc_yahoo_finance.model.EMAData;
import com.bc2403.bc_yahoo_finance.model.apiResponse.StockQuoteYahooRedis;
import com.bc2403.bc_yahoo_finance.redis.RedisHelper;
import com.bc2403.bc_yahoo_finance.repository.StockQuoteYahooRepo;
import com.bc2403.bc_yahoo_finance.service.StockAnalysisServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StockAnalysisService implements StockAnalysisServiceImpl {
  @Autowired
  private StockQuoteYahooRepo stockQuoteYahooRepo;

  @Autowired
  private Mapper mapper;

  @Autowired
  private RedisHelper redisHelper;

  @Override
  public List<CandleStick> convertToOtherTimeFrameCandleStick(String symbol,
      String period) throws JsonProcessingException {

    if (period.equals("5m"))
      return stockQuoteYahooRepo.findAll().stream()//
          .filter(e -> e.getSymbol().equals(symbol))
          .map(mapper::mapToCandleStick).collect(Collectors.toList());

    List<StockQuoteYahoo> stockQuotes = stockQuoteYahooRepo.findAll().stream()//
        .filter(e -> e.getSymbol().equals(symbol)).collect(Collectors.toList());
    String keyPrefix = "5MIN-";
    StockQuoteYahooRedis existingEntry =
        redisHelper.get(keyPrefix + symbol, StockQuoteYahooRedis.class);


    long periodInSeconds = this.convertPeriodToSeconds(period);


    List<CandleStick> candleSticks = new ArrayList<>();
    List<StockQuoteYahoo> groupedStockQuotes = new ArrayList<>();

    long currentGroupStartTime = -1;
    for (StockQuoteYahoo stockQuote : stockQuotes) {
      if (currentGroupStartTime == -1) {
        currentGroupStartTime =
            stockQuote.getTimestamp() / periodInSeconds * periodInSeconds;
      }

      if (stockQuote.getTimestamp() >= currentGroupStartTime
          + periodInSeconds) {
        CandleStick candleStick = processGroup(groupedStockQuotes);
        candleSticks.add(candleStick);
        groupedStockQuotes.clear();
        currentGroupStartTime =
            stockQuote.getTimestamp() / periodInSeconds * periodInSeconds;
      }
      groupedStockQuotes.add(stockQuote);
    }

    if (!groupedStockQuotes.isEmpty()) {
      CandleStick candleStick = processGroup(groupedStockQuotes);
      candleSticks.add(candleStick);
    }

    return candleSticks;
  }

  @Override
  public List<EMAData> EMA(String symbol, String period)
      throws IllegalAccessException {
    List<StockQuoteYahoo> stockQuotes = stockQuoteYahooRepo.findAll().stream()
        .filter(e -> e.getSymbol().equals(symbol)).collect(Collectors.toList());

    List<EMAData> emaDataList = new ArrayList<>();
    int periodInt = Integer.parseInt(period);

    for (int i = periodInt - 1; i < stockQuotes.size(); i++) {
      BigDecimal sum = BigDecimal.ZERO;

      for (int j = i - periodInt + 1; j <= i; j++) {
        sum = sum.add(BigDecimal.valueOf(stockQuotes.get(j).getClose()));
      }

      BigDecimal periodBD = BigDecimal.valueOf(periodInt);
      BigDecimal ema = sum.divide(periodBD, RoundingMode.HALF_UP);

      String dateTime = Instant.ofEpochSecond(stockQuotes.get(i).getTimestamp())
          .atZone(ZoneId.systemDefault())
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))//
          .toString();

      EMAData emaData = new EMAData(dateTime, ema.doubleValue());
      emaDataList.add(emaData);
    }

    return emaDataList;
  }

  private long convertPeriodToSeconds(String period) {
    switch (period.toLowerCase()) {
      case "15m":
        return 900;
      case "30m":
        return 1800;
      case "1h":
        return 3600;
      case "4h":
        return 3600 * 4;
      case "d":
        return 3600 * 24;
      case "w":
        return 3600 * 24 * 5;
      default:
        throw new IllegalArgumentException("Invalid period: " + period);
    }
  }

  private CandleStick processGroup(List<StockQuoteYahoo> groupedStockQuotes) {
    double open = groupedStockQuotes.get(0).getOpen();
    double close =
        groupedStockQuotes.get(groupedStockQuotes.size() - 1).getClose();
    double high = groupedStockQuotes.stream()
        .mapToDouble(StockQuoteYahoo::getHigh).max().getAsDouble();
    double low = groupedStockQuotes.stream()
        .mapToDouble(StockQuoteYahoo::getLow).min().getAsDouble();

    String time = LocalDateTime
        .ofInstant(
            Instant.ofEpochSecond(groupedStockQuotes.get(0).getTimestamp()),
            ZoneId.systemDefault())//
        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    return CandleStick.builder()//
        .open(open)//
        .close(close)//
        .high(high)//
        .low(low)//
        .time(time)//
        .build();
  }



}
