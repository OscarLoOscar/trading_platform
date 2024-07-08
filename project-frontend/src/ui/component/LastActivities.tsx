import React from 'react';
import { Avatar, Chip, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { ArrowDownward as ArrowDownwardIcon, ArrowUpward as ArrowUpwardIcon, SwapHoriz as SwapHorizIcon } from '@material-ui/icons';

const useStyles = makeStyles((theme) => ({
  listItemSelected: {
    backgroundColor: theme.palette.action.selected,
    borderRadius: theme.shape.borderRadius,
  },
  lastActivitiesTable: {
    minWidth: 650,
  },
  statusChip: {
    borderRadius: 4,
  },
  pendingChip: {
    backgroundColor: theme.palette.warning.light,
    color: theme.palette.warning.contrastText,
  },
  successChip: {
    backgroundColor: theme.palette.success.light,
    color: theme.palette.success.contrastText,
  },
}));

interface Activity {
  id: number;
  type: string;
  icon: React.ReactNode;
  status: 'Pending' | 'Successful';
  fromTo: string;
  date: string;
  amount: string;
}

const LastActivities: React.FC = () => {
  const classes = useStyles();

  const activities: Activity[] = [
    {
      id: 1,
      type: 'BTC Received',
      icon: (
        <Avatar style={{ backgroundColor: '#E0E7FF' }}>
          <ArrowDownwardIcon style={{ color: '#3366FF' }} />
        </Avatar>
      ),
      status: 'Pending',
      fromTo: 'John Doe',
      date: '05 Oct 2020',
      amount: '0.000234 BTC',
    },
    {
      id: 2,
      type: 'ETH Sent',
      icon: (
        <Avatar style={{ backgroundColor: '#FFE0E0' }}>
          <ArrowUpwardIcon style={{ color: '#FF3366' }} />
        </Avatar>
      ),
      status: 'Successful',
      fromTo: 'Jane Smith',
      date: '05 Oct 2020',
      amount: '0.005 ETH',
    },
    {
      id: 3,
      type: 'Swap BTC to LTC',
      icon: (
        <Avatar style={{ backgroundColor: '#E0F7FF' }}>
          <SwapHorizIcon style={{ color: '#33C6FF' }} />
        </Avatar>
      ),
      status: 'Successful',
      fromTo: 'Crypto Exchange',
      date: '05 Oct 2020',
      amount: '0.45 LTC',
    },
  ];

  return (
    <>
      <Typography variant="h6" gutterBottom>
        &nbsp;&nbsp;Last Activities
      </Typography>
      <TableContainer component={Paper}>
        <Table className={classes.lastActivitiesTable} aria-label="last activities table">
          <TableHead>
            <TableRow>
              <TableCell>Type of Action</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>From/To</TableCell>
              <TableCell>Date</TableCell>
              <TableCell align="right">Amount</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {activities.map((activity) => (
              <TableRow key={activity.id}>
                <TableCell component="th" scope="row">
                  <div style={{ display: 'flex', alignItems: 'center' }}>
                    {activity.icon}
                    <Typography style={{ marginLeft: 8 }}>{activity.type}</Typography>
                  </div>
                </TableCell>
                <TableCell>
                  <Chip
                    label={activity.status}
                    size="small"
                    className={`${classes.statusChip} ${activity.status === 'Pending' ? classes.pendingChip : classes.successChip}`}
                  />
                </TableCell>
                <TableCell>{activity.fromTo}</TableCell>
                <TableCell>{activity.date}</TableCell>
                <TableCell align="right">{activity.amount}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

export default LastActivities ;