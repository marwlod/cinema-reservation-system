import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import Collapse from "@material-ui/core/Collapse";
import React, {useState} from "react";
import {buildUrl, callCrsApi, availableSeatsSubUrl, reserveSeatSubUrl} from "./ApiUtils";
import TablePagination from "@material-ui/core/TablePagination";
import Fab from "@material-ui/core/Fab";
import AddIcon from '@material-ui/icons/Add';

export default function AvailableSeats(props) {
    const {open, movieId, clientId, isAdmin, description, link} = props
    const [seats, setSeats] = useState([{
        seatId: "",
        hallId: "",
        rowNo: "",
        seatNo: "",
        vip: "",
        basePrice: ""
    }])
    function getAvailableSeats() {
        const url = buildUrl(availableSeatsSubUrl, [movieId], {"clientId": clientId})
        function onSuccess(data) {
            console.log("Returned seats from API: ", data)
            setSeats(data)
        }
        function onFail(data) {
            console.log("Error retrieving seats: ", data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    function handleReserveClick(seatId) {
        if (window.confirm('Are you sure you want to reserve this seat?')) {
            reserveSeat(seatId)
        } else {
            console.log("User rejected reserving seat")
        }
    }

    function reserveSeat(seatId) {
        const url = buildUrl(reserveSeatSubUrl, [movieId, seatId], {"clientId": clientId})
        function onSuccess(data) {
            console.log("Seat reserved, reservationId: ", data)
            getAvailableSeats()
        }
        function onFail(data) {
            console.log("Error reserving seat: ", data.message)
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

    return (
        <Collapse in={open} onEnter={getAvailableSeats} timeout="auto" unmountOnExit>
            <br/>
            <Typography variant="h6" gutterBottom component="div">
                Description
            </Typography>
            <Table size="medium">
                <div className="description">
                <TableRow>
                    <TableCell>
                        <div className="descriptionText">
                            <description>{description}</description>
                        </div>
                    </TableCell>
                    <TableCell>
                        <img src={props.link} alt="Movie Image" width="340" height="400"></img>
                    </TableCell>
                </TableRow>
            </div>
            </Table>
            <Box margin={1}>
                <Typography variant="h6" gutterBottom component="div">
                    Free seats
                </Typography>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell>Row number</TableCell>
                            <TableCell>Seat number</TableCell>
                            <TableCell align="right">VIP seat</TableCell>
                            <TableCell align="right">Price [PLN]</TableCell>
                            {
                                isAdmin ||
                                <TableCell align="right">RESERVE</TableCell>
                            }
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {seats.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((seat) => (
                            <TableRow key={seat.seatId}>
                                <TableCell>{seat.rowNo}</TableCell>
                                <TableCell>{seat.seatNo}</TableCell>
                                <TableCell align="right">
                                    {seat.vip ? "YES" : "NO"}
                                </TableCell>
                                <TableCell align="right">
                                    {seat.vip ? 1.5 * seat.basePrice : seat.basePrice}
                                </TableCell>
                                {
                                    isAdmin ||
                                    <TableCell align="right">
                                        <Fab color="primary" aria-label="add">
                                            <AddIcon onClick={() => handleReserveClick(seat.seatId)}/>
                                        </Fab>
                                    </TableCell>
                                }
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <TablePagination
                    rowsPerPageOptions={[5, 10, 25, 100]}
                    component="div"
                    count={seats.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onChangePage={handleChangePage}
                    onChangeRowsPerPage={handleChangeRowsPerPage}
                />
            </Box>
        </Collapse>
    );
}