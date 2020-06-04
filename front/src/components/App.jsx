import React, {useState} from "react";
import LoginForm from "./LoginForm";
import Header from "./Header";
import Footer from "./Footer";
import {buildUrl, callCrsApi, loginSubUrl} from "./ApiUtils";
import MenuTabs from "./MenuTabs";

function App() {
    const [isLoggedIn, setLoggedIn] = useState(true)
    const [error, setError] = useState("")
    const [clientId, setClientId] = useState()

    function afterLogin(loginDetails) {
        const url = buildUrl(loginSubUrl, [],
            {"email": loginDetails.email, "password": loginDetails.password})
        const params = {method: 'POST'}
        function onSuccess(dataReturned) {
            if (dataReturned.hasOwnProperty("clientId")) {
                setClientId(dataReturned.clientId)
                setLoggedIn(true)
            }
        }
        function onFail(dataReturned) {
            setError(dataReturned.message)
            setLoggedIn(false)
        }
        callCrsApi(url, params, onSuccess, onFail)
    }

    function afterLogout() {
        setLoggedIn(false)
    }

    return (
        <div>
            <Header showLogoutButton={isLoggedIn} onLogout={afterLogout}/>
            {isLoggedIn ?
                <MenuTabs clientId={clientId}/> :
                <LoginForm onLogin={afterLogin} error={error}/>
            }
            <Footer />
        </div>
    )
}

export default App;
