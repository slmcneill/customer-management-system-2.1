
import React, { useState } from 'react';
import './CustomerList.css';

export function CustomerList(params) {
  const [page, setPage] = useState(0);
  const rowsPerPage = 5;
  const totalPages = Math.ceil(params.customers.length / rowsPerPage);

  const handlePrev = () => {
    setPage((prev) => Math.max(prev - 1, 0));
  };
  const handleNext = () => {
    setPage((prev) => Math.min(prev + 1, totalPages - 1));
  };

  const startIdx = page * rowsPerPage;
  const endIdx = startIdx + rowsPerPage;
  const visibleCustomers = params.customers.slice(startIdx, endIdx);

  return (
    <div>
      <table className="customer-list-table">
        <thead className="table-header">
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Password</th>
          </tr>
        </thead>
        <tbody>
          {visibleCustomers.map((item) => (
            <tr
              key={item.id}
              className={item.id === params.formObject.id ? 'selected' : ''}
              onClick={() => params.handleListClick(item)}
            >
              <td>{item.id}</td>
              <td>{item.name}</td>
              <td>{item.email}</td>
              <td>{item.password}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="button-bar" style={{ marginTop: '10px' }}>
        <button onClick={handlePrev} disabled={page === 0}>Previous</button>
        <span style={{ color: '#53858D', fontWeight: 'bold', alignSelf: 'center' }}>
          Page {page + 1} of {totalPages}
        </span>
        <button onClick={handleNext} disabled={page === totalPages - 1}>Next</button>
      </div>
    </div>
  );
}