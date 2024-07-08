import React, { useState } from 'react';
import {
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Divider,
  Collapse
} from '@material-ui/core';
import {
  Dashboard as DashboardIcon,
  CreditCard as CardIcon,
  Payment as PaymentIcon,
  Assessment as ReportIcon,
  Contacts as ContactIcon,
  Settings as SettingsIcon,
  AccountCircle as AccountIcon,
  ExpandLess,
  ExpandMore,
  ShowChart as StockIcon
} from '@material-ui/icons';
import { makeStyles, Theme } from '@material-ui/core/styles';

const useStyles = makeStyles((theme: Theme) => ({
  listItemSelected: {
    backgroundColor: theme.palette.action.selected,
    borderRadius: theme.shape.borderRadius,
  },
  nested: {
    paddingLeft: theme.spacing(4),
  },
}));

interface NavItem {
  text: string;
  icon: React.ReactElement;
}

interface Stock {
  name: string;
  symbol: string;
  price: number;
}

const stocks: Stock[] = [
  { name: 'NVDA', symbol: 'NVDA', price: 100 },
  { name: 'VOO', symbol: 'VOO', price: 200 },
  { name: 'TSLA', symbol: 'TSLA', price: 300 },
  { name: 'DELL', symbol: 'DELL', price: 300 },
  { name: 'LOGI', symbol: 'LOGI', price: 300 },

];

const navItems: NavItem[] = [
  { text: 'Cards', icon: <CardIcon /> },
  { text: 'Payments', icon: <PaymentIcon /> },
  { text: 'Reports', icon: <ReportIcon /> },
  { text: 'Contacts', icon: <ContactIcon /> },
  { text: 'Settings', icon: <SettingsIcon /> },
  { text: 'Account', icon: <AccountIcon /> },
];

interface SidebarNavigationProps {
  onSelectStock: (symbol: string) => void;
}

const SidebarNavigation: React.FC<SidebarNavigationProps> = ({ onSelectStock }) => {
  const classes = useStyles();
  const [selectedIndex, setSelectedIndex] = useState<number>(0);
  const [overviewOpen, setOverviewOpen] = useState(true);

  const handleListItemClick = (index: number): void => {
    setSelectedIndex(index);
  };

  const handleOverviewClick = () => {
    setOverviewOpen(!overviewOpen);
  };

  return (
    <List>
      <ListItem button onClick={handleOverviewClick}>
        <ListItemIcon>
          <DashboardIcon />
        </ListItemIcon>
        <ListItemText primary="Overview" />
        {overviewOpen ? <ExpandLess /> : <ExpandMore />}
      </ListItem>
      <Collapse in={overviewOpen} timeout="auto" unmountOnExit>
        <List component="div" disablePadding>
          {stocks.map((stock) => (
            <ListItem button className={classes.nested}
            key={stock.symbol}
            onClick={()=> onSelectStock(stock.symbol)}>
              <ListItemIcon>
                <StockIcon />
              </ListItemIcon>
              <ListItemText primary={`${stock.name} (${stock.symbol})`} secondary={`$${stock.price}`} />
            </ListItem>
          ))}
        </List>
      </Collapse>
      {navItems.map((item, index) => (
        <React.Fragment key={item.text}>
          {index === 4 && <Divider />}
          <ListItem
            button
            className={index + 1 === selectedIndex ? classes.listItemSelected : ''}
            onClick={() => handleListItemClick(index + 1)}
          >
            <ListItemIcon>{item.icon}</ListItemIcon>
            <ListItemText primary={item.text} />
          </ListItem>
        </React.Fragment>
      ))}
    </List>);
};

export default SidebarNavigation;