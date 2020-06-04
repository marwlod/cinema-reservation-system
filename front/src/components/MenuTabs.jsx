import React, {useState} from 'react';
import PropTypes from 'prop-types';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Box from '@material-ui/core/Box';
import Profile from "./Profile";
import makeStyles from "@material-ui/core/styles/makeStyles";
import Movies from "./Movies";

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

const useStyles = makeStyles({
    root: {
        minWidth: 650,
        backgroundColor: "#a4a3b0",
        textAlign: "center"
    },
});

export default function MenuTabs(props) {
    const classes = useStyles()
    // used for generating tabs
    const [value, setValue] = useState(0);
    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <div className={classes.root}>
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
                    <Tab label="My reservations" {...a11yProps(2)} />
                    <Tab label="My profile" {...a11yProps(3)} />
                    <Tab label="Contact us" {...a11yProps(4)} />
                </Tabs>
            </AppBar>
            <TabPanel value={value} index={0}>
                <Movies />
            </TabPanel>
            <TabPanel value={value} index={1}>
                Halls
            </TabPanel>
            <TabPanel value={value} index={2}>
                My reservations
            </TabPanel>
            <TabPanel value={value} index={3}>
                <Profile clientId={props.clientId}/>
            </TabPanel>
            <TabPanel value={value} index={4}>
                Contact us
            </TabPanel>
        </div>
    );
}