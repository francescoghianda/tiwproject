package it.polimi.tiw.beans;

public class User
{

    private String username;
    private String email;
    private String role;
    private String password;

    public User(String username, String email, String role, String password)
    {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
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
