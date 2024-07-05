package com.bc2403.bc_yahoo_finance.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import com.bc2403.bc_yahoo_finance.cookie.FinanceService;
import com.bc2403.bc_yahoo_finance.redis.RedisHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AppRunner implements CommandLineRunner {

  @Autowired
  private FinanceService financeService;

  @Autowired
  private RedisHelper redisHelper;

  @Value("${yahoo.finance.api.symbols}")
  private String yahooFinanceApiSymbols;

  @Value("${redis.crumb_key}")
  private String CRUMB;

  @Value("${redis.cookie_key}")
  private String COOKIE;

  @Override
  public void run(String... args) throws Exception {
    if (redisHelper.hasKey("STOCK_LIST")) {
      log.info("Data is already saved in Redis");
    } else {
      redisHelper.lSet("STOCK_LIST", List.of(yahooFinanceApiSymbols.split(",")),
          24);
      log.info("STOCK_LIST is saved in Redis");
    }

    redisHelper.set(CRUMB, financeService.getCrumb());
    redisHelper.set(COOKIE, financeService.getCookie());
    log.info("Redis.getCrumb() : {}", redisHelper.get(CRUMB));
    log.info("Redis.getCookie() : {}", redisHelper.get(COOKIE));
    log.info("Crumb and Cookie are saved in Redis");
    // Task 6a
    redisHelper.del("SYSDATE-*");
    // String[] symbols = yahooFinanceApiSymbols.split(",");
    // log.info("AppRunner : " + Arrays.toString(symbols));
    // for (String symbol : symbols) {
    // // log.info("" + yahooFinanceService.callYahooFinanceAPI(symbol));
    // redisHelper.set(symbol,
    // yahooFinanceService.callYahooFinanceAPI(symbol));
    // log.info("Symbol: " + symbol + " is saved in Redis");
    // }
  }
}
