package com.bc2403.bc_yahoo_finance.model;

import java.util.List;
import com.bc2403.bc_yahoo_finance.websocket.StockData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StockDataResponse {
  private List<StockData> buyData;
  private List<StockData> sellData;

}
