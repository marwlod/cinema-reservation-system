import React, {useState} from 'react';
import PropTypes from 'prop-types';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Box from '@material-ui/core/Box';
import Profile from "./Profile";
import Movies from "./Movies";
import SeatReservations from "./SeatReservations";
import HallReservations from "./HallReservations";

function TabPanel(props) {
    const { children, value, index, ...other } = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`scrollable-auto-tabpanel-${index}`}
            aria-labelledby={`scrollable-auto-tab-${index}`}
            {...other}
        >
            {value === index && (
                <Box p={3}>
                    <div>{children}</div>
                </Box>
            )}
        </div>
    );
}

TabPanel.propTypes = {
    children: PropTypes.node,
    index: PropTypes.any.isRequired,
    value: PropTypes.any.isRequired,
};

function a11yProps(index) {
    return {
        id: `scrollable-auto-tab-${index}`,
        'aria-controls': `scrollable-auto-tabpanel-${index}`,
    };
}

export default function MenuTabs(props) {
    const {clientId} = props
    // used for generating tabs
    const [value, setValue] = useState(0);
    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <div className="menuTabs">
            <AppBar position="static" color="default">
                <Tabs
                    value={value}
                    onChange={handleChange}
                    indicatorColor="primary"
                    textColor="primary"
                    variant="fullWidth"
                    aria-label="Menu Bar"
                >
                    <Tab label="Show movies" {...a11yProps(0)} />
                    <Tab label="Show halls" {...a11yProps(1)} />
                    <Tab label="My movie reservations" {...a11yProps(2)} />
                    <Tab label="My hall reservations" {...a11yProps(3)} />
                    <Tab label="My profile" {...a11yProps(4)} />
                    <Tab label="Contact us" {...a11yProps(5)} />
                </Tabs>
            </AppBar>
            <TabPanel value={value} index={0}>
                <Movies clientId={clientId} />
            </TabPanel>
            <TabPanel value={value} index={1}>
                Halls
            </TabPanel>
            <TabPanel value={value} index={2}>
                <SeatReservations clientId={clientId}/>
            </TabPanel>
            <TabPanel value={value} index={3}>
                <HallReservations clientId={clientId}/>
            </TabPanel>
            <TabPanel value={value} index={4}>
                <Profile clientId={clientId}/>
            </TabPanel>
            <TabPanel value={value} index={5}>
                Contact us
            </TabPanel>
        </div>
    );
}