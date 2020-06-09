import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {
    buildUrl,
    callCrsApi,
    statisticsHallSubUrl
} from "./ApiUtils";
import Paper from "@material-ui/core/Paper";
import TextField from "@material-ui/core/TextField";
import IconButton from "@material-ui/core/IconButton";
import CachedIcon from "@material-ui/icons/Cached";


export default function StatisticsHalls(props) {
    const {clientId} = props
    const defaultHall = "1"
    const [selectedHall, setSelectedHall] = useState(defaultHall);
    const [error, setError] = useState("");
    const [hallStatistics, setHallStatistics] = useState([{
        totalReservations: "",
        incomeGenerated: "",
        deletedReservations: ""
    }])

    useEffect(getHallStatistics, [selectedHall])

    function getHallStatistics() {
        const url = buildUrl(statisticsHallSubUrl, [selectedHall], {"clientId": clientId})
        function onSuccess(data) {
            setError("")
            setHallStatistics(data)
        }
        function onFail(data) {
            setError(data.message)
            console.warn(data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    function onChangeHall(event) {
        const {value} = event.target
        setSelectedHall(value)
    }

    function refresh() {
        getHallStatistics()
    }

    return (
        <div>
            <TextField
                id="hall number"
                label="Hall number"
                type="text"
                value={selectedHall}
                onChange={onChangeHall}
                InputLabelProps={{
                    shrink: true,
                }}
            />
            {
                error !== "" ?
                    <div className="error">{error}</div> :
                    <div>
                        <TableContainer component={Paper}>
                            <IconButton onClick={refresh}>
                                <CachedIcon fontSize="large"/>
                            </IconButton>
                            <Table aria-label="simple table">
                                <TableBody>
                                    <TableRow>
                                        <TableCell>Total Reservations</TableCell>
                                        <TableCell>{hallStatistics.totalReservations}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Generated Income</TableCell>
                                        <TableCell>{hallStatistics.incomeGenerated}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Deleted Reservations</TableCell>
                                        <TableCell>{hallStatistics.deletedReservations}</TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </div>
            }
        </div>
    );
}
