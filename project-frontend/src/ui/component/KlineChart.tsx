import * as echarts from 'echarts';
import React, { useEffect, useRef, useState } from 'react';
import { StockData } from './StockData';



const KlineChart: React.FC = () => {
  const klineChart = useRef<HTMLDivElement>(null);
  const [data, setData] = useState<StockData[]>([]);
  const [error, setError] = useState<string | null>(null);

  const fetchStockData = async (symbol: string) => {
    try {
      const response = await fetch(`http://localhost:8085/5min/stock/${symbol}`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data: StockData[] = await response.json();
      setData(data);
    } catch (error: unknown) {
      if (error instanceof Error)
        setError(error.message);
    }
  }
};

const initKlineChart = (selectedTime: string) => {
  if (!klineChart.current || data.length === 0) return;

  const myChart = echarts.init(klineChart.current);
  const categoryData = data.map(item => new Date(item.regularMarketUnix * 1000).toLocaleTimeString());
  const values = data.map(item => [
    item.regularMarketOpen,
    item.regularMarketPrice,
    item.regularMarketDayLow,
    item.regularMarketDayHigh
  ]);

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    grid: [{
      left: '3%',
      right: '3%',
      top: '10%',
      height: '50%'
    }, {
      left: '3%',
      right: '3%',
      top: '65%',
      height: '10%'
    }, {
      left: '3%',
      right: '3%',
      top: '78%',
      height: '10%'
    }],
    xAxis: [{
      type: 'category',
      data: categoryData,
      scale: true,
      boundaryGap: false,
      axisLine: {
        onZero: false,
        lineStyle: {
          color: 'red',
        }
      },
      splitLine: {
        show: false
      },
      splitNumber: 20
    }, {
      type: 'category',
      gridIndex: 1,
      data: categoryData,
      axisLabel: {
        show: false
      },
    }, {
      type: 'category',
      gridIndex: 2,
      data: categoryData,
      axisLabel: {
        show: false
      },
    }],
    yAxis: [{
      scale: true,
      splitArea: {
        show: true
      },
      axisLine: {
        lineStyle: {
          color: 'red',
        }
      },
      position: 'right'
    }, {
      gridIndex: 1,
      splitNumber: 3,
      axisLine: {
        onZero: false,
        lineStyle: {
          color: 'red'
        }
      },
      axisTick: {
        show: false
      },
      splitLine: {
        show: false
      },
      axisLabel: {
        show: true
      },
      position: 'right'
    }, {
      gridIndex: 2,
      splitNumber: 4,
      axisLine: {
        onZero: false,
        lineStyle: {
          color: 'red'
        }
      },
      axisTick: {
        show: false
      },
      splitLine: {
        show: false
      },
      axisLabel: {
        show: true
      },
      position: 'right'
    }],
    dataZoom: [{
      type: 'inside',
      start: 100,
      end: 80
    }, {
      show: true,
      type: 'slider',
      y: '90%',
      xAxisIndex: [0, 1],
      start: 50,
      end: 100
    }, {
      show: false,
      xAxisIndex: [0, 2],
      type: 'slider',
      start: 20,
      end: 100
    }],
    series: [{
      name: 'Candlestick',
      type: 'candlestick',
      data: values,
      markPoint: {
        data: [{
          name: 'XX标点'
        }]
      },
      markLine: {
        silent: true,
        data: [{
          yAxis: 2222,
        }]
      }
    }]
  };

  myChart.setOption(option);
};

useEffect(() => {
  const symbol = '0388.HK';
  fetchStockData(symbol);
}, []);

useEffect(() => {
  initKlineChart('D');
  const interval = 100000; // 100 seconds
  const intervalId = setInterval(() => initKlineChart('D'), interval);
  return () => clearInterval(intervalId);
}, [data]);

return (
  <div>
    {error && <div className="error-message">Error: {error}</div>}
    <div className="time-buttons">
      <button onClick={() => initKlineChart('1')} className="custom-button">1M</button>
      <button onClick={() => initKlineChart('5')} className="custom-button">5M</button>
      <button onClick={() => initKlineChart('15')} className="custom-button">15M</button>
      <button onClick={() => initKlineChart('60')} className="custom-button">H</button>
      <button onClick={() => initKlineChart('D')} className="custom-button">D</button>
      <button onClick={() => initKlineChart('M')} className="custom-button">M</button>
      <button onClick={() => initKlineChart('W')} className="custom-button">W</button>
    </div>
    <div className="date-inputs">
      <label htmlFor="fromDate" className="custom-button">From Date:</label>
      <input type="date" id="fromDate" onChange={() => { }} />
      <label htmlFor="toDate" className="custom-button">To Date:</label>
      <input type="date" id="toDate" onChange={() => { }} />
    </div>
    <div id="kline-chart" style={{ width: '100%', height: '600px' }} ref={klineChart} className="chart-background"></div>
  </div>
);
};

export default KlineChart;

<style>{`
  .custom-button {
    padding: 5px 10px;
    background-color: #007BFF;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin: 5px;
  }

  .custom-button:nth-child(odd) {
    background-color: #5bc0de;
  }

  .custom-button:nth-child(even) {
    background-color: #d9534f;
  }

  .chart-background {
    background-color: #fff;
  }

  .error-message {
    color: red;
    font-weight: bold;
    margin: 10px 0;
  }
`}</style>