package com.bc2403.bc_yahoo_finance.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import jakarta.annotation.PreDestroy;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Autowired
  private StockWebSocketHandler stockWebSocketHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(stockWebSocketHandler, "/stockfeed")
        .setAllowedOrigins("*");
  }
  
  @PreDestroy
  public void onShutdown() {
    stockWebSocketHandler.shutdown();
  }
}
