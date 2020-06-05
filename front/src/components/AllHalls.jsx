import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import Collapse from "@material-ui/core/Collapse";
import React, {useState} from "react";
import {buildUrl, callCrsApi, hallsSubUrl, loggedInClient} from "./ApiUtils";

export default function AllHalls(props) {
    const {open, hallId} = props
    const [halls, setHalls] = useState([{
        hallId: "",
        advancePrice: "",
        totalPrice: "",
        screenSize: "",
        regularSeats: "",
        vipSeats: ""
    }])
    function getFreeSeats() {
        const url = buildUrl(hallsSubUrl, [hallId], {"clientId": loggedInClient})
        function onSuccess(data) {
            console.log("Returned halls from API: ", data)
            setHalls(data)
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
                    All halls
                </Typography>
                <Table size="small" aria-label="purchases">
                    <TableHead>
                        <TableRow>
                            <TableCell>Hall number</TableCell>
                            <TableCell>AdvancePrice</TableCell>
                            <TableCell>Total Price</TableCell>
                            <TableCell>Screen Size</TableCell>
                            <TableCell>Regular Seats</TableCell>
                            <TableCell>VIP Seats</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {halls.map((hall) => (
                            <TableRow key={hall.hallId}>
                                <TableCell>{hall.hallId}</TableCell>
                                <TableCell>{hall.advancePrice}</TableCell>
                                <TableCell>{hall.totalPrice}</TableCell>
                                <TableCell>{hall.screenSize}</TableCell>
                                <TableCell>{hall.regularSeats}</TableCell>
                                <TableCell>{hall.vipSeats}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </Box>
        </Collapse>
    );
}