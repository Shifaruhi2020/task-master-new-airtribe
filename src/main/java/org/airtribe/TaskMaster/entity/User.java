package org.airtribe.TaskMaster.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    // private String email;

    private String role;

    private boolean is_Enabled;

    public User(){

    }
    public User(Long id, String username, String password, String role, boolean is_Enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        // this.email = email;
        this.role = role;
        this.is_Enabled = is_Enabled;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    // public String getEmail() {
    //     return email;
    // }
    // public void setEmail(String email) {
    //     this.email = email;
    // }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean isIs_Enabled() {
        return is_Enabled;
    }
    public void setIs_Enabled(boolean is_Enabled) {
        this.is_Enabled = is_Enabled;
}
}
