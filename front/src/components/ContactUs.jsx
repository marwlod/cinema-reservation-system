import React from "react";
const year = new Date().getFullYear();
export default function Contact(props) {

    return (

        <div className={props.className}>

            <h2><b>Welcome to AGH Cinema</b></h2><br/><br/>
            <b>email:</b> <i>cinema@agh.com</i><br/>
            <b>Address: </b>ul. Kawiory 21, 30-055 Kraków<br/>
            <b>Phone: </b>tel. 655 123 456<br/><br/><br/>
            <footer>
                <p>Copyright ⓒ {year}</p>
            </footer>
        </div>
    );
}
