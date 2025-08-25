// servers must allow CORS requests for these urls to work
const custBaseURL = 'http://localhost:8080/api/customers';
const authBaseUrl = 'http://localhost:8081/account';


// Use localStorage to persist JWT token
const TOKEN_KEY = 'jwt_token';


/* CUSTOMER REQUESTS */


function getStoredToken() {
  return localStorage.getItem(TOKEN_KEY);
}

let getHeaders = () => {
  const myHeaders = new Headers({ "Content-Type": "application/json" });
  const storedToken = getStoredToken();
  if (storedToken) {
    myHeaders.append("Authorization", "Bearer " + storedToken);
  }
  return myHeaders;
}

export async function getAll(setCustomers) {
  const myInit = {
    method: 'GET',
    mode: 'cors',
  headers: getHeaders()
  };
  const fetchData = async (url) => {
    try {
      const response = await fetch(url, myInit);
      if (!response.ok) {
        throw new Error(`Error fetching data: ${response.status}`);
      }
      const data = await response.json();
      setCustomers(data);
    } catch (error) {
      alert(error);
    }
  }
  fetchData(custBaseURL);
}

export async function deleteById(id, postopCallback) {
  const myInit = {
    method: 'DELETE',
    mode: 'cors',
  headers: getHeaders()
  };
  const deleteItem = async (url) => {
    try {
      const response = await fetch(url, myInit);
      if (!response.ok) {
        throw new Error(`Error deleting data: ${response.status}`);
      }
      postopCallback();
    } catch (error) {
      alert(error);
    }
  }
  deleteItem(custBaseURL + "/" + id);
}

export function post(customer, postopCallback) {
  delete customer.id;
  const myInit = {
    method: 'POST',
    body: JSON.stringify(customer),
    headers: {
      "Content-Type": "application/json",
  
    },
    mode: 'cors'
  };
  const postItem = async (url) => {
    try {
      const response = await fetch(url, myInit);
      if (!response.ok) {
        throw new Error(`Error posting data: ${response.status}`);
      }
      postopCallback();
    } catch (error) {
      alert(error);
    }
  }
  postItem(custBaseURL);
}

export function put(customer, postopCallback) {
  const myInit = {
    method: 'PUT',
    body: JSON.stringify(customer),
  headers: getHeaders(),
    mode: 'cors'
  };
  const putItem = async (url) => {
    try {
      const response = await fetch(url, myInit);
      if (!response.ok) {
        throw new Error(`Error puting data: ${response.status}`);
      }
      postopCallback();
    } catch (error) {
      alert(error);
    }
  }
  putItem(custBaseURL + "/" + customer.id);
}

export function lookupCustomerByName(username) {
  var myInit = {
    method: 'POST',
    body: username,
  headers: getHeaders(),
    mode: 'cors'
  };
  const lookupCustomer = async (url) => {
    try {
      const response = await fetch(url, init);
      if (!response.ok) {
        throw new Error(`Error looking up customer: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      alert(error);
    }
  };
  lookupCustomer(custBaseURL + "/byname");
}


/* LOGIN REQUESTS */
export async function registerUser(name, username, password, email) {
  let url = authBaseUrl + "/register";
  let customer = {
    name: name,
    user_name: username,
    email: email,
    password: password
  };
  let body = JSON.stringify(customer);
  var myInit = {
    method: 'POST',
    body: body,
    headers: getHeaders(),
    mode: 'cors'
  };
  try {
    const response = await fetch(url, myInit);
    if (!response.ok) {
      throw new Error(`Error registering user: ${response.status}`);
    }
    const text = await response.text();
    return text;
  } catch (error) {
    alert(error);
  }
}

export function callTokenService(customer) {
  let url = authBaseUrl +"/token";
  let body = JSON.stringify(customer);

  var myInit = {
    method: 'POST',
    body: body,
    headers: getHeaders(),
    mode: 'cors'
  };
  let promise = fetch(url, myInit);
  return promise;
}



export async function getJWTToken(username, password) {
  let customer = { "name": username, password };

  const response = await callTokenService(customer);
  if (!response.ok) {
    return { "status": "error", "message": "Login failed: " + "Invalid Credentials" };
  }

  const data = await response.json(); // parse JSON response
  // Store token in localStorage
  localStorage.setItem(TOKEN_KEY, data.token);
  return { "status": "success", "message": "Login successful", "token": data.token };
}

// Logout function to remove JWT from localStorage
export function logout() {
  localStorage.removeItem(TOKEN_KEY);
}



