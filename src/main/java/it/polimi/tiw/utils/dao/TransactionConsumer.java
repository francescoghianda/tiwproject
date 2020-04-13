package it.polimi.tiw.utils.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionConsumer
{
    void accept(Connection connection) throws SQLException, RollbackException;
}
