import React from "react";
import Button from "@material-ui/core/Button";

function CustomButton(props) {
    return (
        <Button style={props.style}
                onClick={props.onClick}
                variant="contained"
                color="primary"
                type={props.type}>
            {props.text}
        </Button>
    );
}

export default CustomButton;