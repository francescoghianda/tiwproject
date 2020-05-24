package it.polimi.tiw.utils.dao;


public class SelectOrder
{
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private final String columnName;
    private final String order;

    public SelectOrder(String columnName, String order)
    {
        if(!(order.equals(ASC) || order.equals(DESC)))throw new IllegalArgumentException("Invalid order ("+order+").");
        this.columnName = columnName;
        this.order = order;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public String getOrder()
    {
        return order;
    }

    @Override
    public String toString()
    {
        return columnName+" "+order;
    }
}
