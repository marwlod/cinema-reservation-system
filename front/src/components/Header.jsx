import React from "react";
import TheatersIcon from '@material-ui/icons/Theaters';
import CustomButton from "./CustomButton";

function Header(props) {
    return (
        <header>
            <h1>
                <TheatersIcon />
                Cinema Reservation System
                <span className="logoutButton">{props.showLogoutButton &&
                    <CustomButton style={{ float: "right" }} onClick={props.onLogout} type="button" text="Logout"/>}
                </span>
            </h1>
        </header>
  );
}

export default Header;
