package com.bc2403.bc_yahoo_finance.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bc2403.bc_yahoo_finance.entity.StockQuoteYahoo;
import com.bc2403.bc_yahoo_finance.infra.Mapper;
import com.bc2403.bc_yahoo_finance.model.apiResponse.StockQuoteYahooRedis;
import com.bc2403.bc_yahoo_finance.redis.RedisHelper;
import com.bc2403.bc_yahoo_finance.repository.StockQuoteYahooRepo;
import com.bc2403.bc_yahoo_finance.service.FiveMinDataServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FiveMinDataService implements FiveMinDataServiceImpl {
  private static final int EXPIRATION_HOURS = 12;

  @Autowired
  private RedisHelper redisHelper;

  @Autowired
  private StockQuoteYahooRepo stockQuoteYahooRepo;

  @Autowired
  private Mapper mapper;

  @Override
  public List<StockQuoteYahooRedis.QuoteResult.RedisData> getFiveMinutesData(
      String symbol) throws JsonProcessingException {
    long sysDate =
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    log.info("sysDate : " + sysDate);
    String key = "5MIN-" + symbol;
    StockQuoteYahooRedis data = getFiveMinDataFromRedis(key);
    List<StockQuoteYahoo> sysData =
        stockQuoteYahooRepo.findFiveMinDataBySymbolAndDate(symbol, sysDate);
    if (sysData.isEmpty()) {
      log.info("dbData.isEmpty()");
      return new ArrayList<>();
    }
    if (data.getQuoteResult().getData().size() < sysData.size()) {
      return sysData.stream()//
          .map(e -> mapper.toRedisData(e))//
          .toList();
    }
    return data.getQuoteResult().getData();
  }

  private StockQuoteYahooRedis getFiveMinDataFromRedis(String key)
      throws JsonProcessingException {
    return redisHelper.get(key, StockQuoteYahooRedis.class);
  }
}