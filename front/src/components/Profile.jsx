import React, {useEffect, useState} from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import {buildUrl, callCrsApi, profileSubUrl} from "./ApiUtils";
import Paper from "@material-ui/core/Paper";
import CheckIcon from "@material-ui/icons/Check";
import ClearIcon from "@material-ui/icons/Clear";

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
        <div>
            <TableContainer component={Paper}>
                <Table aria-label="simple table">
                    <TableBody>
                        <TableRow key="Name">
                            <TableCell>Name</TableCell>
                            <TableCell>{profile.name}</TableCell>
                        </TableRow>
                        <TableRow key="Surname">
                            <TableCell>Surname</TableCell>
                            <TableCell>{profile.surname}</TableCell>
                        </TableRow>
                        <TableRow key="Email">
                            <TableCell>Email</TableCell>
                            <TableCell>{profile.email}</TableCell>
                        </TableRow>
                        <TableRow key="IsAdmin">
                            <TableCell>Is Admin</TableCell>
                            <TableCell>
                                {
                                    profile.isAdmin ?
                                        <CheckIcon fontSize="large"/>:
                                        <ClearIcon fontSize="large"/>
                                }
                            </TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}
