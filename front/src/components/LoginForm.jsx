import React, {useState} from "react";
import CustomButton from "./CustomButton";
import {buildUrl, callCrsApi, loginSubUrl, registerSubUrl} from "./ApiUtils";

export default function LoginForm(props) {
    const {setClientId, setLoggedIn} = props
    const [isRegister, setRegister] = useState(false)
    const [error, setError] = useState("")
    const [successMessage, setSuccessMessage] = useState("")
    const [loginDetails, setLoginDetails] = useState({
        name: "",
        surname: "",
        email: "",
        password: ""
    })

    function handleChange(event) {
        const { name, value } = event.target;

        setLoginDetails(prevDetails => {
            return {
                ...prevDetails,
                [name]: value
            };
        });
    }

    function afterLogin(loginDetails) {
        const url = buildUrl(loginSubUrl, [],
            {"email": loginDetails.email, "password": loginDetails.password})
        const params = {method: 'POST'}
        function onSuccess(dataReturned) {
            if (dataReturned.hasOwnProperty("clientId")) {
                setClientId(dataReturned.clientId)
                setError("")
                setSuccessMessage("Successfully logged in")
                setLoggedIn(true)
            }
        }
        function onFail(dataReturned) {
            setError(dataReturned.message)
            setSuccessMessage("")
            setLoggedIn(false)
        }
        callCrsApi(url, params, onSuccess, onFail)
    }

    function afterRegister(loginDetails) {
        console.log(loginDetails)
        const urlParams = {
            "name": loginDetails.name,
            "surname": loginDetails.surname,
            "email": loginDetails.email,
            "password": loginDetails.password,
        }
        const url = buildUrl(registerSubUrl, [], urlParams)
        const params = {method: 'POST'}
        function onSuccess(data) {
            console.log("Client with ID ", data, " successfully registered")
            setError("")
            setSuccessMessage("Registration successful, you can now log in")
        }
        function onFail(data) {
            setError(data.message)
            setSuccessMessage("")
        }
        callCrsApi(url, params, onSuccess, onFail)
    }

    function onSubmitLogin(event) {
        afterLogin(loginDetails);
        event.preventDefault();
    }

    function onSubmitRegister(event) {
        afterRegister(loginDetails);
        event.preventDefault();
    }

    function clickedRegister() {
        console.log("clicked register")
        setSuccessMessage("")
        setError("")
        setRegister(true)
    }

    function clickedLogin() {
        console.log("clicked login")
        setSuccessMessage("")
        setError("")
        setRegister(false)
    }

    return (
        <div className="container">
            <form className="form">
                {
                    isRegister &&
                    <input name="name" onChange={handleChange} value={loginDetails.name} type="text" placeholder="Name" />
                }
                {
                    isRegister &&
                    <input name="surname" onChange={handleChange} value={loginDetails.surname} type="text" placeholder="Surname" />
                }
                <input name="email" onChange={handleChange} value={loginDetails.email} type="text" placeholder="Email" />
                <input name="password" onChange={handleChange} value={loginDetails.password} type="password" placeholder="Password" />
                {
                    isRegister ?
                    <CustomButton onClick={onSubmitRegister} type="button" text="Register"/> :
                    <CustomButton onClick={onSubmitLogin} type="button" text="Login"/>
                }
                <div className="error">{error}</div>
                <div className="successMessage">{successMessage}</div>
                <br/>
                {
                    isRegister ?
                    <h2 className="regularMessage">Already have an account?</h2> :
                    <h2 className="regularMessage">Do not have an account?</h2>
                }
                {
                    isRegister ?
                    <CustomButton onClick={clickedLogin} type="button" text="Login"/> :
                    <CustomButton onClick={clickedRegister} type="button" text="Register"/>
                }
            </form>
        </div>
    );
}
