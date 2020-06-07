import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {buildUrl, callCrsApi, hallsSubUrl, loggedInClient} from "./ApiUtils";
import TableHead from "@material-ui/core/TableHead";
import Paper from "@material-ui/core/Paper";
import HallDetails from "./HallDetails";

export default function Halls(props) {
    const {clientId} = props
    const [halls, setHalls] = useState([{
        hallId: "",
        advancePrice: "",
        totalPrice: "",
        screenSize: "",
        regularSeats: "",
        vipSeats: ""
    }])
    useEffect(getHalls, [])

    function getHalls() {
        const url = buildUrl(hallsSubUrl, [], {"clientId": loggedInClient})
        function onSuccess(data) {
/*            data.sort((m1,m2) => m1.hallId.localeCompare(m2.hallId))*/
            console.log(data)
            setHalls(data)
        }
        function onFail(data) {
            console.warn(data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }


    return (
        <div className={props.className}>
            <TableContainer component={Paper}>
                <Table aria-label="collapsible table">
                    <TableHead>
                        <TableRow>
                            <TableCell />
                            <TableCell>Hall ID</TableCell>
                            <TableCell align="right">AdvancePrice [PLN]</TableCell>
                            <TableCell align="right">Total Price [PLN]</TableCell>
                            <TableCell align="right">Screen Size [INCH]</TableCell>
                            <TableCell align="right">Regular Seats</TableCell>
                            <TableCell align="right">VIP Seats</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {halls.map((hall) => (
                            <HallDetails key={hall.hallId} hall={hall} />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
