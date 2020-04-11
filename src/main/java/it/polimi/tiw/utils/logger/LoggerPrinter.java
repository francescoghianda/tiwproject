package it.polimi.tiw.utils.logger;

import java.util.Arrays;
import java.util.Optional;

public interface LoggerPrinter
{
    void log(String msg);
    void log(String msg, LogType type);

    default String getCallerName()
    {
        Optional<StackTraceElement> stackTraceElement = Arrays.stream(new Exception().getStackTrace()).filter(elem ->
                !elem.getClassName().equals(this.getClass().getName()) && !elem.getClassName().equals(LoggerPrinter.class.getName())).findFirst();
        return stackTraceElement.map(StackTraceElement::getClassName).orElse("");
    }
}
