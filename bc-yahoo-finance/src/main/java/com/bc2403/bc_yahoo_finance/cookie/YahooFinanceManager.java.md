package com.bc2403.bc_yahoo_finance.cookie;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.bc2403.bc_yahoo_finance.infra.ApiUtil;
import com.bc2403.bc_yahoo_finance.infra.UriScheme;
import com.bc2403.bc_yahoo_finance.model.apiResponse.YahooApiResponse;
import com.bc2403.bc_yahoo_finance.redis.RedisHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class YahooFinanceManager {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.42";
    private static String crumb = null;
    private static String cookie = null;

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

    @PostConstruct
    public void init() throws JsonProcessingException {
        resetCookieCrumb();
        redisHelper.set(CRUMB, getCrumb());
        redisHelper.set(COOKIE, getCookie());
    }

    @Scheduled(fixedRateString = "${redis.cookie.refresh.interval}")
    public void refreshCookie() throws JsonProcessingException {
        log.info("Checking if cookies and crumb need refreshing");
        if (checkIfExpired()) {
            log.info("Cookies are expired, refreshing...");
            resetCookieCrumb();
            redisHelper.set(CRUMB, getCrumb());
            redisHelper.set(COOKIE, getCookie());
            log.info("New crumb and cookie are saved in Redis");
        } else {
            log.info("Cookies are still valid");
        }
    }

    private boolean checkIfExpired() throws JsonProcessingException {
        String firstSymbol = getFirstSymbol();
        ResponseEntity<YahooApiResponse> response = callYahooFinanceApi(firstSymbol);
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    private String getFirstSymbol() {
        List<String> symbolList = List.of(yahooFinanceApiSymbols.split(","));
        return symbolList.get(0);
    }

    private ResponseEntity<YahooApiResponse> callYahooFinanceApi(String symbol) throws JsonProcessingException {
        return ResponseEntity.of(Optional.of(this.fetchDataWithCrumb(symbol)));
    }

    public String getCrumb() throws JsonProcessingException {
        return redisHelper.get(CRUMB, String.class);
    }

    public YahooApiResponse fetchDataWithCrumb(String symbol) throws JsonProcessingException {
        String url = buildUrl(symbol);
        HttpHeaders headers = buildHeaders();
        HttpEntity<YahooApiResponse> entity = new HttpEntity<>(headers);
        ResponseEntity<YahooApiResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, YahooApiResponse.class);
        return response.getBody();
    }

    private String buildUrl(String symbol) throws JsonProcessingException {
        UriComponentsBuilder uri = ApiUtil.uriBuilder(UriScheme.HTTPS, yahooFinanceApiUrl);
        uri.queryParam("symbols", symbol);
        uri.queryParam("crumb", getCrumb());
        log.info("check : " + uri.build(false).toUriString());
        return uri.build(false).toUriString();
    }

    private HttpHeaders buildHeaders() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", redisHelper.get(COOKIE, String.class));
        return headers;
    }

    private static void setCookie() {
        try {
            URL url = new URI("https://fc.yahoo.com?p=us").toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Get the cookie from the response headers
            cookie = connection.getHeaderField("Set-Cookie").split(";")[0];
            log.info("cookie: {}", cookie);
            connection.disconnect();
        } catch (Exception e) {
            log.debug("Failed to set cookie from http request. In trade day quote requests will most likely fail.", e);
        }
    }

    private static void setCrumb() {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URI("https://query2.finance.yahoo.com/v1/test/getcrumb").toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Set the cookie
            connection.setRequestProperty("Cookie", cookie);
            connection.addRequestProperty("User-Agent", USER_AGENT);
            // Make the HTTP request
            connection.setRequestMethod("GET");
            // Read the response content
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (Exception e) {
            log.debug("Failed to set crumb from http request. In trade day quote requests will most likely fail.", e);
        }
        log.info("check " + crumb);
        crumb = response.toString();
    }

    public static synchronized void resetCookieCrumb() {
        setCookie();
        setCrumb();
    }

    public static synchronized String getCookie() {
        if (cookie == null || cookie.isEmpty()) {
            resetCookieCrumb();
        }
        return cookie;
    }

    // public static synchronized String getCrumb() {
    //     if (crumb == null || crumb.isBlank()) {
    //         resetCookieCrumb();
    //     }
    //     return crumb;
    // }
}
