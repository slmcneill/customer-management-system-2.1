import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getJWTToken } from './restdb';
import './LoginForm.css';

export function LoginForm(props) {
  let [formData, setFormData] = useState({ username: "", password: "" });
  let [status, setStatus] = useState({ status: "init", message: "Enter credentials and click 'login'", token: "" });
  const navigate = useNavigate();

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };

  const onRegisterClick = (formData) => {
    navigate("/register");
  };

  let onLoginClick = async function (credentials) {
    if (credentials === undefined ||
      credentials === null ||
      credentials.username === ""
      || credentials.password === "") {
      alert("Username and password cannot be empty");
      return;
    }
    const response = await getJWTToken(credentials.username, credentials.password);
    setStatus({ status: response.status, message: response.message, token: response.token });
    if (response.status === "error") {
    } else if (response.status === "success") {
      props.setUsername(credentials.username);
      navigate("/app");
    }


  }

  return (
    <div className='login'>

      <h4>Sign In</h4>
      {/* <p>Please enter your username and password to continue.</p>             */}
      
      <form>
        <label htmlFor="username" className="input-label">Username</label>
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

        <label htmlFor="password" className="input-label2">Password</label>
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
          <button type="button" className="loginbutton" onClick={() => onLoginClick(formData)}>Login</button>
        </div>
        <div>
          <a
            href="#"
            className="register-link"
            onClick={e => { e.preventDefault(); onRegisterClick(formData); }}
          >
            Register
          </a>
        </div>

      </form>
    </div>
  )
}

export default LoginForm;