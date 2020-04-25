package it.polimi.tiw.beans;

import it.polimi.tiw.beans.validation.annotations.Enum;
import it.polimi.tiw.beans.validation.annotations.IntRange;
import it.polimi.tiw.beans.validation.annotations.Size;

import java.io.Serializable;

public class Campaign extends Bean
{

    @IntRange(min = 0)
    private int id;

    @IntRange(min = 0)
    private int managerId;

    @Size(min = 1)
    private String name;

    @Size(min = 1)
    private String client;

    @Enum({"CREATED", "STARTED", "CLOSED"})
    private String status;


    public Campaign(int id, int managerId, String name, String client, String status)
    {
        this.id = id;
        this.managerId = managerId;
        this.name = name;
        this.client = client;
        this.status = status;
    }

    public Campaign(){}

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getManagerId()
    {
        return managerId;
    }

    public void setManagerId(int managerId)
    {
        this.managerId = managerId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getClient()
    {
        return client;
    }

    public void setClient(String client)
    {
        this.client = client;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
