package it.polimi.tiw.beans;

import it.polimi.tiw.beans.validation.annotations.IntRange;
import it.polimi.tiw.beans.validation.annotations.NotNull;

public class Worker extends Bean
{
    @IntRange(min = 0)
    private int userId;

    @NotNull
    private String expLvl;

    @NotNull
    private String photo;

    public Worker(int userId, String expLvl, String photo)
    {
        this.userId = userId;
        this.expLvl = expLvl;
        this.photo = photo;
    }

    public Worker()
    {

    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getExpLvl() {
        return expLvl;
    }

    public void setExpLvl(String expLvl)
    {
        this.expLvl = expLvl;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }
}
