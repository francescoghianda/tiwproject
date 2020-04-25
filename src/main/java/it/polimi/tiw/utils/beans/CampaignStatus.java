package it.polimi.tiw.utils.beans;

public class CampaignStatus
{
    private CampaignStatus(){}

    public static final String CREATED = "CREATED";
    public static final String STARTED = "STARTED";
    public static final String CLOSED = "CLOSED";
    public static final String[] STATUS = {CREATED, STARTED, CLOSED};

    public static boolean isValid(String status)
    {
        return status != null && (status.equals(CREATED) || status.equals(STARTED) || status.equals(CLOSED));
    }
}
