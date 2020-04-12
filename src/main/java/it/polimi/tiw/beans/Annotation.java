package it.polimi.tiw.beans;

import it.polimi.tiw.beans.validation.annotations.Enum;
import it.polimi.tiw.beans.validation.annotations.IntRange;
import it.polimi.tiw.beans.validation.annotations.NotNull;

import java.sql.Date;

public class Annotation extends Bean
{
    @IntRange(min = 0)
    private int id;

    @IntRange(min = 0)
    private int userId;

    @IntRange(min = 0)
    private int imageId;

    @NotNull
    private Date date;

    private boolean valid;

    @Enum({"GOOD", "MEDIUM", "LOW"})
    private String trust;

    private String notes;

    public Annotation(){}

    public Annotation(int id, int userId, int imageId, Date date, boolean valid, String trust, String notes)
    {
        this.id = id;
        this.userId = userId;
        this.imageId = imageId;
        this.date = date;
        this.valid = valid;
        this.trust = trust;
        this.notes = notes;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getImageId()
    {
        return imageId;
    }

    public void setImageId(int imageId)
    {
        this.imageId = imageId;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    //This method is not called isValid to resolve the conflict with the method isValid of the super-class Bean
    public boolean getValid()
    {
        return valid;
    }

    public void setValid(boolean valid)
    {
        this.valid = valid;
    }

    public String getTrust()
    {
        return trust;
    }

    public void setTrust(String trust)
    {
        this.trust = trust;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }
}
