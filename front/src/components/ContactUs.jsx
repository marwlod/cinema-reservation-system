import React from "react";

export default function Contact(props) {

    function Footer() {
        const year = new Date().getFullYear();
        return (
            <footer>
                <p>Copyright ⓒ {year}</p>
            </footer>
        );
    }
    return (
        <div className={props.className}>
            <h2><b>Welcome to AGH Cinema</b></h2><br/><br/>
            <b>email:</b> <i>cinema@agh.com</i><br/>
            <b>Address: </b>ul. Kawiory 21, 30-055 Kraków<br/>
            <b>Phone: </b>tel. 655 123 456<br/>
{/*            <ContactUsFooter />*/}
        </div>
    );
}
