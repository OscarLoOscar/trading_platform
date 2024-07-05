package com.bc2403.bc_yahoo_finance.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import com.bc2403.bc_yahoo_finance.cookie.FinanceService;
import com.bc2403.bc_yahoo_finance.infra.Mapper;
import com.bc2403.bc_yahoo_finance.model.apiResponse.StockQuoteYahooRedis;
import com.bc2403.bc_yahoo_finance.model.apiResponse.YahooApiResponse;
import com.bc2403.bc_yahoo_finance.redis.RedisHelper;
import com.bc2403.bc_yahoo_finance.service.impl.YahooFinanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScheduledConfig {

  private final RedisHelper redisHelper;
  private final YahooFinanceService yahooFinanceService;
  private final FinanceService financeService;
  private final Mapper mapper;

  @Autowired
  public ScheduledConfig(RedisHelper redisHelper,
      YahooFinanceService yahooFinanceService, FinanceService financeService,
      Mapper mapper) {
    this.redisHelper = redisHelper;
    this.yahooFinanceService = yahooFinanceService;
    this.financeService = financeService;
    this.mapper = mapper;
  }

  @Scheduled(cron = "0 55 8 * * ?", zone = "GMT+8")
  public void clearSystemDateEntries() {
    try {
      redisHelper.del("SYSDATE-*");
      log.info("System date entries cleared successfully");
    } catch (Exception ex) {
      log.error("Error clearing system date entries", ex);
      throw ex; // Ensure error handling via the aspect and task scheduler
    }
  }
  @Scheduled(cron = "0 30 21-23,1-3 ? * MON-FRI", zone = "GMT+8")
  // @Scheduled(cron = "0 */5 21-23,1-3 ? * MON-FRI", zone = "GMT+8")
  public void syncData() {
    try {
      log.info("Start saving data to Redis");
      String keyPrefix = "5MIN-";
      List<String> stockList =
          redisHelper.lGet("STOCK_LIST", 0, -1, String.class);
      stockList.forEach(symbol -> processSymbol(symbol, keyPrefix));
      log.info("Finished saving data to Redis");
    } catch (Exception ex) {
      log.error("Error in syncData scheduled task", ex);
      throw ex; // Ensure error handling via the aspect and task scheduler
    }
  }

  // @Scheduled(cron = "0 */5 21-23,1-3 ? * MON-FRI", zone = "GMT+8")
  @Scheduled(cron = "0 30 21-23,1-3 ? * MON-FRI", zone = "GMT+8")
  public void setDataInDB() {
    try {
      log.info("Start saving data to DB");
      List<String> stockList =
          redisHelper.lGet("STOCK_LIST", 0, -1, String.class);
      stockList.forEach(this::saveSymbolInDB);
      log.info("Finished saving data to DB");
    } catch (Exception ex) {
      log.error("Error in setDataInDB scheduled task", ex);
      throw ex; // Ensure error handling via the aspect and task scheduler
    }
  }


  private void processSymbol(String symbol, String keyPrefix) {
    try {
      YahooApiResponse response = fetchDataWithRetries(symbol);
      log.info("Raw data from API saved in Redis");
      redisHelper.set(symbol, response);
      processStockQuote(symbol, response, keyPrefix);
      log.info("Symbol: {} is saved in Redis", symbol);
    } catch (Exception e) {
      log.error("Error processing symbol: {}", symbol, e);
    }
  }

  private YahooApiResponse fetchDataWithRetries(String symbol)
      throws JsonProcessingException {
    try {
      return financeService.fetchDataWithCrumb(symbol);
    } catch (HttpClientErrorException.Unauthorized ex) {
      log.warn("Unauthorized exception occurred. Trying to fetch crumb again");
      financeService.refreshCookie();
      return financeService.fetchDataWithCrumb(symbol);
    }
  }

  private void saveSymbolInDB(String symbol) {
    try {
      YahooApiResponse response = fetchDataWithRetries(symbol);
      yahooFinanceService.saveInDB(mapper.map(response));
      log.info("Symbol: {} is saved in Postgres", symbol);
    } catch (Exception e) {
      log.error("Error saving symbol in DB: {}", symbol, e);
    }
  }

  private void processStockQuote(String symbol, YahooApiResponse response,
      String keyPrefix) throws JsonProcessingException {
    StockQuoteYahooRedis.QuoteResult newResult =
        mapper.mapToQuoteResult(response);
    String redisKey = keyPrefix + symbol;
    StockQuoteYahooRedis existingEntry =
        redisHelper.get(redisKey, StockQuoteYahooRedis.class);
    if (existingEntry != null) {
      newResult.getData().forEach(existingEntry.getQuoteResult()::addData);
      existingEntry.getQuoteResult()
          .setRegularMarketTime(newResult.getRegularMarketTime());
    } else {
      existingEntry = StockQuoteYahooRedis.builder()//
          .key(redisKey)//
          .quoteResult(newResult)//
          .build();
    }
    redisHelper.set(redisKey, existingEntry, 12 * 60 * 60);
    log.info("Symbol: " + symbol + " is saved in Redis");

  }

}
