package it.polimi.tiw.utils.beans;

import it.polimi.tiw.beans.Campaign;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CampaignBeanFactory extends BeanFactory<Campaign>
{
    @Override
    public Campaign convert(ResultSet resultSet) throws SQLException
    {
        Campaign campaign = new Campaign();
        campaign.setId(resultSet.getInt("id"));
        campaign.setManagerId(resultSet.getInt("manager_id"));
        campaign.setName(resultSet.getString("name"));
        campaign.setClient(resultSet.getString("client"));
        campaign.setStatus(resultSet.getString("status"));
        return campaign;
    }
}
