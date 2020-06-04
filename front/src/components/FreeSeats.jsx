import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import Collapse from "@material-ui/core/Collapse";
import React, {useState} from "react";
import {buildUrl, callCrsApi, freeSeatsSubUrl, loggedInClient} from "./ApiUtils";

export default function FreeSeats(props) {
    const {open, movieId} = props
    const [seats, setSeats] = useState([{
        seatId: "",
        hallId: "",
        rowNo: "",
        seatNo: "",
        vip: "",
        basePrice: ""
    }])
    function getFreeSeats() {
        const url = buildUrl(freeSeatsSubUrl, [movieId], {"clientId": loggedInClient})
        function onSuccess(data) {
            console.log("Returned seats from API: ", data)
            setSeats(data)
        }
        function onFail(data) {
            console.log("Error retrieving seats: ", data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    return (
        <Collapse in={open} onEnter={getFreeSeats} timeout="auto" unmountOnExit>
            <Box margin={1}>
                <Typography variant="h6" gutterBottom component="div">
                    Free seats
                </Typography>
                <Table size="small" aria-label="purchases">
                    <TableHead>
                        <TableRow>
                            <TableCell>Row number</TableCell>
                            <TableCell>Seat number</TableCell>
                            <TableCell align="right">is VIP</TableCell>
                            <TableCell align="right">Price [PLN]</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {seats.map((seat) => (
                            <TableRow key={seat.seatId}>
                                <TableCell>{seat.rowNo}</TableCell>
                                <TableCell>{seat.seatNo}</TableCell>
                                <TableCell align="right">
                                    {seat.vip ? "YES" : "NO"}
                                </TableCell>
                                <TableCell align="right">
                                    {seat.vip ? 1.5 * seat.basePrice : seat.basePrice}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </Box>
        </Collapse>
    );
}