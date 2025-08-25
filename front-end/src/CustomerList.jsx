import './CustomerList.css';

export function CustomerList(params){
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
            {params.customers.map(
              (item, index) => {
                return (<tr key={item.id}
                  className={ (item.id === params.formObject.id )?'selected': ''}
                  onClick={()=>params.handleListClick(item)}
                >
            <td>{item.id}</td>
            <td>{item.name}</td>
            <td>{item.email}</td>
            <td>{item.password}</td>
                </tr>);
              }
            )}
          </tbody>
        </table>
      </div>
    );
}