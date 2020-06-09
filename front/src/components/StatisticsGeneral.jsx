import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {apiDateNowMinusDays, buildUrl, callCrsApi, formatMoney, statisticsGeneralSubUrl, toApiDate} from "./ApiUtils";
import Paper from "@material-ui/core/Paper";
import IconButton from "@material-ui/core/IconButton";
import CachedIcon from "@material-ui/icons/Cached";


export default function StatisticsGeneral(props) {
    const {clientId} = props
    const from = apiDateNowMinusDays(30)
    const to = apiDateNowMinusDays(1)
    const [statistics, setStatistics] = useState([{
        seatReservations: "",
        hallReservations: "",
        movies: "",
        moneyEarned: "",
        newClientsRegistered: "",
        totalClientsAtTheMoment: "",
        clientsThatReserved: ""
    }])

    useEffect(getStatistics, [])

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

    function refresh() {
        getStatistics()
    }

    return (
        <div>
            <TableContainer component={Paper}>
                <IconButton>
                    <CachedIcon fontSize="large" onClick={refresh}/>
                </IconButton>
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
                            <TableCell align="left">{formatMoney(statistics.moneyEarned)}</TableCell>
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
