package com.bc2403.bc_yahoo_finance.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StockData implements Comparable<StockData> {
  private String price;
  private String volume;

  @Override
  public int compareTo(StockData o) {
    return Double.compare(Double.parseDouble(this.price),
        Double.parseDouble(o.price));
  }
}
