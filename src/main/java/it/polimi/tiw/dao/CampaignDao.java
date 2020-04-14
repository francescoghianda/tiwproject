package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.utils.beans.CampaignBeanFactory;
import it.polimi.tiw.utils.dao.Dao;
import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CampaignDao extends Dao<Campaign>
{

    public CampaignDao()
    {
        super("campaign", new CampaignBeanFactory());
    }

    public List<Campaign> findCampaignByStatus(String status) throws SQLException
    {
        return findBy("status", status);
    }

    public List<Campaign> findCampaignByManagerId(int managerId) throws SQLException
    {
        return findBy( "manager_id", managerId);
    }

    public Optional<Campaign> findCampaignByName(String name) throws SQLException
    {
        return findFirstBy("name", name);
    }

    public boolean updateCampaignStatus(int campaignId, String status) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("UPDATE campaign SET status = ? WHERE id = ?"))
        {
            statement.setString(1, status);
            statement.setInt(2, campaignId);
            return statement.executeUpdate() != 0;
        }
    }

    public boolean updateCampaignStatus(String campaignName, String status) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("UPDATE campaign SET status = ? WHERE name = ?"))
        {
            statement.setString(1, status);
            statement.setString(2, campaignName);
            return statement.executeUpdate() != 0;
        }
    }

    public boolean insertCampaign(Campaign campaign) throws SQLException, InvalidBeanException
    {
        if(!campaign.isValid()) throw new InvalidBeanException(campaign.getValidation().orElse(null));

        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("INSERT INTO campaign (manager_id, name, client, status) VALUES (?, ?, ?, ?::campaign_status)"))
        {
            statement.setInt(1, campaign.getManagerId());
            statement.setString(2, campaign.getName());
            statement.setString(3, campaign.getClient());
            statement.setString(4, campaign.getStatus());

            return statement.executeUpdate() >= 1;
        }
    }

}
