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
} from "./ApiUtils";
import Paper from "@material-ui/core/Paper";


export default function StatisticsMovies(props) {
    const {clientId} = props
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
        const url = buildUrl(statisticsMovieSubUrl, [statisticsMovieName], {"clientId": clientId})
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
                <Table aria-label="simple table">
                    <TableBody>
                        <TableRow>
                            <TableCell align="right">Shows count</TableCell>
                            <TableCell align="left">{movieStatistics.showCount}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Total Reservations</TableCell>
                            <TableCell align="left">{movieStatistics.totalReservationsReservations}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">First seance</TableCell>
                            <TableCell align="left">{movieStatistics.fromDate}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Last seance</TableCell>
                            <TableCell align="left">{movieStatistics.toDate}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Generated Income</TableCell>
                            <TableCell align="left">{movieStatistics.incomeGenerated}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Deleted Reservations</TableCell>
                            <TableCell align="left">{movieStatistics.deletedReservations}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
