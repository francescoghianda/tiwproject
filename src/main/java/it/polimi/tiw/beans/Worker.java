package it.polimi.tiw.beans;

import it.polimi.tiw.beans.validation.annotations.Enum;
import it.polimi.tiw.beans.validation.annotations.IntRange;
import it.polimi.tiw.beans.validation.annotations.Size;

public class Worker extends Bean
{
    @IntRange(min = 0)
    private int userId;

    @Enum({"GOOD", "MEDIUM", "LOW"})
    private String expLvl;

    @Size(min = 1)
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
