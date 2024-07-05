import React from 'react';
import './StockTable.css';

const StockTable: React.FC = () => {
  return (
    <div className="stock-table-container">
      <header>
        <h2>港股交易</h2>
        <div className="header-right">
          <span>增強限價單</span>
        </div>
      </header>
      
      <div className="tables">
        <div className="table-container">
          <h3>買盤十檔</h3>
          <table className="buy-table">
            <thead>
              <tr>
                <th className='table-t1'>#</th>
                <th className='table-t2'>Price</th>
                <th className='table-t3'>Volume</th>
              </tr>
            </thead>
            <tbody>
              {[
                { price: "33.400", volume: "400" },
                { price: "33.350", volume: "4.5K" },
                { price: "33.300", volume: "30.5K" },
                { price: "33.250", volume: "14.8K" },
                { price: "33.200", volume: "22.8K" },
                { price: "33.150", volume: "5.4K" },
                { price: "33.100", volume: "9.7K" },
                { price: "33.050", volume: "56.8K" },
                { price: "33.000", volume: "107.4K" },
                { price: "32.950", volume: "0" },
              ].map((row, index) => (
                <tr key={index}>
                  <td>{index + 1}</td>
                  <td>{row.price}</td>
                  <td>{row.volume}</td>
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
                <th className='table-t1'>#</th>
                <th className='table-t2'>Price</th>
                <th className='table-t3'>Volume</th>
              </tr>
            </thead>
            <tbody>
              {[
                { price: "33.450", volume: "900" },
                { price: "33.500", volume: "300" },
                { price: "33.550", volume: "9.6K" },
                { price: "33.600", volume: "11.5K" },
                { price: "33.650", volume: "5.3K" },
                { price: "33.700", volume: "5.5K" },
                { price: "33.750", volume: "4.3K" },
                { price: "33.800", volume: "6.5K" },
                { price: "33.850", volume: "15.4K" },
                { price: "33.900", volume: "3.4K" },
              ].map((row, index) => (
                <tr key={index}>
                  <td>{index + 1}</td>
                  <td>{row.price}</td>
                  <td>{row.volume}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="brokers">
        <div className="broker-list">
          <h4>買盤經紀</h4>
          {["3344 美林", "-1", "3347 美林", "3418 太平", "8167 中銀國際", "3349 美林", "-2", "6727 寶生", "1569 電訊數碼", "8575 滙豐證券"].map((broker, index) => (
            <div key={index}>{broker}</div>
          ))}
        </div>
        <div className="broker-list">
          <h4>賣盤經紀</h4>
          {["3439 高盛(亞洲)", "9054 瑞銀", "1194 瑞士信貸", "7219 德意志", "9025 瑞銀", "+1", "3439 高盛(亞洲)", "+2", "4974 法國興業", "4974 法國興業"].map((broker, index) => (
            <div key={index}>{broker}</div>
          ))}
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
