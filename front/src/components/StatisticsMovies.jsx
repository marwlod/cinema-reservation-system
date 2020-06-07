import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {
    buildUrl,
    callCrsApi,
    statisticsMovieName,
    statisticsMovieSubUrl,
    toApiDate
} from "./ApiUtils";
import TableHead from "@material-ui/core/TableHead";
import Paper from "@material-ui/core/Paper";


export default function StatisticsMovies(props) {
    const {clientId} = props
    const from = toApiDate(new Date(Date.now() - 30*24*60*60*1000)) // 30 days before now
    const to = toApiDate(new Date(Date.now() - 2*24*60*60*1000)) // 2 days before now
    const [movieStatistics, setMovieStatistics] = useState([{
        showCount: "",
        totalReservationsReservations: "",
        fromDate: "",
        toDate: "",
        incomeGenerated: "",
        deletedReservations: ""
    }])

    useEffect(getMovieStatistics, [])

    function getMovieStatistics() {
        const url = buildUrl(statisticsMovieSubUrl, [statisticsMovieName], {"clientId": clientId, "from": from, "to": to})
        function onSuccess(data) {
            setMovieStatistics(data)
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
                            <TableCell align="right">Shows count</TableCell>
                            <TableCell align="right">Total Reservations</TableCell>
                            <TableCell align="right">First seance</TableCell>
                            <TableCell align="right">Last seance</TableCell>
                            <TableCell align="right">Generated Income</TableCell>
                            <TableCell align="right">Deleted Reservations</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableCell>{movieStatistics.showCount}</TableCell>
                        <TableCell>{movieStatistics.totalReservationsReservations}</TableCell>
                        <TableCell>{movieStatistics.fromDate}</TableCell>
                        <TableCell>{movieStatistics.toDate}</TableCell>
                        <TableCell>{movieStatistics.incomeGenerated}</TableCell>
                        <TableCell>{movieStatistics.deletedReservations}</TableCell>
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
