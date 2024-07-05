package com.bc2403.bc_yahoo_finance.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.bc2403.bc_yahoo_finance.entity.StockQuoteYahoo;
import com.bc2403.bc_yahoo_finance.model.apiResponse.YahooApiResponse;

public interface StockQuoteYahooRepo
                extends JpaRepository<StockQuoteYahoo, Integer> {

        @Query(value = "SELECT *, AVG(close) OVER (PARTITION BY symbol ORDER BY timestamp ROWS BETWEEN :period - 1 PRECEDING AND CURRENT ROW) AS sma "
                        + "FROM tstock_quote_yahoo WHERE symbol = :symbol AND MOD(EXTRACT(EPOCH FROM to_timestamp(timestamp)), 300) = 0",
                        nativeQuery = true)
        List<StockQuoteYahoo> findBySymbolAndPeriod(
                        @Param("symbol") String symbol,
                        @Param("period") int period);

        List<YahooApiResponse> findTop5BySymbolOrderByTimestamp(String symbol);

        @Query(value = "SELECT MAX(t.timestamp) FROM TSTOCK_QUOTE_YAHOO t WHERE t.symbol = :symbol",
                        nativeQuery = true)
        LocalDateTime findMaxRegularMarketTime(
                        @Param(value = "symbol") String symbol);

        @Query(value = "SELECT MAX(t.timestamp) FROM TSTOCK_QUOTE_YAHOO t WHERE t.symbol = :symbol AND DATE(TO_TIMESTAMP(t.timestamp))   < DATE(TO_TIMESTAMP(:sysDate))",
                        nativeQuery = true)
        LocalDateTime findMaxRegularMarketTime(
                        @Param(value = "symbol") String symbol,
                        @Param(value = "sysDate") Long sysDate);

        @Query(value = "SELECT * FROM TSTOCK_QUOTE_YAHOO t WHERE t.symbol = :symbol AND DATE(TO_TIMESTAMP(t.timestamp)) = DATE(TO_TIMESTAMP(:sysDate))",
                        nativeQuery = true)
        List<StockQuoteYahoo> findFiveMinDataBySymbolAndDate(
                        @Param("symbol") String symbol,
                        @Param("sysDate") Long sysDate);

        @Query(value = "SELECT * FROM TSTOCK_QUOTE_YAHOO t WHERE t.symbol = :symbol AND t.timestamp BETWEEN :startTime AND :currentTime",
                        nativeQuery = true)
        List<StockQuoteYahoo> findBySymbolAndPeriod(String symbol,
                        long startTime, long currentTime);

}
