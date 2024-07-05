package com.bc2403.bc_yahoo_finance.infra;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bc2403.bc_yahoo_finance.entity.StockQuoteYahoo;
import com.bc2403.bc_yahoo_finance.model.CandleStick;
import com.bc2403.bc_yahoo_finance.model.apiResponse.StockQuoteYahooRedis;
import com.bc2403.bc_yahoo_finance.model.apiResponse.YahooApiResponse;

@Component
public class Mapper {

  @Autowired
  ModelMapper modelMapper;

  public CandleStick mapToCandleStick(
      StockQuoteYahooRedis.QuoteResult.RedisData redisData) {
    return CandleStick.builder()//
        .open(redisData.getRegularMarketOpen())//
        .high(redisData.getRegularMarketDayHigh())//
        .low(redisData.getRegularMarketDayLow())//
        .close(redisData.getRegularMarketPreviousClose())//
        .time(Instant.ofEpochSecond(redisData.getRegularMarketUnix())//
            .atZone(ZoneId.systemDefault())//
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))//
            .toString())//
        .build();
  }

  public CandleStick mapToCandleStick(StockQuoteYahoo stockQuoteYahoo) {
    return CandleStick.builder().open(stockQuoteYahoo.getOpen())//
        .high(stockQuoteYahoo.getHigh())//
        .low(stockQuoteYahoo.getLow())//
        .close(stockQuoteYahoo.getClose())//
        .time(Instant.ofEpochSecond(stockQuoteYahoo.getTimestamp())//
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            .toString())
        .build();
  }

  public StockQuoteYahoo map(YahooApiResponse yahooApiResponse) {
    return StockQuoteYahoo.builder()//
        .symbol(
            yahooApiResponse.getQuoteResponse().getResult().get(0).getSymbol())//
        .timestamp(yahooApiResponse.getQuoteResponse().getResult()
            .get(0).getRegularMarketTime())//
        // .regularMarketPrice(yahooApiResponse.getQuoteResponse().getResult()
        //     .get(0).getRegularMarketPrice())//
        // .regularMarketChange(yahooApiResponse.getQuoteResponse().getResult()
        //     .get(0).getRegularMarketChange())//
        // .bid(yahooApiResponse.getQuoteResponse().getResult().get(0).getBid())//
        // .ask(yahooApiResponse.getQuoteResponse().getResult().get(0).getAsk())//
        // .bidSize(
        //     yahooApiResponse.getQuoteResponse().getResult().get(0).getBidSize())//
        // .askSize(
        //     yahooApiResponse.getQuoteResponse().getResult().get(0).getAskSize())//
        .high(yahooApiResponse.getQuoteResponse().getResult()
            .get(0).getRegularMarketDayHigh())//
        .low(yahooApiResponse.getQuoteResponse().getResult()
            .get(0).getRegularMarketDayLow())//
        .open(yahooApiResponse.getQuoteResponse().getResult()
            .get(0).getRegularMarketOpen())//
        .close(yahooApiResponse.getQuoteResponse()
            .getResult().get(0).getRegularMarketPreviousClose())//

        .build();
  }

  public StockQuoteYahooRedis.QuoteResult mapToQuoteResult(
      YahooApiResponse yahooApiResponse) {
    YahooApiResponse.QuoteResponseDetail.Result result =
        yahooApiResponse.getQuoteResponse().getResult().get(0);
    List<StockQuoteYahooRedis.QuoteResult.RedisData> data =
        yahooApiResponse.getQuoteResponse().getResult().stream()//
            .map(res -> StockQuoteYahooRedis.QuoteResult.RedisData.builder()//
                .symbol(res.getSymbol())//
                .regularMarketUnix(res.getRegularMarketTime())//
                .regularMarketPrice(res.getRegularMarketPrice())//
                .regularMarketChange(res.getRegularMarketChange())//
                .bid(res.getBid())//
                .ask(res.getAsk())//
                .bidSize(res.getBidSize())//
                .askSize(res.getAskSize())//
                .regularMarketDayHigh(res.getRegularMarketDayHigh())//
                .regularMarketDayLow(res.getRegularMarketDayLow())//
                .regularMarketOpen(res.getRegularMarketOpen())//
                .regularMarketPreviousClose(res.getRegularMarketPreviousClose())//
                .build())//
            .collect(Collectors.toList());

    return StockQuoteYahooRedis.QuoteResult.builder()//
        .regularMarketTime(String.valueOf(result.getRegularMarketTime()))//
        .data(data)//
        .build();
  }

  public StockQuoteYahooRedis.QuoteResult.RedisData toRedisData(
      StockQuoteYahoo stockQuoteYahoo) {
    if (stockQuoteYahoo == null) {
      return null;
    }

    return StockQuoteYahooRedis.QuoteResult.RedisData.builder()
        .symbol(stockQuoteYahoo.getSymbol())
        .regularMarketUnix(stockQuoteYahoo.getTimestamp())
        // .regularMarketPrice(stockQuoteYahoo.getRegularMarketPrice())
        // .regularMarketChange(stockQuoteYahoo.getRegularMarketChange())
        // .bid(stockQuoteYahoo.getBid())//
        // .ask(stockQuoteYahoo.getAsk())
        // .bidSize(stockQuoteYahoo.getBidSize())
        // .askSize(stockQuoteYahoo.getAskSize())
        .regularMarketOpen(stockQuoteYahoo.getOpen())
        .regularMarketDayHigh(stockQuoteYahoo.getHigh())
        .regularMarketDayLow(stockQuoteYahoo.getLow())
        .regularMarketPreviousClose(
            stockQuoteYahoo.getClose())
        .build();
  }

  public StockQuoteYahooRedis.QuoteResult.RedisData map(
      StockQuoteYahoo stockQuoteYahoo) {
    return modelMapper.map(stockQuoteYahoo,
        StockQuoteYahooRedis.QuoteResult.RedisData.class);
  }
}
