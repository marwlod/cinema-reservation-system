import React, {useState} from "react";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import IconButton from "@material-ui/core/IconButton";
import KeyboardArrowUp from "@material-ui/icons/KeyboardArrowUp";
import KeyboardArrowDown from "@material-ui/icons/KeyboardArrowDown";
import AllHalls from "./AllHalls";
import AvailableHalls from "./AvailableHalls";

export default function HallDetails(props) {
    const {hall} = props;
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
                    {hall.hallId}
                </TableCell>
                <TableCell align="right">{hall.advancePrice}</TableCell>
                <TableCell align="right">{hall.totalPrice}</TableCell>
                <TableCell align="right">{hall.screenSize}</TableCell>
                <TableCell align="right">{hall.regularSeats}</TableCell>
                <TableCell align="right">{hall.vipSeats}</TableCell>
            </TableRow>
            <TableRow>
                <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                 {/*   <AllHalls open={open} hallId={hall.hallId} />*/}
                 <AvailableHalls open={open} hallId={hall.hallId} />
                </TableCell>
            </TableRow>
        </React.Fragment>
    );
}