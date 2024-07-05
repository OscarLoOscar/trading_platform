package com.bc2403.bc_yahoo_finance.config;


import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CORSFilter implements Filter {

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest reqs = (HttpServletRequest) req;
    String curOrigin = reqs.getHeader("Origin");
    HttpServletResponse response = (HttpServletResponse) res;
    response.setHeader("Access-Control-Allow-Origin", curOrigin == null ? "true" : curOrigin);
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, HEAD");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers",
        "access-control-allow-origin, authority, content-type, version-info, X-Requested-With");
    response.setContentType("application/json;charset=UTF-8");
    chain.doFilter(req, res);
  }

  public void init(FilterConfig filterConfig) {
  }

  public void destroy() {
  }
}

/*
 * CORSFilter class是Servlet過濾器，用於處理跨來源資源共享 (Cross-Origin Resource Sharing，CORS) 的請求。
 * 
 *  CORS是一種機制，用於在瀏覽器中允許一個網頁從一個網域（網站）向不同的網域（網站）發起跨域請求。 
 * 
 * Spring中，如果你front-end託管在一個不同的網域或連接埠上，就需要解決CORS的問題，以確保前端能夠與後端進行通訊。

這個CORSFilter類別的主要作用是配置HTTP回應頭，以允許跨域請求。 以下是它的一些關鍵設定：

Access-Control-Allow-Origin：這個回應頭指定了哪些網域可以存取資源。 通常，它會設定為請求中的Origin頭的值，以允許來自相同網域的請求。
Access-Control-Allow-Credentials：這個回應頭指示瀏覽器是否可以包含憑證（如cookies、HTTP認證等）在跨網域請求中。 設定為true表示可以包括憑證。
Access-Control-Allow-Methods：指定哪些HTTP方法（例如POST、GET、OPTIONS、DELETE、HEAD）可以被跨網域請求使用。
Access-Control-Max-Age：指定預檢請求（OPTIONS）的快取時間，以秒為單位。 這可以減少瀏覽器多次發送預檢請求的頻率。
Access-Control-Allow-Headers：指定瀏覽器允許的自訂請求頭。 這裡列出了一些常見的請求頭，但你可以根據你的需求添加或修改。
這個CORSFilter通常用於處理跨域請求，以確保你的前端應用能夠正常與後端進行通信，而不受瀏覽器的同源策略的限制。 這對於建立分散式的網路應用程式和API很重要，因為它們可能會託管在不同的網域或連接埠上。 這個過濾器會在每個請求上執行，並根據配置的回應頭來處理CORS。
 */