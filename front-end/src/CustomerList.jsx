import React, { useState, useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrashCan, faPen, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import Seashell from './Seashell';
import './CustomerList.css';

export default function CustomerListPage() {
  const [customers, setCustomers] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [customersPerPage] = useState(5);
  const [visiblePasswords, setVisiblePasswords] = useState({});
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [customerToDelete, setCustomerToDelete] = useState(null);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [formCustomer, setFormCustomer] = useState({ id: null, name: '', email: '', password: '' });
  const [userName, setUserName] = useState('admin');
  const [searchQuery, setSearchQuery] = useState('');

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = () => {
    fetch('http://localhost:8080/api/customers')
      .then(res => res.json())
      .then(data => setCustomers(data))
      .catch(err => console.error(err));
  };

  const togglePasswordVisibility = (id) => {
    setVisiblePasswords(prev => ({ ...prev, [id]: !prev[id] }));
  };

  const handleAddCustomer = () => {
    setFormCustomer({ id: null, name: '', email: '', password: '' });
    setShowAddModal(true);
  };

  const handleEditCustomer = (customer) => {
    setFormCustomer(customer);
    setShowUpdateModal(true);
  };

  const handleDeleteCustomer = (id) => {
    setCustomerToDelete(id);
    setShowDeleteModal(true);
  };

  const confirmDeleteCustomer = () => {
    fetch(`http://localhost:8080/api/customers/${customerToDelete}`, { method: 'DELETE' })
      .then(res => {
        if (res.ok) {
          setCustomers(customers.filter(c => c.id !== customerToDelete));
          setCustomerToDelete(null);
          setShowDeleteModal(false);
        } else alert('Delete failed');
      })
      .catch(console.error);
  };

  const saveCustomer = () => {
    const method = formCustomer.id ? 'PUT' : 'POST';
    const url = formCustomer.id 
      ? `http://localhost:8080/api/customers/${formCustomer.id}` 
      : 'http://localhost:8080/api/customers';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formCustomer)
    })
    .then(res => res.json())
    .then(() => {
      fetchCustomers();
      setShowAddModal(false);
      setShowUpdateModal(false);
    })
    .catch(console.error);
  };

  // Filter customers based on search query
  const filteredCustomers = customers.filter((customer) => {
    const query = searchQuery.toLowerCase();
    return (
      customer.name.toLowerCase().includes(query) ||
      customer.email.toLowerCase().includes(query) ||
      String(customer.id).includes(query)
    );
  });

  // Pagination
  const indexOfLastCustomer = currentPage * customersPerPage;
  const indexOfFirstCustomer = indexOfLastCustomer - customersPerPage;
  const currentCustomers = filteredCustomers.slice(indexOfFirstCustomer, indexOfLastCustomer);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <>
      {/* Floating Seashells */}
      <Seashell className="shell-top-left" animationDuration={6} animationDelay={0} />
      <Seashell className="shell-top-right" animationDuration={8} animationDelay={2} />
      <Seashell className="shell-bottom-left" animationDuration={7} animationDelay={4} />
      <Seashell className="shell-bottom-right" animationDuration={9} animationDelay={1} />

      <div className="header">
        <h2>Customer Management Dashboard</h2>
        <h3>Welcome, {userName}!</h3>
      </div>

      {/* Search Bar */}
      <div className="search-container">
        <input
          type="text"
          placeholder="Search by username, email, or ID..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="search-input"
        />
      </div>

      {/* Add Button */}
      <div className="add-button-container">
        <button className="add-btn" onClick={handleAddCustomer}>Add Customer</button>
      </div>

      {/* Customer Table */}
      <table className="customer-list-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Password</th>
            <th>ID</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {currentCustomers.map((customer) => (
            <tr key={customer.id}>
              <td>{customer.name}</td>
              <td>{customer.email}</td>
              <td className="password-cell">
                {visiblePasswords[customer.id] ? customer.password : '••••••••'}
                <FontAwesomeIcon
                  icon={visiblePasswords[customer.id] ? faEyeSlash : faEye}
                  className="password-toggle"
                  onClick={() => togglePasswordVisibility(customer.id)}
                />
              </td>
              <td>{customer.id}</td>
              <td>
                <FontAwesomeIcon icon={faPen} className="icon-button" onClick={() => handleEditCustomer(customer)} />
                <FontAwesomeIcon icon={faTrashCan} className="icon-button" onClick={() => handleDeleteCustomer(customer.id)} />
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Pagination */}
      <Pagination
        customersPerPage={customersPerPage}
        totalCustomers={filteredCustomers.length}
        paginate={paginate}
        currentPage={currentPage}
      />

      {/* Delete Modal */}
      {showDeleteModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Confirm Delete</h3>
            <p>Are you sure you want to delete this customer?</p>
            <div className="modal-footer">
              <button onClick={confirmDeleteCustomer}>Confirm</button>
              <button onClick={() => setShowDeleteModal(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}

      {/* Add/Update Modal */}
      {(showAddModal || showUpdateModal) && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>{formCustomer.id ? 'Update Customer' : 'Add Customer'}</h3>
            <div className="modal-body">
              <div className="form-field">
                <label>Username:</label>
                <input
                  type="text"
                  value={formCustomer.name}
                  onChange={(e) => setFormCustomer({...formCustomer, name: e.target.value})}
                />
              </div>
              <div className="form-field">
                <label>Email:</label>
                <input
                  type="email"
                  value={formCustomer.email}
                  onChange={(e) => setFormCustomer({...formCustomer, email: e.target.value})}
                />
              </div>
              <div className="form-field">
                <label>Password:</label>
                <input
                  type="text"
                  value={formCustomer.password}
                  onChange={(e) => setFormCustomer({...formCustomer, password: e.target.value})}
                />
              </div>
            </div>
            <div className="modal-footer">
              <button onClick={saveCustomer}>{formCustomer.id ? 'Update' : 'Add'}</button>
              <button onClick={() => { setShowAddModal(false); setShowUpdateModal(false); }}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

// Advanced Pagination Component
function Pagination({ customersPerPage, totalCustomers, paginate, currentPage }) {
  const totalPages = Math.ceil(totalCustomers / customersPerPage);
  const pageNumbers = [];

  for (let i = 1; i <= totalPages; i++) pageNumbers.push(i);

  const renderPageNumbers = () => {
    const delta = 2; // number of pages to show around current
    const range = [];
    const rangeWithDots = [];
    let l;

    for (let i = 1; i <= totalPages; i++) {
      if (i === 1 || i === totalPages || (i >= currentPage - delta && i <= currentPage + delta)) {
        range.push(i);
      }
    }

    for (let i of range) {
      if (l) {
        if (i - l === 2) {
          rangeWithDots.push(l + 1);
        } else if (i - l !== 1) {
          rangeWithDots.push('...');
        }
      }
      rangeWithDots.push(i);
      l = i;
    }

    return rangeWithDots.map((number, idx) => (
      <button
        key={idx}
        className={`pagination-btn ${currentPage === number ? 'active' : ''}`}
        onClick={() => typeof number === 'number' && paginate(number)}
        disabled={number === '...'}
      >
        {number}
      </button>
    ));
  };

  return (
    <div className="pagination-container">
      <button
        className="pagination-btn"
        onClick={() => paginate(1)}
        disabled={currentPage === 1}
      >
        &laquo; First
      </button>
      <button
        className="pagination-btn"
        onClick={() => paginate(currentPage - 1)}
        disabled={currentPage === 1}
      >
        &lsaquo; Prev
      </button>

      {renderPageNumbers()}

      <button
        className="pagination-btn"
        onClick={() => paginate(currentPage + 1)}
        disabled={currentPage === totalPages}
      >
        Next &rsaquo;
      </button>
      <button
        className="pagination-btn"
        onClick={() => paginate(totalPages)}
        disabled={currentPage === totalPages}
      >
        Last &raquo;
      </button>
    </div>
  );
}
