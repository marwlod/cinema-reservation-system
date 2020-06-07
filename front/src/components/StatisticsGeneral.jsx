import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {buildUrl, callCrsApi, statisticsGeneralSubUrl, toApiDate} from "./ApiUtils";
import Paper from "@material-ui/core/Paper";


export default function StatisticsGeneral(props) {
    const {clientId} = props
    const from = toApiDate(new Date(Date.now() - 30*24*60*60*1000)) // 30 days before now
    const to = toApiDate(new Date(Date.now() - 2*24*60*60*1000)) // 2 days before now
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

    return (
        <div className={props.className}>
            <TableContainer component={Paper}>
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
