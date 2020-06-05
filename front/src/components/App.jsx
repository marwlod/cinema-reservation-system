import React, {useState} from "react";
import LoginForm from "./LoginForm";
import Header from "./Header";
import MenuTabs from "./MenuTabs";

function App() {
    const [isLoggedIn, setLoggedIn] = useState(false)
    const [clientId, setClientId] = useState()

    function afterLogout() {
        setLoggedIn(false)
    }

    return (
        <div>
            <Header showLogoutButton={isLoggedIn} onLogout={afterLogout}/>
            {isLoggedIn ?
                <MenuTabs clientId={clientId}/> :
                <LoginForm setClientId={setClientId} setLoggedIn={setLoggedIn}/>
            }
        </div>
    )
}

export default App;
