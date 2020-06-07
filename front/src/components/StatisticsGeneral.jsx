import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {buildUrl, callCrsApi, statisticsGeneralSubUrl, toApiDate} from "./ApiUtils";
import TableHead from "@material-ui/core/TableHead";
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
/*            data.sort((m1,m2) => m1.startDate.localeCompare(m2.startDate))*/
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
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell align="right">Seat Reservations</TableCell>
                            <TableCell align="right">Hall Reservations</TableCell>
                            <TableCell align="right">Movies</TableCell>
                            <TableCell align="right">Money earned</TableCell>
                            <TableCell align="right">New Clients registered</TableCell>
                            <TableCell align="right">Total Clients at the moment</TableCell>
                            <TableCell align="right">Clients that reserved</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableCell>{statistics.seatReservations}</TableCell>
                        <TableCell>{statistics.hallReservations}</TableCell>
                        <TableCell>{statistics.movies}</TableCell>
                        <TableCell>{statistics.moneyEarned}</TableCell>
                        <TableCell>{statistics.newClientsRegistered}</TableCell>
                        <TableCell>{statistics.totalClientsAtTheMoment}</TableCell>
                        <TableCell>{statistics.clientsThatReserved}</TableCell>
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
