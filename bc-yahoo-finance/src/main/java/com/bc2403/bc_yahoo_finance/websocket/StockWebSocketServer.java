package com.bc2403.bc_yahoo_finance.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/stockfeed")
public class StockWebSocketServer {
  private static Set<Session> clients =
      Collections.synchronizedSet(new HashSet<Session>());

  @OnOpen
  public void onOpen(Session session) {
    clients.add(session);
  }

  @OnClose
  public void onClose(Session session) {
    clients.remove(session);
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    // 處理客戶端消息
  }

  public static void broadcastStockUpdate(String stockData) {
    for (Session client : clients) {
      try {
        client.getBasicRemote().sendText(stockData);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
