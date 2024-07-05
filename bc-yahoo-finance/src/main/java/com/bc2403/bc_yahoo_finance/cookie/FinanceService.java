package com.bc2403.bc_yahoo_finance.cookie;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.bc2403.bc_yahoo_finance.infra.ApiUtil;
import com.bc2403.bc_yahoo_finance.infra.UriScheme;
import com.bc2403.bc_yahoo_finance.model.apiResponse.YahooApiResponse;
import com.bc2403.bc_yahoo_finance.redis.RedisHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FinanceService {

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${yahoo.finance.api.url}")
    private String yahooFinanceApiUrl;

    @Value("${redis.crumb_key}")
    private String CRUMB;

    @Value("${redis.cookie_key}")
    private String COOKIE;

    @Value("${yahoo.finance.api.symbols}")
    private String yahooFinanceApiSymbols;

    @Scheduled(fixedRateString = "${redis.cookie.refresh.interval}")
    public void refreshCookie() throws JsonProcessingException {
        log.info("Checking if cookies and crumb need refreshing");
        try {
            if (checkIfExpired()) {
                log.info("Cookies are expired, refreshing...");
                this.resetCookieCrumb();
                redisHelper.set(CRUMB, this.getCrumb());
                redisHelper.set(COOKIE, this.getCookie());
                log.info("New crumb and cookie are saved in Redis");
            } else {
                log.info("Cookies are still valid");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Unauthorized access - Invalid Crumb.", e);
        } catch (HttpClientErrorException | JsonProcessingException e) {
            log.error("Error fetching data from Yahoo Finance", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred in scheduled task", e);
        }
    }

    private boolean checkIfExpired() throws JsonProcessingException {
        String firstSymbol = getFirstSymbol();
        ResponseEntity<YahooApiResponse> response =
                callYahooFinanceApi(firstSymbol);

        return response.getStatusCode().is4xxClientError()
                || response.getStatusCode().is5xxServerError();
    }

    private String getFirstSymbol() {
        List<String> symbolList = List.of(yahooFinanceApiSymbols.split(","));
        return symbolList.get(0);
    }

    private ResponseEntity<YahooApiResponse> callYahooFinanceApi(String symbol)
            throws JsonProcessingException {
        return ResponseEntity.of(Optional.of(this.fetchDataWithCrumb(symbol)));
    }

    // public String getCrumbFromRedis() throws JsonProcessingException {
    // return redisHelper.get(CRUMB, String.class);
    // }

    public YahooApiResponse fetchDataWithCrumb(String symbol)
            throws JsonProcessingException {
        String url = buildUrl(symbol);
        HttpHeaders headers = buildHeaders();
        HttpEntity<YahooApiResponse> entity = new HttpEntity<>(headers);
        ResponseEntity<YahooApiResponse> response = restTemplate.exchange(url,
                HttpMethod.GET, entity, YahooApiResponse.class);
        return response.getBody();
    }

    private String buildUrl(String symbol) throws JsonProcessingException {
        UriComponentsBuilder uri =
                ApiUtil.uriBuilder(UriScheme.HTTPS, yahooFinanceApiUrl);
        uri.queryParam("symbols", symbol);
        uri.queryParam("crumb", getCrumb());
        log.info("URL: {}" + uri.build(false).toUriString());
        return uri.build(false).toUriString();
    }

    private HttpHeaders buildHeaders() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", redisHelper.get(COOKIE, String.class));
        return headers;
    }

    public static String crumb;
    public static String cookie;

    private static void setCookie() {
        try {
            URL url = new URI("https://fc.yahoo.com?p=us").toURL();
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            cookie = connection.getHeaderField("Set-Cookie");
            if (cookie != null) {
                cookie = cookie.split(";")[0];
                log.info("cookie: {}", cookie);
            }
            connection.disconnect();
        } catch (Exception e) {
            log.debug(
                    "Failed to set cookie from http request. In trade day quote requests will most likely fail.",
                    e);
        }
    }

    public static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.42";

    private static void setCrumb() {
        StringBuilder response = new StringBuilder();

        try {
            URL url =
                    new URI("https://query2.finance.yahoo.com/v1/test/getcrumb")
                            .toURL();
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Cookie", cookie);
            connection.addRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (Exception e) {
            log.debug(
                    "Failed to set crumb from http request. In trade day quote requests will most likely fail.",
                    e);
        }
        crumb = response.toString();
    }

    public synchronized void resetCookieCrumb() {
        setCookie();
        setCrumb();
    }

    public synchronized String getCookie() {
        if (cookie == null || cookie.isEmpty()) {
            resetCookieCrumb();
        }
        return cookie;
    }

    public synchronized String getCrumb() {
        if (crumb == null || crumb.isBlank()) {
            resetCookieCrumb();
        }
        return crumb;

    }
}
