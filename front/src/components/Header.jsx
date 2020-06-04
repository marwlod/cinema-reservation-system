import React from "react";
import TheatersIcon from '@material-ui/icons/Theaters';
import CustomButton from "./CustomButton";

function Header(props) {
    return (
        <header>
            <h1>
                <TheatersIcon /> AGH Cinema
                {
                    props.showLogoutButton &&
                    <CustomButton className="logoutButton" style={{ float: "right" }} onClick={props.onLogout} type="button" text="Logout"/>
                }
            </h1>
        </header>
  );
}

export default Header;
