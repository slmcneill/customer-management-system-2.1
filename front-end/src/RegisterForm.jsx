import { useState } from 'react';
import { registerUser } from './restdb';
import { useNavigate } from 'react-router-dom';
import './RegisterForm.css';

export function RegisterForm(props) {

    const navigate = useNavigate();
    let [formData, setFormData] = useState({ username: "", password: "", email: "" });

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setFormData({ ...formData, [name]: value });
    };

    const onRegisterClick = async (formData) => {     
        const response = await registerUser(formData.username, formData.password, formData.email);
        navigate("/login");
    };

    return (
        <>
        <a
        href="#"
        className="back-link"
        onClick={e => { e.preventDefault(); navigate(-1); }}
      >
        &larr; Back
      </a>
        <div className='register'>

      <h4>Register as a New User</h4>
      {/* <p>Please enter your username and password to continue.</p>             */}
      
      <form>
        <label htmlFor="username" className="register-username">Username</label>
        <div className="text_area">

        <input 
          type="text" 
          name="username"
          placeholder="Enter Username"
          value={formData.username}
          className="text_input"
          onChange={handleInputChange} 
        />
        </div>

        <label htmlFor="email" className="register-email">Email</label>
        <div className="text_area">
          <input 
            type="email" 
            name="email"
            placeholder="Enter Email"
            value={formData.email}
            className="text_input"
            onChange={handleInputChange} 
          />
        </div>

        <label htmlFor="password" className="register-password">Password</label>
        <div className="text_area">
          <input 
            type="password" 
            name="password"
            placeholder="Enter Password"
            value={formData.password}
            className="text_input"
            onChange={handleInputChange} 
          />
        </div>
        <div>
          <button type="button" className="register-button" onClick={() => onRegisterClick(formData)}>Register</button>
        </div>
      </form>
    </div>
        </>
    )
}