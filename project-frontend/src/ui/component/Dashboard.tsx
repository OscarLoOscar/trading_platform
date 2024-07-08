import {
  AppBar,
  Button,
  Card,
  CardContent,
  Grid, IconButton, Paper,
  Table, TableBody,
  TableContainer,
  Toolbar,
  Typography
} from '@material-ui/core';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import NotificationsIcon from '@mui/icons-material/Notifications';
import { makeStyles } from '@material-ui/core/styles';
import React, { useState } from 'react';
import LastActivities from './LastActivities';
import SidebarNavigation from './SidebarNavigation';
import CandleStickChart from './CandleStickChart';
import StockTable from './StockTable';

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  sidebar: {
    padding: theme.spacing(2),
    height: '100vh',
    backgroundColor: theme.palette.background.paper,
  },
  content: {
    padding: theme.spacing(2),
  },
  paper: {
    padding: theme.spacing(2),
    textAlign: 'center',
    color: theme.palette.text.secondary,
  },
}));

const Dashboard: React.FC = () => {
  const classes = useStyles();
  const [selectedSymbol, setSelectedSymbol] = useState<string>('');

  const handleSelectStock = (symbol: string) => {
    setSelectedSymbol(symbol);
  };

  return (
    <div className={classes.root}>
      <Grid container>
        <Grid item xs={2}>
          <Paper className={classes.sidebar}>
            {/* Sidebar content */}
            <Typography variant="h6">Balinium</Typography>
            <SidebarNavigation onSelectStock={handleSelectStock} />
          </Paper>
        </Grid>
        <Grid item xs={10}>
          <div className={classes.content}>
            <Grid container spacing={3}>
              {/* Crypto tickers */}
              <Grid item xs={12}>
                <Paper className={classes.paper}>
                  {/* Add crypto ticker components here */}
                  <AppBar position="static" style={{ backgroundColor: '#959595', height: '50px', display: 'flex', justifyContent: 'center' }}>
                    <Toolbar style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', width: '100%' }}>
                      <Typography variant="h6" component="div" style={{ textAlign: 'center' }}>
                        Stock Trading Dashboard
                      </Typography>
                      <Typography variant="h6" style={{ marginLeft: '2px', marginRight: '2px', textAlign: 'center' }}>
                        Balance: 3470.09 USD
                      </Typography>
                      <IconButton color="inherit">
                        <NotificationsIcon />
                      </IconButton>
                      <IconButton color="inherit">
                        <AccountCircleIcon />
                      </IconButton>
                    </Toolbar>
                  </AppBar>
                </Paper>
              </Grid>

              <Grid item xs={8}>
                <Paper className={classes.paper} style={{ height: '100%', backgroundColor: '#2c2c2e', color: '#ffffff' }}>
                  <CandleStickChart symbol={selectedSymbol || 'DELL'} />
                </Paper>
              </Grid>

              {/* Account info and actions */}
              <Grid item xs={4}>
                <Card>
                  <CardContent>
                    <StockTable />
                    <Typography variant="h4">$2,128,022.00</Typography>
                    <Grid container spacing={2}>
                      <Grid item xs={6}>
                        <Button variant="contained"
                          color="primary"
                          fullWidth>
                          BUY
                        </Button>
                      </Grid>
                      <Grid item xs={6}>
                        <Button variant="contained" color="secondary" fullWidth>
                          SELL
                        </Button>
                      </Grid>
                    </Grid>
                  </CardContent>
                </Card>
              </Grid>
              <Grid item xs={12}>
                <TableContainer component={Paper}>
                  <Table>
                    <TableBody>
                      <LastActivities />
                    </TableBody>
                  </Table>
                </TableContainer>
              </Grid>
            </Grid>
          </div>
        </Grid>
      </Grid>
    </div>
  );
};

export default Dashboard;