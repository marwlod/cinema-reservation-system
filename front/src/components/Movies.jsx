import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {apiDateNow, apiDateNowPlusDays, buildUrl, callCrsApi, moviesSubUrl} from "./ApiUtils";
import TableHead from "@material-ui/core/TableHead";
import Paper from "@material-ui/core/Paper";
import MovieDetails from "./MovieDetails";
import TablePagination from "@material-ui/core/TablePagination";

export default function Movies(props) {
    const {clientId, isAdmin} = props
    const from = apiDateNow()
    const to = apiDateNowPlusDays(30)
    const [movies, setMovies] = useState([{
        movieId: "",
        name: "",
        startDate: "",
        endDate: "",
        basePrice: "",
        hallId: "",
        description: "",
        link: ""
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

    // pagination
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };
    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

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
                        {movies.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((movie) => (
                            <MovieDetails key={movie.movieId} movie={movie} clientId={clientId} isAdmin={isAdmin}/>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <TablePagination
                rowsPerPageOptions={[5, 10, 25, 100]}
                component="div"
                count={movies.length}
                rowsPerPage={rowsPerPage}
                page={page}
                onChangePage={handleChangePage}
                onChangeRowsPerPage={handleChangeRowsPerPage}
            />
        </div>
    );
}
