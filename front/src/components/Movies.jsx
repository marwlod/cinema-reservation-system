import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {buildUrl, callCrsApi, moviesSubUrl, toApiDate} from "./ApiUtils";
import TableHead from "@material-ui/core/TableHead";
import Paper from "@material-ui/core/Paper";
import MovieDetails from "./MovieDetails";

export default function Movies(props) {
    const {clientId} = props
    const from = toApiDate(new Date())
    const to = toApiDate(new Date(Date.now() + 30*24*60*60*1000)) // add 30 days
    const [movies, setMovies] = useState([{
        movieId: "",
        name: "",
        startDate: "",
        endDate: "",
        basePrice: "",
        hallId: ""
    }])
    useEffect(getMovies, [])

    function getMovies() {
        const url = buildUrl(moviesSubUrl, [], {"from": from, "to": to})
        function onSuccess(data) {
            data.sort((m1,m2) => m1.startDate.localeCompare(m2.startDate))
            setMovies(data)
        }
        function onFail(data) {
            console.warn(data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    return (
        <div className={props.className}>
            <TableContainer component={Paper}>
                <Table aria-label="collapsible table">
                    <TableHead>
                        <TableRow>
                            <TableCell />
                            <TableCell>Name</TableCell>
                            <TableCell align="right">Start date</TableCell>
                            <TableCell align="right">End date</TableCell>
                            <TableCell align="right">Base price [PLN]</TableCell>
                            <TableCell align="right">Hall number</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {movies.map((movie) => (
                            <MovieDetails key={movie.movieId} movie={movie} clientId={clientId}/>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
