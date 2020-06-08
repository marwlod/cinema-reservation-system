import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {
    apiDateNowPlusDays,
    availableCinemaHallsUrl,
    buildUrl,
    callCrsApi,
    reserveHallSubUrl
} from "./ApiUtils";
import TableHead from "@material-ui/core/TableHead";
import Paper from "@material-ui/core/Paper";
import TablePagination from "@material-ui/core/TablePagination";
import Fab from "@material-ui/core/Fab";
import AddIcon from "@material-ui/icons/Add";
import TextField from "@material-ui/core/TextField";

export default function AvailableHalls(props) {
    const {clientId, isAdmin} = props
    const defaultDate = apiDateNowPlusDays(15)
    const [selectedDate, setSelectedDate] = useState(defaultDate);
    const [error, setError] = useState("");
    const [halls, setHalls] = useState([{
        hallId: "",
        advancePrice: "",
        totalPrice: "",
        screenSize: "",
        regularSeats: "",
        vipSeats: ""
    }])
    useEffect(getAvailableHalls, [selectedDate])

    function getAvailableHalls() {
        const url = buildUrl(availableCinemaHallsUrl, [], {"clientId": clientId, "date": selectedDate})
        function onSuccess(data) {
            data.sort((m1,m2) => m1.hallId - m2.hallId)
            console.log("AvailableHalls returned from API: ", data)
            setError("")
            setHalls(data)
        }
        function onFail(data) {
            setError(data.message)
            setHalls([])
            console.warn("Error retrieving halls from API: ", data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    function handleReserveClick(hallId) {
        if (window.confirm('Are you sure you want to reserve this hall?')) {
            reserveHall(hallId)
        } else {
            console.log("User rejected reserving hall")
        }
    }

    function reserveHall(hallId) {
        const url = buildUrl(reserveHallSubUrl, [hallId, selectedDate], {"clientId": clientId})
        function onSuccess(data) {
            console.log("Hall reserved, reservationId: ", data)
            getAvailableHalls()
        }
        function onFail(data) {
            console.log("Error reserving hall: ", data.message)
        }
        callCrsApi(url, {method: 'POST'}, onSuccess, onFail)
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

    function onChangeDate(event) {
        const {value} = event.target
        setSelectedDate(value)
    }

    return (
        <div className={props.className}>
            <TextField
                id="date"
                label="Available halls for date"
                type="date"
                value={selectedDate}
                defaultValue={defaultDate}
                onChange={onChangeDate}
                InputLabelProps={{
                    shrink: true,
                }}
            />
            {
                error !== "" ?
                <div className="error">{error}</div> :
                <div>
                    <TableContainer component={Paper}>
                        <Table aria-label="basic table">
                            <TableHead>
                                <TableRow>
                                    <TableCell align="right">Hall Number</TableCell>
                                    <TableCell align="right">AdvancePrice [PLN]</TableCell>
                                    <TableCell align="right">Total Price [PLN]</TableCell>
                                    <TableCell align="right">Screen Size [INCH]</TableCell>
                                    <TableCell align="right">Regular Seats</TableCell>
                                    <TableCell align="right">VIP Seats</TableCell>
                                    <TableCell align="right">RESERVE</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {halls.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((hall) => (
                                    <TableRow key={hall.hallId}>
                                        <TableCell align="right">{hall.hallId}</TableCell>
                                        <TableCell align="right">{hall.advancePrice}</TableCell>
                                        <TableCell align="right">{hall.totalPrice}</TableCell>
                                        <TableCell align="right">{hall.screenSize}</TableCell>
                                        <TableCell align="right">{hall.regularSeats}</TableCell>
                                        <TableCell align="right">{hall.vipSeats}</TableCell>
                                        {
                                            isAdmin ||
                                            <TableCell align="right">
                                                <Fab color="primary" aria-label="add">
                                                    <AddIcon onClick={() => handleReserveClick(hall.hallId)}/>
                                                </Fab>
                                            </TableCell>
                                        }
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <TablePagination
                        rowsPerPageOptions={[5, 10, 25, 100]}
                        component="div"
                        count={halls.length}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onChangePage={handleChangePage}
                        onChangeRowsPerPage={handleChangeRowsPerPage}
                    />
                </div>
            }
        </div>
    );
}
