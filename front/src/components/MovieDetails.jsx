import React, {useState} from "react";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import IconButton from "@material-ui/core/IconButton";
import KeyboardArrowUp from "@material-ui/icons/KeyboardArrowUp";
import KeyboardArrowDown from "@material-ui/icons/KeyboardArrowDown";
import AvailableSeats from "./AvailableSeats";
import {formatDateAndTime, formatMoney} from "./ApiUtils";

export default function MovieDetails(props) {
    const {movie, clientId, isAdmin} = props;
    const [open, setOpen] = useState(false);

    return (
        <React.Fragment>
            <TableRow>
                <TableCell>
                    <IconButton aria-label="expand row" size="small" onClick={() => setOpen(!open)}>
                        {open ? <KeyboardArrowUp /> : <KeyboardArrowDown/>}
                    </IconButton>
                </TableCell>
                <TableCell component="th" scope="row">
                    {movie.name}
                </TableCell>
                <TableCell align="right">{formatDateAndTime(movie.startDate)}</TableCell>
                <TableCell align="right">{formatDateAndTime(movie.endDate)}</TableCell>
                <TableCell align="right">{formatMoney(movie.basePrice)}</TableCell>
                <TableCell align="right">{movie.hallId}</TableCell>
            </TableRow>
            <TableRow>
                <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                    <AvailableSeats open={open} movieId={movie.movieId} clientId={clientId} isAdmin={isAdmin} description={movie.description} link={movie.link}/>
                </TableCell>
            </TableRow>
        </React.Fragment>
    );
}