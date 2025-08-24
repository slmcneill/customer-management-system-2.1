package com.seashells.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = true, unique = true) // <-- uniqueness added
    @JsonProperty("user_name") 
    private String userName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email_address", nullable = false, unique = true) // <-- uniqueness added
    @JsonProperty("email") 
    private String emailAddress;

    @Column(name = "password", nullable = false)
    private String password;

    // --- Getters and Setters ---
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getUserName() { 
        return userName; 
    }
    public void setUserName(String userName) { 
        this.userName = userName; 
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getEmailAddress() { 
        return emailAddress; 
    }
    public void setEmailAddress(String emailAddress) { 
        this.emailAddress = emailAddress; 
    }

    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }
}
