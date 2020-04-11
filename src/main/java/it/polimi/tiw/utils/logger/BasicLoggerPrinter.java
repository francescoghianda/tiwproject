package it.polimi.tiw.utils.logger;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicLoggerPrinter implements LoggerPrinter
{
    private DateFormat dateFormat;

    public BasicLoggerPrinter()
    {
        dateFormat = new SimpleDateFormat("HH:mm:ss");
    }

    public void setDateFormat(DateFormat dateFormat)
    {
        this.dateFormat = dateFormat;
    }

    @Override
    public void log(String msg)
    {
        log(msg, LogType.INFO);
    }

    @Override
    public void log(String msg, LogType type)
    {
        String callerName = getCallerName();
        String time = dateFormat.format(new Date());
        String completeMessage = time+" ["+type+"] "+callerName+": "+msg;
        PrintStream out = System.out;
        if(type == LogType.ERROR)out = System.err;
        out.println(completeMessage);
    }


}
