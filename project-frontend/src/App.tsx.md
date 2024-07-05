import { AppBar, Box, Drawer, Grid, Paper, Toolbar, Typography } from '@mui/material';
import { styled } from '@mui/system';


const MainContainer = styled('div')(({ theme }) => ({
  display: 'flex',
  width: '100%',
  height: '100vh',
}));

const DrawerContainer = styled(Drawer)(({ theme }) => ({
  width: '15%',
  flexShrink: 0,
  [`& .MuiDrawer-paper`]: { width: '15%', boxSizing: 'border-box' },
}));

const ContentContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexGrow: 1,
  flexDirection: 'column',
}));

const ControlPanelContainer = styled(Box)(({ theme }) => ({
  // width: '15%',
  // height: '100%',
  width: '15%',
  flexShrink: 0,
  [`& .MuiDrawer-paper`]: { width: '15%', boxSizing: 'border-box' },

}));

const items = [
  { name: '總覽', change: -0.23 },
  { name: 'Calliditas Therapeutic', change: -0.23 },
  { name: 'GraniteShares 2x Lon...', change: 1.25 },
  // Add more items as needed
  { name: 'Item 4', change: 0.5 },
  { name: 'Item 5', change: -1.1 },
  { name: 'Item 6', change: 2.3 },
];

function App() {

  return (
    <>
      <MainContainer>
        <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
          <Toolbar>
            <Typography variant="h6" noWrap component="div">
              Tiger Trade
            </Typography>
          </Toolbar>
        </AppBar>
        <DrawerContainer
          variant="permanent"
        >
          <Toolbar />
          <Box sx={{ overflow: 'auto' }}>
            <List >
              {items.map((item) => (
                <ListItem button key={item.name}>
                  <ListItemText primary={item.name} secondary={`${item.change}%`} />
                </ListItem>
              ))}
            </List>
          </Box>
        </DrawerContainer>
        <ContentContainer component="main">
          <Toolbar />
          <Box sx={{ display: 'flex', height: '100%', width: '100%' }}>

            <TradingViewContainer>
              <TradingViewWidget />
            </TradingViewContainer>

            <ControlPanelContainer>
              <Grid container spacing={3}>
                <Grid item xs={12}>
                  <Paper>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                      NVDL - GraniteShares 2x Long N...
                    </Typography>
                  </Paper>
                </Grid>
                <Grid item xs={12}>
                  <Paper>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                      Control Panel
                    </Typography>
                    <Box sx={{ p: 2 }}>
                      <TextField label="買價" variant="outlined" fullWidth sx={{ mb: 2 }} />
                      <TextField label="賣價" variant="outlined" fullWidth sx={{ mb: 2 }} />
                      <Button variant="contained" color="primary" fullWidth>
                        買入
                      </Button>
                      <Divider sx={{ my: 2 }} />
                      <Button variant="contained" color="secondary" fullWidth>
                        賣出
                      </Button>
                    </Box>
                  </Paper>
                </Grid>
              </Grid>
            </ControlPanelContainer>
          </Box>
        </ContentContainer>

      </MainContainer>
    </>
  )
}

export default App
