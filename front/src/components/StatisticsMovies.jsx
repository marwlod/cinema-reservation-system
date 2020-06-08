import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {
    buildUrl,
    callCrsApi, formatDateAndTime,
    statisticsMovieSubUrl,
} from "./ApiUtils";
import Paper from "@material-ui/core/Paper";
import TextField from "@material-ui/core/TextField";

export default function StatisticsMovies(props) {
    const {clientId} = props
    const defaultMovie = "Pulp Fiction"
    const [selectedMovie, setSelectedMovie] = useState(defaultMovie);
    const [error, setError] = useState("");
    const [movieStatistics, setMovieStatistics] = useState([{
        showCount: "",
        totalReservationsReservations: "",
        fromDate: "",
        toDate: "",
        incomeGenerated: "",
        deletedReservations: ""
    }])

    useEffect(getMovieStatistics, [selectedMovie])

    function getMovieStatistics() {
        const url = buildUrl(statisticsMovieSubUrl, [selectedMovie], {"clientId": clientId})
        function onSuccess(data) {
            console.log("Statistics for movie [", selectedMovie, "] returned from API: ", data)
            setError("")
            setMovieStatistics(data)
        }
        function onFail(data) {
            setError(data.message)
            console.warn(data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    function onChangeMovie(event) {
        const {value} = event.target
        setSelectedMovie(value)
    }

    return (
        <div>
            <TextField
                id="movie name"
                label="Movie name"
                type="text"
                value={selectedMovie}
                onChange={onChangeMovie}
                InputLabelProps={{
                    shrink: true,
                }}
            />
            {
                error !== "" ?
                    <div className="error">{error}</div> :
                    <div>
                        <TableContainer component={Paper}>
                            <Table aria-label="simple table">
                                <TableBody>
                                    <TableRow>
                                        <TableCell>Shows count</TableCell>
                                        <TableCell>{movieStatistics.showCount}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Total Reservations</TableCell>
                                        <TableCell>{movieStatistics.totalReservations}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>First seance</TableCell>
                                        <TableCell>{formatDateAndTime(movieStatistics.fromDate)}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Last seance</TableCell>
                                        <TableCell>{formatDateAndTime(movieStatistics.toDate)}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Generated Income</TableCell>
                                        <TableCell>{movieStatistics.incomeGenerated}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell component="th" scope="row">Deleted Reservations</TableCell>
                                        <TableCell align="right">{movieStatistics.deletedReservations}</TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </div>
            }
        </div>
    );
}
