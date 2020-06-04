import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {buildUrl, callCrsApi, profileSubUrl} from "./ApiUtils";
import Paper from "@material-ui/core/Paper";

export default function Profile(props) {
    const [profile, setProfile] = useState({
        name: "",
        surname: "",
        email: "",
        isAdmin: ""
    })
    // will be called only once when profile tab is opened
    useEffect(getProfile, [])

    function getProfile() {
        const url = buildUrl(profileSubUrl, [], {"clientId": props.clientId})
        function onSuccess(data) {
            setProfile(data)
        }
        function onFail(data) {
            console.warn("Error when getting profile: ", data.message)
        }
        callCrsApi(url, {method: 'GET'}, onSuccess, onFail)
    }

    return (
        <div className={props.className}>
            <TableContainer component={Paper}>
                <Table aria-label="simple table">
                    <TableBody>
                        <TableRow key="Name">
                            <TableCell component="th" scope="row">Name</TableCell>
                            <TableCell align="right">{profile.name}</TableCell>
                        </TableRow>
                        <TableRow key="Surname">
                            <TableCell component="th" scope="row">Surname</TableCell>
                            <TableCell align="right">{profile.surname}</TableCell>
                        </TableRow>
                        <TableRow key="Email">
                            <TableCell component="th" scope="row">Email</TableCell>
                            <TableCell align="right">{profile.email}</TableCell>
                        </TableRow>
                        <TableRow key="IsAdmin">
                            <TableCell component="th" scope="row">IsAdmin</TableCell>
                            <TableCell align="right">{profile.isAdmin ? "YES" : "NO"}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
