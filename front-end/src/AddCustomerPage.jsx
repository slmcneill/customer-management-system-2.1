import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function AddCustomerPage() {
  const blankCustomer = { name: '', email: '', password: '' };
  const [formObject, setFormObject] = useState(blankCustomer);
  const navigate = useNavigate();

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormObject((prev) => ({ ...prev, [name]: value }));
  };

  const onSaveClick = () => {
    // TODO: Implement save logic (API call)
    alert('Customer added!');
    setFormObject(blankCustomer);
  };

  const onCancelClick = () => {
    setFormObject(blankCustomer);
  };

  return (
    <div style={{ padding: '40px', textAlign: 'center', display: 'flex', flexDirection: 'column', alignItems: 'center', minHeight: '100vh' }}>
      <a
        href="#"
        className="back-link"
        onClick={e => { e.preventDefault(); navigate(-1); }}
        style={{ display: 'inline-block', marginBottom: '20px' }}
      >
        &larr; Back
      </a>
      <h2>Add Customer</h2>
      <div className="boxed" style={{ textAlign: 'left' }}>
        <form>
          <table id="customer-add-update">
            <tbody>
              <tr>
                <td className={'label'}>Username:</td>
                <td><input
                  type="text"
                  name="name"
                  onChange={handleInputChange}
                  value={formObject.name}
                  placeholder="Customer Username"
                  required /></td>
              </tr>
              <tr>
                <td className={'label'}>Email:</td>
                <td><input
                  type="email"
                  name="email"
                  onChange={handleInputChange}
                  value={formObject.email}
                  placeholder="name@company.com" /></td>
              </tr>
              <tr>
                <td className={'label'}>Pass:</td>
                <td><input
                  type="text"
                  name="password"
                  onChange={handleInputChange}
                  value={formObject.password}
                  placeholder="password" /></td>
              </tr>
              <tr className="button-bar">
                <td colSpan="2">
                  <input type="button" value="Save" onClick={onSaveClick} />
                  <input type="button" value="Cancel" onClick={onCancelClick} />
                </td>
              </tr>
            </tbody>
          </table>
        </form>
      </div>
    </div>
  );
}
