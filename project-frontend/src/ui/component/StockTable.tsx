import React, { useEffect, useRef, useState } from 'react';
import './StockTable.css';

interface StockData {
  [price: string]: number;
}

const StockTable: React.FC = () => {
  const [stockData, setStockData] = useState<StockData>({});
  const ws = useRef<WebSocket | null>(null);
  const prevDataRef = useRef<StockData>({});

  useEffect(() => {
    ws.current = new WebSocket('ws://localhost:8085/stockfeed');

    ws.current.onopen = () => {
      console.log('WebSocket連接已建立');
    };

    ws.current.onmessage = (event) => {
      const newData: StockData = JSON.parse(event.data);
      setStockData(newData);
    };

    ws.current.onclose = () => {
      console.log('WebSocket連接已關閉');
    };

    return () => {
      if (ws.current) {
        ws.current.close();
      }
    };
  }, []);

  useEffect(() => {
    prevDataRef.current = stockData;
  }, [stockData]);

  const getVolumeColor = (price: string, volume: number) => {
    const prevVolume = prevDataRef.current[price];
    if (prevVolume === undefined) return '';
    return volume > prevVolume ? 'lightgreen' : 'lightsalmon';
  };

  return (
    <div className="stock-table-container">
      <header>
        <h2>港股交易</h2>
        <div className="header-right">
          <span>增強限價單</span>
        </div>
      </header>

      <div className="tables-container">
        <div className="tables">
          <div className="table-container">
            <h3>買盤十檔</h3>
            <table className="buy-table">
              <thead>
                <tr>
                  <th className='table-t2'>Price</th>
                  <th className='table-t3'>Volume</th>
                </tr>
              </thead>
              <tbody>
                {Object.entries(stockData).map(([price, volume], index) => (
                  <tr key={index}>
                    <td>{parseFloat(price).toFixed(2)}</td>
                    <td style={{ backgroundColor: getVolumeColor(price, volume) }}>
                      {volume}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="table-container">
            <h3>賣盤十檔</h3>
            <table className="sell-table">
              <thead>
                <tr>
                  <th className='table-t2'>Price</th>
                  <th className='table-t3'>Volume</th>
                </tr>
              </thead>
              <tbody>
                {Object.entries(stockData).map(([price, volume], index) => (
                  <tr key={index}>
                    <td>{parseFloat(price).toFixed(2)}</td>
                    <td style={{ backgroundColor: getVolumeColor(price, volume) }}>
                      {volume}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <footer>
        <div className="stock-summary">
          <span>股票</span>
          <span>06060.HK</span>
          <span>殷安在線</span>
        </div>
        <div className="stock-price">
          <span>33.400</span>
          <span>-0.800</span>
          <span>-2.34%</span>
        </div>
      </footer>
    </div>
  );
}

export default StockTable;
