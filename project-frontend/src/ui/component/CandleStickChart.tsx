import * as Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import HighchartsStock from 'highcharts/modules/stock';
import AnnotationsModule from 'highcharts/modules/annotations';
import StockToolsModule from 'highcharts/modules/stock-tools';
import React, { useState, useEffect, useCallback, useMemo, useRef } from 'react';
import { Button, CircularProgress, Typography } from '@mui/material';
import { StockData } from './StockData';  // Ensure this import is correct

// Initialize Highcharts modules
HighchartsStock(Highcharts);
AnnotationsModule(Highcharts);
StockToolsModule(Highcharts);

interface CandleStickChartProps {
  symbol: string;
}

const CandleStickChart: React.FC<CandleStickChartProps> = ({ symbol }) => {
  const chartComponentRef = useRef<HighchartsReact.RefObject>(null);
  const [period, setPeriod] = useState('5m');
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [options, setOptions] = useState<Highcharts.Options>(getInitialChartOptions());

  const cache = useMemo(() => new Map<string, StockData[]>(), []);

  const fetchData = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    const cacheKey = `${symbol}-${period}`;

    if (cache.has(cacheKey)) {
      updateChartData(cache.get(cacheKey)!);
      setIsLoading(false);
      return;
    }

    try {
      const response = await fetch(`http://localhost:8085/ema/timeframe?symbol=${symbol}&period=${period}`);
      if (!response.ok) throw new Error('Network response was not ok');

      const data: StockData[] = await response.json();
      cache.set(cacheKey, data);
      updateChartData(data);
    } catch (error) {
      console.error('Error fetching data:', error);
      setError('Failed to fetch data. Please try again.');
    } finally {
      setIsLoading(false);
    }
  }, [symbol, period, cache]);

  const updateChartData = (data: StockData[]) => {
    const formattedData = data.map(item => [
      new Date(item.time).getTime(),  // Convert time to timestamp
      item.open,
      item.high,
      item.low,
      item.close
    ]);

    setOptions(prevOptions => ({
      ...prevOptions,
      title: { text: `Chart for ${symbol}` },
      series: [{
        type: 'candlestick',
        name: symbol,
        data: formattedData,
        color: 'red',
        lineColor: 'red',
        upColor: 'green',
        upLineColor: 'green'
      }] as Highcharts.SeriesOptionsType[]
    }));
  };

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const setTimeInterval = (selectedTime: string) => {
    setPeriod(selectedTime);
  };

  return (
    <div>
      <div className="time-buttons" style={{ marginBottom: '10px' }}>
        {['5m', '15m', '30m', '1h', '4h', 'd', 'w'].map((timeInterval) => (
          <Button
            key={timeInterval}
            onClick={() => setTimeInterval(timeInterval)}
            variant="contained"
            color="primary"
            size="small"
            style={{ margin: '0 5px' }}
          >
            {timeInterval.toUpperCase()}
          </Button>
        ))}
      </div>
      <div style={{ position: 'relative', height: '400px' }}>
        {isLoading && (
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
            <CircularProgress />
          </div>
        )}
        {error && (
          <Typography color="error" style={{ textAlign: 'center' }}>
            {error}
          </Typography>
        )}
        {!isLoading && !error && (
          <HighchartsReact
            highcharts={Highcharts}
            constructorType={'stockChart'}
            options={options}
            ref={chartComponentRef}
          />
        )}
      </div>
    </div>
  );
};

function getInitialChartOptions(): Highcharts.Options {
  return {
    title: { text: 'Stock Chart' },
    xAxis: {
      type: 'datetime',
      dateTimeLabelFormats: {
        millisecond: '%H:%M:%S.%L',
        second: '%H:%M:%S',
        minute: '%H:%M',
        hour: '%H:%M',
        day: '%m-%d',
        week: '%m-%d',
        month: '%Y-%m',
        year: '%Y'
      },
    },
    yAxis: {
      startOnTick: false,
      endOnTick: false,
      minPadding: 0.1,
      maxPadding: 0.2,
      labels: {
        formatter: function () {
          return Number(this.value).toFixed(2);
        }
      }
    },
    series: [{
      type: 'candlestick',
      name: 'Candlestick',
      data: [],
      color: 'red',
      lineColor: 'red',
      upColor: 'green',
      upLineColor: 'green'
    }],
    rangeSelector: {
      selected: 0,
      buttons: []
    },
    stockTools: {
      gui: { enabled: true }
    },
    navigation: {
      bindings: {
        rect: {
          className: 'highcharts-rect-annotation',
          start: function (e: Highcharts.PointerEventObject, chart: Highcharts.Chart) {
            return chart.addAnnotation({
              labels: [{
                point: {
                  xAxis: 0,
                  yAxis: 0,
                  x: e.chartX,
                  y: e.chartY
                },
                text: 'Label'
              }],
              shapeOptions: {
                type: 'rect',
                fill: 'rgba(0, 0, 255, 0.1)',
                stroke: 'blue',
                strokeWidth: 1
              }
            });
          }
        }
      }
    }
  };
}

export default CandleStickChart;
