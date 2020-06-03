import React, {useState} from "react";
import CustomButton from "./CustomButton";

function LoginForm(props) {
    const [loginDetails, setLoginDetails] = useState({
        username: "",
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

    function onSubmit(event) {
        props.onLogin(loginDetails);
        event.preventDefault();
    }

    return (
        <form className="form">
            <input name="username" onChange={handleChange} value={loginDetails.username} type="text" placeholder="Username" />
            <input name="password" onChange={handleChange} value={loginDetails.password} type="password" placeholder="Password" />
            <CustomButton onClick={onSubmit} type="submit" text="Login"/>
            <div className="error">{props.error}</div>
        </form>
    );
}

export default LoginForm;
