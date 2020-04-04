package it.polimi.tiw.beans;

import it.polimi.tiw.beans.validation.annotations.Email;
import it.polimi.tiw.beans.validation.annotations.NotNull;
import it.polimi.tiw.beans.validation.annotations.Size;

import java.io.Serializable;

public class User extends Bean implements Serializable
{
    private static final long serialVersionUID = 1L;

    public enum Role
    {
        MANAGER, WORKER
    }

    @Size(min = 4, max = 20)
    private String username;

    @Email
    private String email;

    @Size(min = 6)
    private String password;

    @NotNull
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
