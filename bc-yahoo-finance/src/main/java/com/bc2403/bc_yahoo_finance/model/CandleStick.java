package com.bc2403.bc_yahoo_finance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CandleStick {
  private double open;
  private double high;
  private double low;
  private double close;
  private String time;
}
