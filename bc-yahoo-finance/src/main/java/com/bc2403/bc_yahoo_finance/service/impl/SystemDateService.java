package com.bc2403.bc_yahoo_finance.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bc2403.bc_yahoo_finance.redis.RedisHelper;
import com.bc2403.bc_yahoo_finance.repository.StockQuoteYahooRepo;
import com.bc2403.bc_yahoo_finance.service.SystemDateServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SystemDateService implements SystemDateServiceImpl {
  private static final int EXPIRATION_HOURS = 4;

  @Autowired
  private RedisHelper redisHelper;

  @Autowired
  private StockQuoteYahooRepo stockQuoteYahooRepo;

  @Override
  public String getSystemDate(String symbol) {
    String key = "SYSDATE_" + symbol;
    this.setSysDate(key, LocalDate.now().toString());
    String date = this.getSysDate(key);
    log.info("date: " + date);
    if (date == null) {
      date = stockQuoteYahooRepo.findMaxRegularMarketTime(symbol)
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      log.info("before");
      log.info("after");

    }
    return date;
  }

  private String getSysDate(String key) {
    return redisHelper.get(key).toString();
  }

  private boolean setSysDate(String key, String date) {
    return redisHelper.set(key, date, EXPIRATION_HOURS);
  }
}
