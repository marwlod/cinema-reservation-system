import React, {useState} from "react";
import LoginForm from "./LoginForm";
import Header from "./Header";
import Footer from "./Footer";
import {baseUrl, login} from "./ApiConstants";

function App() {
    const [isLoggedIn, setLoggedIn] = useState(false);
    const [error, setError] = useState("")

    function afterLogin(loginDetails) {
        fetch(baseUrl + login + '?email=' + loginDetails.username +
            '&password=' + loginDetails.password, {method: 'POST'})
            .then(res => res.json())
            .then((data) => {
                console.log(data)
                if (data.hasOwnProperty("status") && data.hasOwnProperty("message")) {
                    setError(data.message)
                } else {
                    setLoggedIn(true)
                }
            })
            .catch(console.log);
    }

    function afterLogout() {
        setLoggedIn(false)
    }

    return (
        <div>
            <Header showLogoutButton={isLoggedIn} onLogout={afterLogout}/>
            <div className="container">
                {isLoggedIn ? "Hello" : <LoginForm onLogin={afterLogin} error={error}/>}
            </div>
            <Footer />
        </div>
    );
}

export default App;
