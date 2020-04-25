package it.polimi.tiw.beans;

public class UserWorker
{
    private final User user;
    private final Worker worker;

    public UserWorker(User user, Worker worker)
    {
        this.user = user;
        this.worker = worker;
    }

    public User getUser()
    {
        return user;
    }

    public Worker getWorker()
    {
        return worker;
    }
}
