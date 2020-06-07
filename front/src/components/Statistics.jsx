import React, {useEffect, useState} from "react";
import AppBar from "@material-ui/core/AppBar";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import Box from "@material-ui/core/Box";
import StatisticsGeneral from "./StatisticsGeneral";
import PropTypes from "prop-types";
import StatisticsMovies from "./StatisticsMovies";
import StatisticsHalls from "./StatisticsHalls";

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

export default function Statistics(props) {
    const {clientId} = props
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
                        <Tab label="Show general statistics" {...a11yProps(0)} />
                        <Tab label="Show movies statistics" {...a11yProps(1)} />
                        <Tab label="Show halls statistics" {...a11yProps(2)} />

                    </Tabs>
                    {
                        <TabPanel value={value} index={0}>
                            <StatisticsGeneral clientId={clientId}/>
                        </TabPanel>
                    }
                    {
                        <TabPanel value={value} index={1}>
                            <StatisticsMovies clientId={clientId} />
                        </TabPanel>
                    }
                    {
                        <TabPanel value={value} index={2}>
                            <StatisticsHalls clientId={clientId} />
                        </TabPanel>
                    }
                </AppBar>
            </div>
    );
}
