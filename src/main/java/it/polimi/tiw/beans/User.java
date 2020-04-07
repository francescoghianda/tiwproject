package it.polimi.tiw.beans;

import it.polimi.tiw.beans.validation.annotations.Email;
import it.polimi.tiw.beans.validation.annotations.Enum;
import it.polimi.tiw.beans.validation.annotations.IntRange;
import it.polimi.tiw.beans.validation.annotations.Size;

import java.io.Serializable;

public class User extends Bean implements Serializable
{
    private static final long serialVersionUID = 1L;

    @IntRange(min = 0)
    private int id;

    @Size(min = 4, max = 20)
    private String username;

    @Email
    private String email;

    @Size(min = 6)
    private String password;

    @Enum({"MANAGER", "WORKER"})
    private String role;

    public User(String username, String email, String password, String role)
    {
        this.id = 0;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(int id, String username, String email, String password, String role)
    {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User()
    {

    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
