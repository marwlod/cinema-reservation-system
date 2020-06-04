import React, {useState} from "react";
import CustomButton from "./CustomButton";

function LoginForm(props) {
    const [loginDetails, setLoginDetails] = useState({
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

    function onSubmit(event) {
        props.onLogin(loginDetails);
        event.preventDefault();
    }

    return (
        <div className="container">
            <form className="form">
                <input name="email" onChange={handleChange} value={loginDetails.email} type="text" placeholder="Email" />
                <input name="password" onChange={handleChange} value={loginDetails.password} type="password" placeholder="Password" />
                <CustomButton onClick={onSubmit} type="button" text="Login"/>
                <div className="error">{props.error}</div>
            </form>
        </div>
    );
}

export default LoginForm;
