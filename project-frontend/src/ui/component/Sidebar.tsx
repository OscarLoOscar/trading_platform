import React from 'react';
import { List, ListItem, ListItemText } from '@mui/material';
import { Stock } from './types';

const stocks: Stock[] = [
  { name: 'NVDA', symbol: 'NVDA', price: 100 },
  { name: 'VOO', symbol: 'VOO', price: 200 },
  { name: 'TSLA', symbol: 'TSLA', price: 300 },
  { name: 'DELL', symbol: 'DELL', price: 300 },
  { name: 'LOGI', symbol: 'LOGI', price: 300 },

];

interface SidebarProps {
  onSelectStock: (symbol: string) => void;
}

const Sidebar: React.FC<SidebarProps> = ({ onSelectStock }) => {
  return (
    <List>
      {stocks.map((stock, index) => (
        <ListItem button key={index} onClick={() => onSelectStock(stock.symbol)}>
          <ListItemText
            primary={`${stock.name} (${stock.symbol})`}
            secondary={`Price: $${stock.price}`}
            style={{ color: 'white' }} 
          />
        </ListItem>
      ))}
    </List>
  );
}

export default Sidebar;
