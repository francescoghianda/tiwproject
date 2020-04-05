package it.polimi.tiw.utils.dao;

import java.sql.SQLException;

public interface TransactionConsumer<T>
{
    void accept(T object) throws SQLException;
}
