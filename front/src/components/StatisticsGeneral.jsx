import React, {useEffect, useRef, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {apiDateNowMinusDays, buildUrl, callCrsApi, dayAndMonthNowMinusDays, statisticsGeneralSubUrl} from "./ApiUtils";
import Paper from "@material-ui/core/Paper";
import {LineChart, XAxis, Tooltip, CartesianGrid, Line, ResponsiveContainer, Legend} from "recharts"
import IconButton from "@material-ui/core/IconButton";
import CachedIcon from "@material-ui/icons/Cached";

export default function StatisticsGeneral(props) {
    const {clientId} = props
    const from = apiDateNowMinusDays(30)
    const to = apiDateNowMinusDays(1)
    const defaultStatistics = {
        seatReservations: "",
        hallReservations: "",
        movies: "",
        moneyEarned: "",
        newClientsRegistered: "",
        totalClientsAtTheMoment: "",
        clientsThatReserved: ""
    }
    const [statistics, setStatistics] = useState(defaultStatistics)
    const [lastWeekStatistics, setLastWeekStatistics] = useState([])
    const [oneDayStatistics, setOneDayStatistics] = useState()
    const isInitialMount = useRef(true)

    useEffect(getStatistics, [])
    useEffect(getDailyStatistics, [])
    useEffect(() => {
        if (isInitialMount.current) {
            isInitialMount.current = false;
        } else {
            appendDailyStatistics(oneDayStatistics)
        }
    }, [oneDayStatistics])

    function getStatistics() {
        const url = buildUrl(statisticsGeneralSubUrl, [], {"clientId": clientId, "from": from, "to": to})
        function onSuccess(data) {
            setStatistics(data)
        }
        function onFail(data) {
            console.warn(data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    function getStatisticsFromTo(from, to, day) {
        const url = buildUrl(statisticsGeneralSubUrl, [], {"clientId": clientId, "from": from, "to": to})
        function onSuccess(data) {
            data.day = day
            setOneDayStatistics(data)
        }
        function onFail(data) {
            console.warn(data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    function appendDailyStatistics() {
        setLastWeekStatistics(prev => {
            const newSorted = [...prev, oneDayStatistics]
            newSorted.sort((s1,s2) => s1.day.localeCompare(s2.day))
            return newSorted
        })
    }

    function getDailyStatistics() {
        for (let daysToSubtractFromNow = 8; daysToSubtractFromNow >= 2; daysToSubtractFromNow--) {
            const start = apiDateNowMinusDays(daysToSubtractFromNow);
            const end = apiDateNowMinusDays(daysToSubtractFromNow-1)
            const endDayInReadableFormat = dayAndMonthNowMinusDays(daysToSubtractFromNow-1)
            getStatisticsFromTo(start, end, endDayInReadableFormat)
        }
    }

    function refresh() {
        getStatistics()
    }

    return (
        <div>
            <TableContainer component={Paper}>
                <IconButton onClick={refresh}>
                    <CachedIcon fontSize="large"/>
                </IconButton>
                <div className="chart">
                    <h2>Last week statistics</h2>
                    <br/>
                    <ResponsiveContainer width='100%' height={500}>
                        <LineChart
                            width={500}
                            height={500}
                            data={lastWeekStatistics}
                            margin={{ top: 5, right: 50, left: 50, bottom: 5 }}
                        >
                            <XAxis dataKey="day" interval={0}/>
                            <Tooltip contentStyle={{color: "#edf2f4", backgroundColor: "#3f51b5"}}/>
                            <CartesianGrid stroke="#f5f5f5" />
                            <Line name="Money earned [PLN]" type="monotone" dataKey="moneyEarned" stroke="#ff7300" yAxisId={0} />
                            <Line name="Total movie screenings" type="monotone" dataKey="movies" stroke="#387908" yAxisId={1} />
                            <Line name="Total seat reservations" type="monotone" dataKey="seatReservations" stroke="#ffff00" yAxisId={2} />
                            <Legend layout="horizontal" verticalAlign="bottom" align="center" />
                        </LineChart>
                    </ResponsiveContainer>
                </div>
                <br/>
                <h2>Overall statistics</h2>
                <br/>
                <Table aria-label="simple table">
                    <TableBody>
                        <TableRow>
                            <TableCell align="right">Seat Reservations</TableCell>
                            <TableCell align="left">{statistics.seatReservations}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Hall Reservations</TableCell>
                            <TableCell align="left">{statistics.hallReservations}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Movies</TableCell>
                            <TableCell align="left">{statistics.movies}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Money earned</TableCell>
                            <TableCell align="left">{statistics.moneyEarned}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">New Clients registered</TableCell>
                            <TableCell align="left">{statistics.newClientsRegistered}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Total Clients at the moment</TableCell>
                            <TableCell align="left">{statistics.totalClientsAtTheMoment}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Clients that reserved</TableCell>
                            <TableCell align="left">{statistics.clientsThatReserved}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
