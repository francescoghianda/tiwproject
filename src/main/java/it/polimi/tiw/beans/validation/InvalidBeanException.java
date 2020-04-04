package it.polimi.tiw.beans.validation;

public class InvalidBeanException extends Exception
{
    public InvalidBeanException(){}

    public InvalidBeanException(String message)
    {
        super(message);
    }

    public InvalidBeanException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
