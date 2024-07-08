import React from 'react';
import Dashboard from './ui/component/Dashboard';
const App: React.FC = () => {

  return (
    <div>
      {/* <AppBar position="static" sx={{ backgroundColor: '#959595', height: '50px', display: 'flex', justifyContent: 'center' }}>
        <Toolbar sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', width: '100%' }}>
          <Typography variant="h6" sx={{ textAlign: 'center' }}>
            Stock Trading Dashboard
          </Typography>
          <Typography variant="h6" sx={{ marginLeft: 2, marginRight: 2, textAlign: 'center' }}>
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
      <Container maxWidth="xl" style={{ marginTop: '20px', maxHeight: '80%' }}>
        <Grid container spacing={3}>
          <Grid item xs={2}>
            <Paper style={{ height: '100%', backgroundColor: '#2c2c2e', color: '#ffffff' }}>
              <Typography variant="h6" style={{ padding: '20px' }}>Stocks List</Typography>
              <Sidebar onSelectStock={handleSelectStock} />
            </Paper>
          </Grid>
          <Grid item xs={7}>
            <Paper style={{ height: '100%', backgroundColor: '#2c2c2e', color: '#ffffff' }}>
              <CandleStickChart symbol={selectedSymbol || 'DELL'} />
            </Paper>
          </Grid>
          <Grid item xs={3}>
            <Paper style={{ height: '100%', width: '100%', backgroundColor: '#000000', color: '#ffffff' }}>
              <Box sx={{ padding: 2 }}>
                <Typography variant="h6" component="div">
                  Trade Information
                </Typography>
                <Typography variant="body1" component="div">
                  Current Price: 498.98
                </Typography>
                <Typography variant="body1" component="div">
                  Change: +0.08%
                </Typography>
                <StockTable />
                <Button variant="contained" fullWidth sx={{ backgroundColor: '#34c759', color: '#ffffff' }}>
                  Buy
                </Button>
                <Button variant="contained" fullWidth sx={{ mt: 2, backgroundColor: '#ff3b30', color: '#ffffff' }}>
                  Sell
                </Button>
              </Box>
            </Paper>
          </Grid>
        </Grid>
      </Container> */}
      <Dashboard/>
    </div>
  );
}

export default App;
