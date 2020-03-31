package it.polimi.tiw.beans;

import java.io.Serializable;

public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    public enum Role
    {
        MANAGER, WORKER
    }

    private String username;
    private String email;
    private String password;
    private Role role;

    public User(String username, String email, String password, Role role)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User()
    {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
