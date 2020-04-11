package it.polimi.tiw.utils.logger;

public class Logger
{
    public static final LoggerPrinter out = new BasicLoggerPrinter();

    private Logger() {}

    public static LoggerPrinter out()
    {
        return out;
    }

}
