package com.bc2403.bc_yahoo_finance.websocket;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StockWebSocketHandler extends TextWebSocketHandler {

  private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
  private final ScheduledExecutorService scheduler =
      Executors.newSingleThreadScheduledExecutor();
  private final AtomicBoolean isRunning = new AtomicBoolean(false);

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TreeMap<Double, Double> data;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    sessions.add(session);
    if (isRunning.compareAndSet(false, true)) {
      startSendingData();
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session,
      CloseStatus status) {
    sessions.remove(session);
    if (sessions.isEmpty()) {
      stopSendingData();
    }
  }

  private void startSendingData() {
    scheduler.scheduleAtFixedRate(() -> {
      if (sessions.isEmpty()) {
        stopSendingData();
        return;
      }
      String data = generateRandomCandlestickData();
      sessions.forEach(session -> {
        try {
          if (session.isOpen()) {
            session.sendMessage(new TextMessage(data));
          }
        } catch (IOException e) {
          log.error("Error sending message to WebSocket session", e);
        }
      });
    }, 0, 1, TimeUnit.SECONDS);
  }

  private void stopSendingData() {
    isRunning.set(false);
  }

  private String generateRandomCandlestickData() {
    for (int i = 0; i < 5; i++) { // 生成5个数据点
      double price = Math.round((70.00 + Math.random()) * 10.0) / 10.0;
      double volume = Math.round(400 + Math.random() * 600);
      data.put(price, volume);
    }
    try {
      return objectMapper.writeValueAsString(data);
    } catch (IOException e) {
      log.error("Failed to generate JSON data", e);
      return "{}";
    }
  }

  public void shutdown() {
    stopSendingData();
    scheduler.shutdownNow();
  }
}
