import TableContainer from "@material-ui/core/TableContainer";
import Paper from "@material-ui/core/Paper";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import React, {useEffect, useState} from "react";
import Fab from "@material-ui/core/Fab";
import DeleteIcon from "@material-ui/icons/Delete";
import MonetizationOnIcon from "@material-ui/icons/MonetizationOn";
import {afterNow} from "./CompUtils";
import {
    buildUrl,
    callCrsApi, formatDate, formatDateAndTime, formatMoney, payAdvanceForHallSubUrl,
    payForHallSubUrl,
    reserveHallSubUrl,
    showHallReservationsSubUrl
} from "./ApiUtils";
import CachedIcon from '@material-ui/icons/Cached';
import Table from "@material-ui/core/Table";
import TablePagination from "@material-ui/core/TablePagination";
import IconButton from "@material-ui/core/IconButton";
import CheckIcon from '@material-ui/icons/Check';
import ClearIcon from '@material-ui/icons/Clear';

export default function HallReservations(props) {
    const {clientId} = props
    const from = "2000-01-01"
    const to = "2050-01-01"
    const [reservations, setReservations] = useState([{
        hallReservationId: "",
        validUntil: "",
        paidAdvance: "",
        paidTotal: "",
        advancePrice: "",
        totalPrice: "",
        reservationDate: "",
        screenSize: "",
        hallId: "",
        deleted: "",
    }])
    useEffect(getReservations, [])

    function getReservations() {
        const url = buildUrl(showHallReservationsSubUrl, [], {"clientId": clientId, "from": from, "to": to})
        function onSuccess(data) {
            data.sort((m1,m2) => m2.reservationDate.localeCompare(m1.reservationDate))
            console.log("Hall reservations returned from API: ", data)
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
        const url = buildUrl(reserveHallSubUrl, [reservationId], {"clientId": clientId})
        function onSuccess() {
            console.log("Deleting hall reservation with ID ", reservationId, " successful")
            getReservations()
        }
        function onFail(data) {
            console.warn("Deleting hall reservation failed: ", data.message)
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

    function handleAdvancePayment(reservationId) {
        const url = buildUrl(payAdvanceForHallSubUrl, [reservationId], {"clientId": clientId})
        function onSuccess(data) {
            window.open(data)
            console.log("Opened URL for hall advance payment ", data)
        }
        function onFail(data) {
            console.warn(data.message)
        }
        callCrsApi(url, {method: 'POST'}, onSuccess, onFail)
    }

    function handleFullPayment(reservationId) {
        const url = buildUrl(payForHallSubUrl, [reservationId], {"clientId": clientId})
        function onSuccess(data) {
            window.open(data)
            console.log("Opened URL for hall full payment ", data)
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
                <h2>No hall reservations found</h2> :
                <TableContainer component={Paper}>
                    <IconButton>
                        <CachedIcon fontSize="large" onClick={refresh}/>
                    </IconButton>
                    <Table aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="right">Hall number</TableCell>
                                <TableCell align="right">Reservation date</TableCell>
                                <TableCell align="right">Reservation valid until</TableCell>
                                <TableCell align="right">Advance price [PLN]</TableCell>
                                <TableCell align="right">Paid advance</TableCell>
                                <TableCell align="right">Total price [PLN]</TableCell>
                                <TableCell align="right">Paid total</TableCell>
                                <TableCell align="right">Screen size [inch]</TableCell>
                                <TableCell align="right">Was deleted</TableCell>
                                <TableCell align="right">DELETE</TableCell>
                                <TableCell align="right">PAY ADVANCE</TableCell>
                                <TableCell align="right">PAY FULL</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {reservations.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((reservation) => (
                                <TableRow key={reservation.hallReservationId}>
                                    <TableCell align="right">{reservation.hallId}</TableCell>
                                    <TableCell align="right">{formatDate(reservation.reservationDate)}</TableCell>
                                    <TableCell align="right">{formatDateAndTime(reservation.validUntil)}</TableCell>
                                    <TableCell align="right">{formatMoney(reservation.advancePrice)}</TableCell>
                                    <TableCell align="right">
                                        {
                                            reservation.paidAdvance ?
                                                <CheckIcon fontSize="large"/>:
                                                <ClearIcon fontSize="large"/>
                                        }
                                    </TableCell>
                                    <TableCell align="right">{formatMoney(reservation.totalPrice)}</TableCell>
                                    <TableCell align="right">
                                        {
                                            reservation.paidTotal ?
                                                <CheckIcon fontSize="large"/>:
                                                <ClearIcon fontSize="large"/>
                                        }
                                    </TableCell>
                                    <TableCell align="right">{reservation.screenSize}</TableCell>
                                    <TableCell align="right">{reservation.deleted ? "YES" : "NO"}</TableCell>
                                    <TableCell align="right">
                                        {
                                            afterNow(reservation.validUntil) &&
                                            <Fab color="primary" aria-label="delete">
                                                <DeleteIcon onClick={() => handleDeleteClicked(reservation.hallReservationId)}/>
                                            </Fab>
                                        }
                                    </TableCell>
                                    <TableCell align="right">
                                        {
                                            reservation.paidAdvance || reservation.paidTotal || !afterNow(reservation.validUntil) ||
                                            <Fab color="primary" aria-label="pay">
                                                <MonetizationOnIcon fontSize="medium"
                                                    onClick={() => handleAdvancePayment(reservation.hallReservationId)}/>
                                            </Fab>
                                        }
                                    </TableCell>
                                    <TableCell align="right">
                                        {
                                            reservation.paidTotal || !afterNow(reservation.validUntil) ||
                                            <Fab color="primary" aria-label="pay">
                                                <MonetizationOnIcon fontSize="large"
                                                    onClick={() => handleFullPayment(reservation.hallReservationId)}/>
                                            </Fab>
                                        }
                                    </TableCell>
                                </TableRow>

                            ))}
                        </TableBody>
                    </Table>
                    <TablePagination
                        rowsPerPageOptions={[5, 10, 25, 100]}
                        component="div"
                        count={reservations.length}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onChangePage={handleChangePage}
                        onChangeRowsPerPage={handleChangeRowsPerPage}
                    />
                </TableContainer>
            }
        </div>
    )
}