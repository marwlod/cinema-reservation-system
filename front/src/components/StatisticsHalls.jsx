import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {
    buildUrl,
    callCrsApi,
    statisticsHallId,
    statisticsHallSubUrl
} from "./ApiUtils";
import Paper from "@material-ui/core/Paper";


export default function StatisticsHalls(props) {
    const {clientId} = props
    const [hallStatistics, setHallStatistics] = useState([{
        totalReservations: "",
        incomeGenerated: "",
        deletedReservations: ""
    }])

    useEffect(getHallStatistics, [])

    function getHallStatistics() {
        const url = buildUrl(statisticsHallSubUrl, [statisticsHallId], {"clientId": clientId})
        function onSuccess(data) {
            setHallStatistics(data)
        }
        function onFail(data) {
            console.warn(data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    return (
        <div className={props.className}>
            <TableContainer component={Paper}>
                <Table aria-label="simple table">
                    <TableBody>
                        <TableRow>
                            <TableCell align="right">Total Reservations</TableCell>
                            <TableCell align="left">{hallStatistics.totalReservations}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Generated Income</TableCell>
                            <TableCell align="left">{hallStatistics.incomeGenerated}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="right">Deleted Reservations</TableCell>
                            <TableCell align="left">{hallStatistics.deletedReservations}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
