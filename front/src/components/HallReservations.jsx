import TableContainer from "@material-ui/core/TableContainer";
import Paper from "@material-ui/core/Paper";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import React, {useState} from "react";

export default function HallReservations(props) {
    const {clientId} = props
    const [reservations, setReservations] = useState([{
        hallReservationId: "",
        validUntil: "",
        isPaidAdvance: "",
        isPaidTotal: "",
        advancePrice: "",
        totalPrice: "",
        reservationDate: "",
        screenSize: "",
        hallId: "",
        deleted: "",
    }])
    return (
        <div>
            {reservations.length === 1 && reservations[0].hallReservationId === "" ?
                <h2>No hall reservations found</h2> :
                <TableContainer component={Paper}>
                    <Table aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell />
                                <TableCell>Name</TableCell>
                                <TableCell align="right">Hall number</TableCell>
                                <TableCell align="right">Valid until</TableCell>
                                <TableCell align="right">Paid advance</TableCell>
                                <TableCell align="right">Paid total</TableCell>
                                <TableCell align="right">Advance price</TableCell>
                                <TableCell align="right">Total price</TableCell>
                                <TableCell align="right">Reservation date</TableCell>
                                <TableCell align="right">Screen size</TableCell>
                                <TableCell align="right">Is deleted</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                        </TableBody>
                    </Table>
                </TableContainer>
            }
        </div>
    )
}