import TableContainer from "@material-ui/core/TableContainer";
import Paper from "@material-ui/core/Paper";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import React, {useEffect, useState} from "react";
import {
    buildUrl,
    callCrsApi,
    formatDateAndTime, formatMoney,
    payForSeatSubUrl,
    reserveSeatSubUrl,
    showSeatReservationsSubUrl
} from "./ApiUtils";
import Fab from "@material-ui/core/Fab";
import DeleteIcon from "@material-ui/icons/Delete";
import TablePagination from "@material-ui/core/TablePagination";
import MonetizationOnIcon from '@material-ui/icons/MonetizationOn';
import {afterNow} from "./CompUtils";
import CachedIcon from "@material-ui/icons/Cached";
import IconButton from "@material-ui/core/IconButton";
import CheckIcon from "@material-ui/icons/Check";
import ClearIcon from "@material-ui/icons/Clear";

export default function SeatReservations(props) {
    const {clientId} = props
    const from = "2000-01-01"
    const to = "2050-01-01"
    const [reservations, setReservations] = useState([{
        seatReservationId: "",
        validUntil: "",
        paid: "",
        totalPrice: "",
        movieName: "",
        startDate: "",
        endDate: "",
        hallId: "",
        rowNumber: "",
        seatNumber: "",
        isVip: "",
        deleted: ""
    }])
    useEffect(getReservations, [])

    function getReservations() {
        const url = buildUrl(showSeatReservationsSubUrl, [], {"clientId": clientId, "from": from, "to": to})
        function onSuccess(data) {
            data.sort((m1,m2) => m2.startDate.localeCompare(m1.startDate))
            console.log("Seat reservations returned from API: ", data)
            setReservations(data)
        }
        function onFail(data) {
            console.warn(data.message)
            setReservations([])
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    function handleDeleteClicked(reservationId) {
        if (window.confirm('Are you sure you want to delete this reservation?')) {
            deleteReservation(reservationId)
        } else {
            console.log("User rejected deleting reservation with ID ", reservationId)
        }
    }

    function deleteReservation(reservationId) {
        const url = buildUrl(reserveSeatSubUrl, [reservationId], {"clientId": clientId})
        function onSuccess() {
            console.log("Deleting reservation with ID ", reservationId, " successful")
            getReservations()
        }
        function onFail(data) {
            console.warn("Deleting failed: ", data.message)
        }
        callCrsApi(url, {method: 'DELETE'}, onSuccess, onFail)
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

    function handlePayment(reservationId) {
        const url = buildUrl(payForSeatSubUrl, [reservationId], {"clientId": clientId})
        function onSuccess(data) {
            window.open(data)
            console.log("Opened URL for payment ", data)
        }
        function onFail(data) {
            console.warn(data.message)
        }
        callCrsApi(url, {method: 'POST'}, onSuccess, onFail)
    }

    function refresh() {
        getReservations()
    }

    return (
        <div>
            {
                reservations.length === 0 ?
                <h2>No movie reservations found</h2> :
                <div>
                    <TableContainer component={Paper}>
                        <IconButton>
                            <CachedIcon fontSize="large" onClick={refresh}/>
                        </IconButton>
                        <Table aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell align="right">Movie name</TableCell>
                                    <TableCell align="right">Start date</TableCell>
                                    <TableCell align="right">End date</TableCell>
                                    <TableCell align="right">Hall number</TableCell>
                                    <TableCell align="right">Row number</TableCell>
                                    <TableCell align="right">Seat number</TableCell>
                                    <TableCell align="right">Reservation valid until</TableCell>
                                    <TableCell align="right">Total price [PLN]</TableCell>
                                    <TableCell align="right">Already paid</TableCell>
                                    <TableCell align="right">Is VIP seat</TableCell>
                                    <TableCell align="right">Was deleted</TableCell>
                                    <TableCell align="right">DELETE</TableCell>
                                    <TableCell align="right">PAY</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {reservations.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((reservation) => (
                                    <TableRow key={reservation.seatReservationId}>
                                        <TableCell align="right">{reservation.movieName}</TableCell>
                                        <TableCell align="right">{formatDateAndTime(reservation.startDate)}</TableCell>
                                        <TableCell align="right">{formatDateAndTime(reservation.endDate)}</TableCell>
                                        <TableCell align="right">{reservation.hallId}</TableCell>
                                        <TableCell align="right">{reservation.rowNumber}</TableCell>
                                        <TableCell align="right">{reservation.seatNumber}</TableCell>
                                        <TableCell align="right">{formatDateAndTime(reservation.validUntil)}</TableCell>
                                        <TableCell align="right">{formatMoney(reservation.totalPrice)}</TableCell>
                                        <TableCell align="right">
                                            {
                                                reservation.paid ?
                                                    <CheckIcon fontSize="large"/>:
                                                    <ClearIcon fontSize="large"/>
                                            }
                                        </TableCell>
                                        <TableCell align="right">{reservation.isVip ? "YES" : "NO"}</TableCell>
                                        <TableCell align="right">{reservation.deleted ? "YES" : "NO"}</TableCell>
                                        <TableCell align="right">
                                            {
                                                afterNow(reservation.validUntil) &&
                                                <Fab color="primary" aria-label="delete">
                                                    <DeleteIcon onClick={() => handleDeleteClicked(reservation.seatReservationId)}/>
                                                </Fab>
                                            }
                                        </TableCell>
                                        <TableCell align="right">
                                            {
                                                reservation.paid || !afterNow(reservation.validUntil) ||
                                                <Fab color="primary" aria-label="pay">
                                                    <MonetizationOnIcon onClick={() => handlePayment(reservation.seatReservationId)}/>
                                                </Fab>
                                            }
                                        </TableCell>
                                    </TableRow>

                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <TablePagination
                        rowsPerPageOptions={[5, 10, 25, 100]}
                        component="div"
                        count={reservations.length}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onChangePage={handleChangePage}
                        onChangeRowsPerPage={handleChangeRowsPerPage}
                    />
                </div>
            }
        </div>
    )
}