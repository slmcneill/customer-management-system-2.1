import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './banner.css';

function Banner(props) {
    const navigate = useNavigate();
    const location = useLocation();

    const onLogoutClick = function () {
        if (window.confirm("Do you want to log out?")) {
            props.setUsername("");
            navigate("/login");
        }
    };

    return (
        <div className="banner">
            <div className="banner-text">
                <span className="banner-title">Seashells</span>
                <span className="banner-subtitle">Customer Information Management</span>
            </div>
            <button className='floatR icon-button'
                onClick={onLogoutClick}
                title="Click here to log out!" >
                <img
                    src="/person.png"
                    alt="Person"
                    style={{ width: '24px', height: '24px' }}
                />
                {props.username}
            </button>
        </div>
    );
}

export default Banner;