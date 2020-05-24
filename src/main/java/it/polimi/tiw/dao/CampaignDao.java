package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Campaign;
import it.polimi.tiw.beans.validation.InvalidBeanException;
import it.polimi.tiw.utils.beans.CampaignBeanFactory;
import it.polimi.tiw.utils.dao.Dao;
import it.polimi.tiw.utils.dao.SelectOrder;
import it.polimi.tiw.utils.sql.ConnectionManager;
import it.polimi.tiw.utils.sql.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CampaignDao extends Dao<Campaign>
{

    public CampaignDao()
    {
        super("campaign", new CampaignBeanFactory());
        addSelectOrder(new SelectOrder("id", SelectOrder.DESC));
    }

    public List<Campaign> findCampaignByStatus(String status) throws SQLException
    {
        return rawGet("SELECT * from campaign where status = ?::campaign_status", status);
    }

    public List<Campaign> findCampaignByManagerId(int managerId) throws SQLException
    {
        return findBy( "manager_id", managerId);
    }

    public Optional<Campaign> findCampaignByName(String name) throws SQLException
    {
        return findFirstBy("name", name);
    }

    public Optional<Campaign> findCampaignById(int id) throws SQLException
    {
        return findFirstBy("id", id);
    }

    public boolean existSubscription(int campaignId, int userId) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("SELECT true FROM subscription WHERE campaign_id = ? and user_id = ?"))
        {
            statement.setInt(1, campaignId);
            statement.setInt(2, userId);

            try(ResultSet resultSet = statement.executeQuery())
            {
                return resultSet.next();
            }
        }
    }

    public boolean addSubscription(int campaignId, int userId) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("INSERT INTO subscription (user_id, campaign_id) VALUES (?, ?) ON CONFLICT DO NOTHING"))
        {
            statement.setInt(1, userId);
            statement.setInt(2, campaignId);

            return statement.executeUpdate() == 1;
        }
    }

    public List<Integer> findSubscription(int userId) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("SELECT campaign_id FROM subscription WHERE user_id = ?"))
        {
            statement.setInt(1, userId);

            try(ResultSet resultSet = statement.executeQuery())
            {
                List<Integer> ids = new ArrayList<>();
                while (resultSet.next())ids.add(resultSet.getInt("campaign_id"));
                return ids;
            }
        }
    }

    public boolean updateCampaignStatus(int campaignId, String status) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("UPDATE campaign SET status = ?::campaign_status WHERE id = ?"))
        {
            statement.setString(1, status);
            statement.setInt(2, campaignId);
            return statement.executeUpdate() != 0;
        }
    }

    public boolean updateCampaignStatus(String campaignName, String status) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("UPDATE campaign SET status = ?::campaign_status = ? WHERE name = ?"))
        {
            statement.setString(1, status);
            statement.setString(2, campaignName);
            return statement.executeUpdate() != 0;
        }
    }

    public boolean insertCampaign(Campaign campaign) throws SQLException, InvalidBeanException
    {
        if(!campaign.isValid()) throw new InvalidBeanException(campaign.getValidation().orElse(null));

        return transaction(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO campaign (manager_id, name, client, status) VALUES (?, ?, ?, ?::campaign_status)", Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, campaign.getManagerId());
            statement.setString(2, campaign.getName());
            statement.setString(3, campaign.getClient());
            statement.setString(4, campaign.getStatus());

            if(statement.executeUpdate() == 0) rollback("Campaign not inserted.");

            try (ResultSet generatedKeys = statement.getGeneratedKeys())
            {
                if (!generatedKeys.next()) rollback("Campaign id (key) not generated.");
                campaign.setId(generatedKeys.getInt(1));
            }
        });
    }

    public boolean campaignNameExist(String campaignName) throws SQLException
    {
        return exist("name", campaignName);
    }

    public boolean deleteCampaign(int campaignId) throws SQLException
    {
        try(PooledConnection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.getConnection().prepareStatement("DELETE FROM campaign WHERE id = ?"))
        {
            statement.setInt(1, campaignId);
            return statement.executeUpdate() == 1;
        }
    }

}
