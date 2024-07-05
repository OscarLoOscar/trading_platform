package com.bc2403.bc_yahoo_finance.model.apiResponse;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StockQuoteYahooRedis {
  private String key;
  private QuoteResult quoteResult;

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  @Builder(toBuilder = true)
  @ToString
  public static class QuoteResult {
    private String regularMarketTime;
    @Builder.Default
    private List<RedisData> data = new ArrayList<>();

    public void addData(RedisData redisData) {
      this.data.add(redisData);
    }

    public QuoteResult(String regularMarketTime) {
      this.regularMarketTime = regularMarketTime;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder(toBuilder = true)
    @ToString
    public static class RedisData {
      private String symbol;
      private long regularMarketUnix;
      private double regularMarketPrice;
      private double regularMarketChange;
      private double bid;
      private double ask;
      private int bidSize;
      private int askSize;
      private double regularMarketOpen;
      private double regularMarketDayHigh;
      private double regularMarketDayLow;
      private double regularMarketPreviousClose;
    }

  }

}
